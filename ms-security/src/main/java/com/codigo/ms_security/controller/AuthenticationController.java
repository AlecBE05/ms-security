package com.codigo.ms_security.controller;

import com.codigo.ms_security.aggregates.request.SignInRequest;
import com.codigo.ms_security.aggregates.request.SignUpRequest;
import com.codigo.ms_security.aggregates.response.SignInResponse;
import com.codigo.ms_security.entity.Usuario;
import com.codigo.ms_security.service.AuthenticationService;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("app/authentication/v1/")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/users")
    public ResponseEntity<Usuario> singUpUsers(@RequestBody SignUpRequest signUpRequest){
        return ResponseEntity.ok(authenticationService.singUpUsers(signUpRequest));
    }

    @PostMapping("/signin")
    public ResponseEntity<SignInResponse> signIn(@RequestBody SignInRequest signUpRequest){
        return ResponseEntity.ok(authenticationService.signIn(signUpRequest));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Usuario>> getAll(){
        return ResponseEntity.ok(authenticationService.allUser());
    }

    @GetMapping("/clave")
    public ResponseEntity<String> getClaveFirma(){
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String dato = Base64.getEncoder().encodeToString(key.getEncoded());
        return ResponseEntity.ok(dato);
    }

}
