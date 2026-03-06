package com.zqksk.api.controller.v1;

import com.zqksk.api.domain.user.service.UserAuthService;
import com.zqksk.api.exception.AuthenticationErrorType;
import com.zqksk.api.jwt.JwtTokenProvider;
import com.zqksk.api.model.*;
import com.zqksk.api.service.AuthService;
import com.zqksk.api.service.CacheService;
import com.zqksk.api.support.exception.CoreException;
import com.zqksk.api.support.response.ApiResponse;
import com.zqksk.api.util.CookieUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth/v1")
public class AuthController {

    private final UserAuthService userAuthService;
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CacheService cacheService;

    @PostMapping({"/verify", "/validate"})
    public ResponseEntity<Boolean> validateAndVerify(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestHeader(value = "Authorization", required = false) @Nullable final String key) {

        boolean isKeyValid = false;
        boolean isTokenValid = false;

        if (key != null) {
            isKeyValid = verifyHeader(key);
        }

        if (!isKeyValid) {
            isTokenValid = validateCookies(request, response);
        }

        boolean finalValidationResult = isKeyValid || isTokenValid;

        return ResponseEntity.ok(finalValidationResult);
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody final LoginRequest loginRequest, HttpServletResponse response) {
        //String result = authService.callOpenZqksk(loginRequest.employeeNo(), loginRequest.password());
        boolean result = authService.verifyPassword(loginRequest.employeeNo(), loginRequest.password());

        if(!result) {
            throw new CoreException(AuthenticationErrorType.INVALID_USER);
        }

        TokenResponse tokenResponse = authService.issueToken(loginRequest.employeeNo(), loginRequest.password());

        CookieUtil.addCookie(response, AuthorizedCookie.ACCESS_TOKEN.getCookieName(), tokenResponse.accessToken(), (int) tokenResponse.expiresIn());
        CookieUtil.addCookie(response, AuthorizedCookie.REFRESH_TOKEN.getCookieName(), tokenResponse.refreshToken(), (int) tokenResponse.expiresIn());

        return ResponseEntity.ok(ApiResponse.of(result));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse> signUp(@RequestBody final LoginRequest loginRequest, HttpServletResponse response) {
        String result = authService.callOpenZqksk(loginRequest.employeeNo(), loginRequest.password());

        if(!"true".equals(result)) {
            throw new CoreException(AuthenticationErrorType.INVALID_ID_OR_PASSWORD);
        }

        authService.preCreateUser(loginRequest.employeeNo());

        return ResponseEntity.ok(ApiResponse.of(result));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestBody final LoginRequest loginRequest, HttpServletResponse response) {
        authService.sendAuthenticationCode(loginRequest.employeeNo());

        return ResponseEntity.ok(ApiResponse.of(true));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.deleteCookie(request, response, "oat");
        CookieUtil.deleteCookie(request, response, "ort");

        return ResponseEntity.ok().build();
    }

    @PostMapping("/authentication")
    public ResponseEntity<ApiResponse> authentication(@RequestBody final AuthenticationRequest loginRequest) {
        boolean result = authService.authentication(loginRequest.employeeNo(), loginRequest.inputCode());

        if (!result) {
            throw new CoreException(AuthenticationErrorType.INVALID_AUTHENTICATION_CODE);
        }

//        authService.createUser(loginRequest.employeeNo());
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("authentication", result);
        resultMap.put("verificationCode", cacheService.generateVerificationCode(loginRequest.employeeNo()));

        return ResponseEntity.ok(ApiResponse.of(resultMap));
    }

    @PostMapping("/create-user")
    public ResponseEntity<ApiResponse> createUser(@RequestBody final CreateUserRequest createUserRequest) {
        boolean result = authService.verify(createUserRequest.employeeNo(), createUserRequest.verificationCode());

        if (!result) {
            throw new CoreException(AuthenticationErrorType.INVALID_VERIFICATION_CODE);
        }
        authService.createUser(createUserRequest.employeeNo(), createUserRequest.password());


        return ResponseEntity.ok(ApiResponse.of(result));
    }


    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse> changePassword(@RequestBody final ChangePasswordRequest changePasswordRequest) {
        boolean result = authService.verify(changePasswordRequest.employeeNo(), changePasswordRequest.verificationCode());

        if (!result) {
            throw new CoreException(AuthenticationErrorType.INVALID_VERIFICATION_CODE);
        }

        authService.changePassword(changePasswordRequest.employeeNo(), changePasswordRequest.password());

        return ResponseEntity.ok(ApiResponse.of(result));
    }

    @GetMapping("/my-info")
    public ResponseEntity<ApiResponse> getMyInfo(HttpServletRequest request) {
        String accessToken = CookieUtil.getCookie(request, AuthorizedCookie.ACCESS_TOKEN.getCookieName())
                .map(Cookie::getValue)
                .orElse(null);

        String refreshToken = CookieUtil.getCookie(request, AuthorizedCookie.REFRESH_TOKEN.getCookieName())
                .map(Cookie::getValue)
                .orElse(null);

        if (accessToken != null && refreshToken != null) {
            String token = jwtTokenProvider.validateToken(accessToken, refreshToken);

            if (!"".equals(token)) {
                return ResponseEntity.ok(ApiResponse.of(userAuthService.getUserPrivileges(jwtTokenProvider.getUserEmployeeNo(token))));
            }
        }

        throw new CoreException(AuthenticationErrorType.INVALID_AUTHORIZATION);
    }

    public boolean verifyHeader(@Nonnull final String key) {
        if (!key.matches("^ZQKSK AK\\s+[A-Za-z0-9]+$")) {
            log.info("Invalid Authorization Header: {}", key);
            throw new CoreException(AuthenticationErrorType.INVALID_AUTHORIZATION, key);
        }

        String cleanedKey = key.replace("ZQKSK AK ", "");
        return userAuthService.validateKey(cleanedKey);
    }

    public boolean validateCookies(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = CookieUtil.getCookie(request, AuthorizedCookie.ACCESS_TOKEN.getCookieName())
                .map(Cookie::getValue)
                .orElse(null);

        String refreshToken = CookieUtil.getCookie(request, AuthorizedCookie.REFRESH_TOKEN.getCookieName())
                .map(Cookie::getValue)
                .orElse(null);

        if (accessToken != null && refreshToken != null) {
            String token = jwtTokenProvider.validateToken(accessToken, refreshToken);

            boolean isValid = !"".equals(token);

            if (!isValid) {
                CookieUtil.deleteCookie(request, response, "oat");
                CookieUtil.deleteCookie(request, response, "ort");
            }

            return isValid;
        }

        return false;
    }
}
