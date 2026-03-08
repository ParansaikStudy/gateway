package com.zqksk.api.domain.user.model;


import java.util.List;

public record CredentialUser(
        String empNo,
        String password,
        List<GrantedAuthority> grantedAuthorities
) {
}
