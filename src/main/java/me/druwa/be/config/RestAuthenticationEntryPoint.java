package me.druwa.be.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException e) {

        log.error("Responding with unauthorized error. Message - {}", e.getMessage());
        response.reset();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
