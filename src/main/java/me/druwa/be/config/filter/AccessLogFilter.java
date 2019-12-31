package me.druwa.be.config.filter;

import java.io.IOException;
import java.util.StringJoiner;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

@Slf4j
class AccessLogFilter extends OncePerRequestFilter {
    private static final Logger accessLog = LoggerFactory.getLogger("AccessLogger");

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        //TODO: user
        // SecurityContextHolder.getContext().getAuthentication().getDetails()
        filterChain.doFilter(request, response);
        final String requestLog = new StringJoiner("|").add(format("method:%s", request.getMethod()))
                                                       .add(format("uri:%s", request.getRequestURI()))
                                                       .add(format("params:%s",
                                                                   defaultIfBlank(request.getQueryString(),
                                                                                  StringUtils.EMPTY)))
                                                       .add(format("statusCode:%s", response.getStatus()))
                                                       .add(format("client:%s", request.getRemoteAddr()))
                                                       .toString();
        accessLog.info("{}", requestLog);
        log.info("{}", requestLog);
    }

}

