package com.zqksk.api.service;

import com.zqksk.api.config.OpenZqkskProperties;
import com.zqksk.api.domain.user.component.*;
import com.zqksk.api.domain.user.model.*;
import com.zqksk.api.domain.user.model.request.CreateUser;
import com.zqksk.api.domain.user.model.response.*;
import com.zqksk.api.exception.AuthenticationErrorType;
import com.zqksk.api.jwt.JwtTokenProvider;
import com.zqksk.api.model.TokenResponse;
import com.zqksk.api.security.UnauthorizedException;
import com.zqksk.api.support.exception.CoreException;
import com.zqksk.mail.model.VerificationMail;
import com.zqksk.mail.service.MailService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final OpenZqkskProperties openZqkskProperties;
    private final UserAuthFinder userAuthFinder;
    private final UserAppender userAppender;
    private final UserFinder userFinder;
    private final EmployeeFinder employeeFinder;
    private final DepartmentFinder departmentFinder;
    private final MailService mailService;
    private final CacheService cacheService;
    private final UserAuthAppender userAuthAppender;
    private final RoleFinder roleFinder;
    private final UserModifier userModifier;

    @Value("${spring.profiles.active:prod}")
    private String profile;

    public TokenResponse issueToken(@Nonnull final String employeeNo, @Nonnull final String password) {
//        List<GrantedAuthority> userAuthorities = userAuthFinder.getUserAuthByEmployeeNo(employeeNo);

//        if (userAuthorities.isEmpty()) {
//            userAuthorities = List.of(new GrantedAuthority(AuthorityType.GUEST));
//        }

        List<GrantedAuthority> userAuthorities = List.of(new GrantedAuthority(AuthorityType.GUEST));

        CredentialUser credentialUser = new CredentialUser(employeeNo, password, userAuthorities);
        return issueTokenForValidatedUser(credentialUser);
    }

    private void validateAuthentication(String enteredPassword, String password) {
        if(!passwordEncoder.matches(enteredPassword, password)) {
            throw UnauthorizedException.InvalidAuthentication.INSTANCE;
        }
    }

    private TokenResponse issueTokenForValidatedUser(CredentialUser credentialUser) {
        return jwtTokenProvider.createAuthenticationToken(credentialUser);
    }

    public String callOpenZqksk(String employeeNo, String password) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        byte[] encodedPassword = md.digest(password.getBytes());

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add(openZqkskProperties.queryId(), employeeNo);
        queryParams.add(openZqkskProperties.queryPassword(), new String(Base64.encodeBase64(encodedPassword)));

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme(openZqkskProperties.scheme())
                .host(openZqkskProperties.host())
                .path(openZqkskProperties.path())
                .queryParams(queryParams)
                .build();

        return RestClient
                    .create()
                    .get()
                    .uri(uriComponents.toUri())
                    .retrieve()
                    .body(String.class);
    }

    @Transactional
    public User createUser(@Nonnull final String employeeNo, @Nonnull final String password) {
        Employee employee = employeeFinder.getEmployeeByEmployeeNo(employeeNo);
        Department department = departmentFinder.getDepartmentByDepartmentCode(employee.departmentCode());
        User user = userAppender.append(CreateUser.builder()
                .employeeNo(employeeNo)
                .email(employee.email())
                .name(employee.name())
                .departmentName(department.name())
                .positionName(Position.findByCode(employee.positionCode()).getName())
                .password(passwordEncoder.encode(password))
                .build());

        Role role = roleFinder.getRoleByType("GUEST");

        userAuthAppender.append(role.id(), user.id());

        return user;
    }

    @Transactional
    public void preCreateUser(@Nonnull final String employeeNo) {
        User existUser = userFinder.getUserByEmployeeNo(employeeNo);
        if (existUser != null) {
            throw new CoreException(AuthenticationErrorType.ALREADY_EXIST_USER);
        }

        Employee employee = employeeFinder.getEmployeeByEmployeeNo(employeeNo);
        String verificationCode = cacheService.generateAuthenticationCode(employeeNo);

        mailService.sendAuthenticationCode(
                VerificationMail.builder()
                        .recipient(employee.email())
                        .verificationCode(verificationCode)
                        .build()
        );
    }

    @Transactional
    public void sendAuthenticationCode(@Nonnull final String employeeNo) {
        Employee employee = employeeFinder.getEmployeeByEmployeeNo(employeeNo);
        String verificationCode = cacheService.generateAuthenticationCode(employeeNo);

        mailService.sendAuthenticationCode(
                VerificationMail.builder()
                        .recipient(employee.email())
                        .verificationCode(verificationCode)
                        .build()
        );
    }

    @Transactional(readOnly = true)
    public boolean authentication(@Nonnull final String employeeNo, @Nonnull final String inputCode) {
        String cachedCode = cacheService.getAuthenticationCode(employeeNo);
        return cachedCode != null && cachedCode.equals(inputCode);
    }

    @Transactional(readOnly = true)
    public boolean verify(@Nonnull final String employeeNo, @Nonnull final String inputCode) {
        String cachedCode = cacheService.getVerificationCode(employeeNo);
        return cachedCode != null && cachedCode.equals(inputCode);
    }

    @Transactional
    public void changePassword(@Nonnull final String employeeNo, @Nonnull final String password) {
        userModifier.changePassword(employeeNo, passwordEncoder.encode(password));
    }

    @Transactional(readOnly = true)
    public boolean verifyPassword(@Nonnull final String employeeNo, @Nonnull final String password) {
        UserLogin user = userFinder.getUserPasswordByEmployeeNo(employeeNo);
        validateAuthentication(password, user.password());
        return true;
    }
}
