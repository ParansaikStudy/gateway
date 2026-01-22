package com.zqksk.api.jwt;

import com.zqksk.api.config.JwtProperties;
import com.zqksk.api.domain.user.model.CredentialUser;
import com.zqksk.api.domain.user.model.GrantedAuthority;
import com.zqksk.api.model.TokenResponse;
import com.zqksk.api.security.UnauthorizedException;
import com.zqksk.api.util.KeyGenerator;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public JwtTokenProvider(JwtProperties jwtProperties, JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtProperties = jwtProperties;
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public TokenResponse createAuthenticationToken(CredentialUser credentialUser) {
        String key = KeyGenerator.generateKey();
        String accessToken = issueAccessToken(key, credentialUser);
        String refreshToken = issueRefreshToken(key, credentialUser, accessToken);

        return TokenResponse.from(accessToken, refreshToken, jwtProperties.accessExpirationTime());
    }

    public String validateToken(String accessToken, String refreshToken) throws AuthenticationException {
        try {
            Jwt jwt = jwtDecoder.decode(accessToken);
            return jwt.getTokenValue();
        } catch (JwtValidationException exception) {
            throw UnauthorizedException.InvalidToken.INSTANCE;
        } catch (BadJwtException exception) {
            throw UnauthorizedException.InvalidToken.INSTANCE;
        } catch (JwtException exception) {
            throw new AuthenticationServiceException(exception.getMessage(), exception);
        }
    }

    public String getUserEmployeeNo(String accessToken) {
        if (StringUtils.isBlank(accessToken)) {
            return "anonymous";
        }

        Jwt jwt = jwtDecoder.decode(accessToken);
        return jwt.getClaim("user");
    }

    private String issueAccessToken(String jwtId, CredentialUser credentialUser) {
        Instant issuedAt = Instant.now();
        Map<String, Object> claims = new HashMap<>();
        claims.put("user", credentialUser.empNo());
        claims.put("roles", credentialUser.grantedAuthorities().stream()
                .map(GrantedAuthority::authorityType)
                .toList()
        );

        return generateToken(jwtId, issuedAt.plusSeconds(jwtProperties.accessExpirationTime()), issuedAt, claims);
    }

    private String issueRefreshToken(String jwtId, CredentialUser credentialUser, String accessToken) {
        Instant issuedAt = Instant.now();
        return generateToken(jwtId, issuedAt.plusSeconds(jwtProperties.refreshExpirationTime()), issuedAt, null);
    }

    private String generateToken(String jwtId, Instant expiresAt, Instant issuedAt, Map<String, Object> claims) {
        JwtClaimsSet.Builder jwtClaimsSetBuilder = JwtClaimsSet.builder()
                .id(jwtId)
                .subject("zqksk")
                .expiresAt(expiresAt)
                .issuedAt(issuedAt)
                .issuer("zqksk-auth-service");
        if (claims != null) {
            jwtClaimsSetBuilder.claims(stringObjectMap -> stringObjectMap.putAll(claims));
        }
        JwtClaimsSet jwtClaimsSet = jwtClaimsSetBuilder.build();
        try {
            return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
        } catch (IllegalArgumentException e) {
            throw new InvalidBearerTokenException(e.getMessage());
        }
    }
}