package com.zqksk.api.jwt;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.util.Assert;

import java.util.Collection;

public class JwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private Converter<Jwt, Collection<GrantedAuthority>> customJwtGrantedAuthoritiesConverter = new CustomJwtGrantedAuthoritiesConverter();

    private String principalClaimName = JwtClaimNames.SUB;

    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        Collection<GrantedAuthority> authorities = customJwtGrantedAuthoritiesConverter.convert(source);
        return new UsernamePasswordAuthenticationToken(source, null, authorities);
    }

    public void setCustomJwtGrantedAuthoritiesConverter(Converter<Jwt, Collection<GrantedAuthority>> customJwtGrantedAuthoritiesConverter) {
        Assert.notNull(customJwtGrantedAuthoritiesConverter, "jwtGrantedAuthoritiesConverter cannot be null");
        this.customJwtGrantedAuthoritiesConverter = customJwtGrantedAuthoritiesConverter;
    }

    public void setPrincipalClaimName(String principalClaimName) {
        Assert.hasText(principalClaimName, "principalClaimName cannot be empty");
        this.principalClaimName = principalClaimName;
    }
}