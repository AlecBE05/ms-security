package com.codigo.ms_security.aggregates.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInRequest {

    private String username;
    private String password;

}
