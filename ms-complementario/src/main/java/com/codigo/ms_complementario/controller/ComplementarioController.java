package com.codigo.ms_complementario.controller;

import com.codigo.ms_complementario.client.SeguridadClient;
import com.codigo.ms_complementario.response.UsuarioResponseClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/complementario/v1")
@RequiredArgsConstructor
public class ComplementarioController {

    private final SeguridadClient seguridadClient;

    @GetMapping("/info-users/{username}")
    public ResponseEntity<UsuarioResponseClient> getInfoUser(
            @RequestHeader("Authorization") String auth,
            @PathVariable("username") String username){
        return ResponseEntity.ok(seguridadClient.getInfoUser(auth,username));
    }

}
