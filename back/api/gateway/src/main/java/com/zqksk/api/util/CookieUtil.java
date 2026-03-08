package com.zqksk.api.util;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import java.util.Objects;
import java.util.Optional;

public class CookieUtil {

    private CookieUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static Optional<String> getCookie(ServerHttpRequest req, String cookieName) {
        final MultiValueMap<String, HttpCookie> cookies = req.getCookies();
        if (cookies.isEmpty()) {
            return Optional.empty();
        }
        HttpCookie cookie = cookies.getFirst(cookieName);
        return cookie != null ? Optional.of(cookie.getValue()) : Optional.empty();
    }
}
