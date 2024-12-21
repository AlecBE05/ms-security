package com.codigo.ms_security.aggregates.constans;

public class Constans {

    public static final Boolean STATUS_ACTIVE = true;
    public static final String CLAVE_AccountNonExpired ="isAccountNonExpired";
    public static final String CLAVE_AccountNonLocked ="isAccountNonLocked";
    public static final String CLAVE_CredentialsNonExpired = "isCredentialsNonExpired";
    public static final String CLAVE_Enabled = "isEnabled";
    public static final String ACCESS = "access";
    public static final String ENDPOINTS_PERMIT = "app/authentication/v1/**";
    public static final String ENDPOINTS_USER = "app/user/v1/**";
    public static final String ENDPOINTS_ADMIN = "app/admin/v1/**";

}
