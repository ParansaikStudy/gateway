package com.zqksk.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zqksk.api.support.exception.CoreException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.zqksk.api.security.UnauthorizedException.getUnauthorizedExceptionMessage;

@Component
public class OwAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public OwAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        CoreException coreException = UnauthorizedException.getUnauthorizedExceptionMessage(authException);
        response.getOutputStream().write(objectMapper.writeValueAsBytes(coreException));
    }
}
