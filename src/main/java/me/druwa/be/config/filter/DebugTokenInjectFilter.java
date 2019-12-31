package me.druwa.be.config.filter;

import java.io.IOException;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

@Slf4j
@RequiredArgsConstructor
class DebugTokenInjectFilter extends OncePerRequestFilter {
    private final String responseHeader;
    private final String mdcTokenKey;

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws IOException, ServletException {
        try {
            final String token = UUID.randomUUID().toString().toUpperCase().replace("-", "");
            MDC.put(mdcTokenKey, token);

            if (StringUtils.isNotBlank(responseHeader)) {
                response.addHeader(responseHeader, token);
            }

            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(mdcTokenKey);
        }
    }
}


