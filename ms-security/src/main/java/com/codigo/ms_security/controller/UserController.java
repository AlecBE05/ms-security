package com.codigo.ms_security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("app/user/v1/")
public class UserController {

    @GetMapping("saludo")
    public ResponseEntity<String> getSaludo(){
        return ResponseEntity.ok("Hola User!!!");
    }
}
