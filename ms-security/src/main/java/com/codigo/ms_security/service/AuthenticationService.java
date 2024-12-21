package com.codigo.ms_security.service;

import com.codigo.ms_security.aggregates.request.SignInRequest;
import com.codigo.ms_security.aggregates.request.SignUpRequest;
import com.codigo.ms_security.aggregates.response.SignInResponse;
import com.codigo.ms_security.entity.Usuario;

import java.util.List;

public interface AuthenticationService {

    Usuario singUpUsers(SignUpRequest signUpRequest);

    List<Usuario> allUser();

    SignInResponse signIn(SignInRequest signInRequest);

    Usuario findByUsername(String username);

    boolean validateToken(String token);

}
