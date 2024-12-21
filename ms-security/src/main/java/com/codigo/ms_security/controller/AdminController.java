package com.codigo.ms_security.controller;


import com.codigo.ms_security.aggregates.request.SignUpRequest;
import com.codigo.ms_security.entity.Usuario;
import com.codigo.ms_security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("app/admin/v1/")
@RequiredArgsConstructor
public class AdminController {

    private final AuthenticationService authenticationService;

    @PostMapping("/users")
    public ResponseEntity<Usuario> singUpUsers(@RequestBody SignUpRequest signUpRequest){
        return ResponseEntity.ok(authenticationService.singUpUsers(signUpRequest));
    }

    @GetMapping("/users/{username}")
    public  ResponseEntity<Usuario> findByUsername(@PathVariable String username){
        return ResponseEntity.ok(authenticationService.findByUsername(username));
    }

}
