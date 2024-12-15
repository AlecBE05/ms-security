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
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

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
                .password(signUpRequest.getPassword())
                .tipoDoc(signUpRequest.getTipoDoc())
                .numDoc(signUpRequest.getNumDoc())
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
                new UsernamePasswordAuthenticationToken(signInRequest.getEmail(),signInRequest.getPassword()));
        var user = usuarioRepository.findByEmail(signInRequest.getEmail()).orElseThrow(
                () -> new RuntimeException("Error usuario no existe"));
        var token = jwtService.generateToken(user);

        return SignInResponse.builder()
                .token(token)
                .build();
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
