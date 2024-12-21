package com.codigo.ms_complementario.client;

import com.codigo.ms_complementario.response.UsuarioResponseClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ms-security")
public interface SeguridadClient {

    @GetMapping("app/user/v1/users/{username}")
    UsuarioResponseClient getInfoUser(@RequestHeader("Authorization") String auth,
                                      @PathVariable("username") String username);

}
