package me.druwa.be.config.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;
import me.druwa.be.log.LoggingUtils;

class ExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Throwable e) {
            LoggingUtils.dumpThrowable(e);
            response.reset();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.flushBuffer();
        }
    }
}
