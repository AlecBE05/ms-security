package com.codigo.ms_security.controller;

import com.codigo.ms_security.entity.Usuario;
import com.codigo.ms_security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("app/user/v1/")
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationService authenticationService;

    @GetMapping("/users/{username}")
    public  ResponseEntity<Usuario> findByUsername(@PathVariable String username){
        return ResponseEntity.ok(authenticationService.findByUsername(username));
    }
}
