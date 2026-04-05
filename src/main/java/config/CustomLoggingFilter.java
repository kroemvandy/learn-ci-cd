package config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Component
public class CustomLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(CustomLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Wrap the request and response
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request, 10240);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            logDetails(requestWrapper, responseWrapper);
            // Crucial: Copy the body back to the actual response
            responseWrapper.copyBodyToResponse();
        }
    }

    private void logDetails(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
        String requestBody = getContent(request.getContentAsByteArray(), request.getCharacterEncoding());
        String responseBody = getContent(response.getContentAsByteArray(), response.getCharacterEncoding());

        logger.info("METHOD: {}; URL: {}; REQUEST: {}; STATUS: {}; RESPONSE: {}",
                request.getMethod(),
                request.getRequestURI(),
                requestBody,
                response.getStatus(),
                responseBody);
    }

    private String getContent(byte[] buf, String encoding) {
        if (buf == null || buf.length == 0) return "[empty]";
        try {
            return new String(buf, 0, buf.length, encoding);
        } catch (UnsupportedEncodingException e) {
            return "[unknown]";
        }
    }
}
