package com.codigo.ms_security.service.impl;

import com.codigo.ms_security.aggregates.constans.Constans;
import com.codigo.ms_security.aggregates.request.SignInRequest;
import com.codigo.ms_security.aggregates.request.SignUpRequest;
import com.codigo.ms_security.aggregates.response.SignInResponse;
import com.codigo.ms_security.entity.Rol;
import com.codigo.ms_security.entity.Usuario;
import com.codigo.ms_security.repository.RolRepository;
import com.codigo.ms_security.repository.UsuarioRepository;
import com.codigo.ms_security.service.AuthenticationService;
import com.codigo.ms_security.service.JwtService;
import com.codigo.ms_security.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    @Override
    public Usuario singUpUsers(SignUpRequest signUpRequest) {
        Usuario usuario = getUsuarioEntity(signUpRequest);
        return usuarioRepository.save(usuario);
    }

    private Usuario getUsuarioEntity(SignUpRequest signUpRequest) {
        Set<Rol> roles = getRols(signUpRequest.getRoles());
        return Usuario.builder()
                .nombres(signUpRequest.getNombres())
                .apellidos(signUpRequest.getApellidos())
                .email(signUpRequest.getEmail())
                .password(new BCryptPasswordEncoder().encode(signUpRequest.getPassword()))
                .tipoDoc(signUpRequest.getTipoDoc())
                .numDoc(signUpRequest.getNumDoc())
                .username(signUpRequest.getNombres().charAt(0)+signUpRequest.getApellidos().toLowerCase())
                .isAccountNonExpired(Constans.STATUS_ACTIVE)
                .isAccountNonLocked(Constans.STATUS_ACTIVE)
                .isCredentialsNonExpired(Constans.STATUS_ACTIVE)
                .isEnabled(Constans.STATUS_ACTIVE)
                .roles(roles)
                .build();
    }

    @Override
    public List<Usuario> allUser() {
        return usuarioRepository.findAll();
    }

    @Override
    public SignInResponse signIn(SignInRequest signInRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getUsername(),signInRequest.getPassword()));
        var user = usuarioRepository.findByUsername(signInRequest.getUsername()).orElseThrow(
                () -> new RuntimeException("Error usuario no existe"));
        var token = jwtService.generateToken(user);
        return SignInResponse.builder()
                .token(token)
                .build();
    }

    @Override
    public Usuario findByUsername(String username) {
        return usuarioRepository.findByUsername(username).orElseThrow(
                ()-> new RuntimeException("Error usuario no existe")
        );
    }

    @Override
    public boolean validateToken(String token) {
        final String jwt;
        final String username;

        if(Objects.nonNull(token) && !token.trim().isEmpty()){
            jwt = token.substring(7);
            username = jwtService.extractUsername(jwt);
            if(Objects.nonNull(username) && !username.trim().isEmpty()){
                UserDetails userDetails = usuarioService
                        .userDetailsService()
                        .loadUserByUsername(username);
                return jwtService.validateToken(jwt,userDetails);
            }
        }
        return false;
    }

    private Set<Rol> getRols(Set<String> roleNames) {
        Set<Rol> roles = new HashSet<>();
        for (String rolNombre : roleNames) {
            Rol rol = rolRepository.findByNombreRol(rolNombre)
                    .orElseThrow(() -> new RuntimeException(
                            "ERROR: EL ROL NO EXISTE: " + rolNombre));
            roles.add(rol);
        }
        return roles;
    }
}
