package com.zqksk.api.jwt;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CustomJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        List<String> getClaim = jwt.getClaim("roles");
        if (getClaim == null) {
            return Collections.emptyList();
        }
        return getClaim.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
