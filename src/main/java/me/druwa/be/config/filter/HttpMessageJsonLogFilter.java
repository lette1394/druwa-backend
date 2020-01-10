package me.druwa.be.config.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class HttpMessageJsonLogFilter extends OncePerRequestFilter {
    private static final int MAX_LOG_REQUEST_PAYLOAD_LENGTH = 1024 * 1024; // 1MB
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        if (isAsyncDispatch(request)) {
            filterChain.doFilter(request, response);
        } else {
            doFilterWrapped(wrapRequest(request), wrapResponse(response), filterChain);
        }
    }

    protected void doFilterWrapped(ContentCachingRequestWrapper request,
                                   ContentCachingResponseWrapper response,
                                   FilterChain filterChain) throws ServletException, IOException {
        try {
            beforeRequest(request, response);
            filterChain.doFilter(request, response);
        } finally {
            afterRequest(request, response);
            response.copyBodyToResponse();
        }
    }

    protected void beforeRequest(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
        if (log.isInfoEnabled()) {
            logHeaders(request);
        }
    }

    protected void afterRequest(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
        if (log.isInfoEnabled()) {
            logBody("CLIENT -> body", request.getContentAsByteArray(), Charsets.UTF_8.name());

            logHeaders(response);
            logBody("body <- SERVER", response.getContentAsByteArray(), Charsets.UTF_8.name());
        }
    }

    private static ContentCachingRequestWrapper wrapRequest(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            return (ContentCachingRequestWrapper) request;
        } else {
            return new ContentCachingRequestWrapper(request);
        }
    }

    private static ContentCachingResponseWrapper wrapResponse(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            return (ContentCachingResponseWrapper) response;
        } else {
            return new ContentCachingResponseWrapper(response);
        }
    }

    private void logHeaders(final HttpServletRequest request) {
        log.info("");
        log.info("{}", "============= CLIENT -> header =============");

        try {
            new ServletServerHttpRequest(request)
                    .getHeaders()
                    .entrySet()
                    .stream()
                    .map(header -> String.format("%s: %s",
                                                 header.getKey(),
                                                 String.join(", ",
                                                             header.getValue())))
                    .forEach(header -> log.info("\t{}", header));
        } catch (Exception e) {
            log.info("{}", "[cannot log header. e:[{}]]", e);
            throw e;
        }
        log.info("{}", "============================================");
        log.info("");
    }

    private void logHeaders(final HttpServletResponse response) {
        log.info("");
        log.info("{}", "============= header <- SERVER =============");

        try {
            response.getHeaderNames()
                    .forEach(headerName -> response.getHeaders(headerName)
                                                   .forEach(headerValue -> log.info("\t{}",
                                                                                    String.format("%s: %s",
                                                                                                  headerName,
                                                                                                  headerValue))));
        } catch (Exception e) {
            log.info("{}", "[cannot log header. e:[{}]]", e);
            throw e;
        }
        log.info("{}", "============================================");
        log.info("");
    }

    private void logBody(final String title, final byte[] buf, final String encoding) {
        log.info("");
        if (Objects.isNull(buf) || buf.length == 0) {
            log.info("{}", "[body is empty]");
            log.info("{}", "==========================================");
            return;
        }

        String body = "";
        try {
            log.info("{}", String.format("%s %s %s ", "=============", title, "============="));

            final int length = Math.min(buf.length, MAX_LOG_REQUEST_PAYLOAD_LENGTH);
            body = new String(buf, 0, length, encoding);
            final String prettyBody = gson.toJson(JsonParser.parseString(body));

            Arrays.stream(prettyBody.split("\n"))
                  .forEach(line -> log.info("\t{}", line));
        } catch (JsonSyntaxException e) {
            log.info("{}", body);
        } catch (UnsupportedEncodingException e) {
            log.info("{}", "[not readable, cannot log body]");
        } finally {
            log.info("{}", "==========================================");
            log.info("");
        }
    }
}
