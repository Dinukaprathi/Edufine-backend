package com.edufine.backend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        long start = System.currentTimeMillis();
        StatusCaptureResponseWrapper responseWrapper = new StatusCaptureResponseWrapper(response);

        try {
            filterChain.doFilter(request, responseWrapper);
        } finally {
            long duration = System.currentTimeMillis() - start;
            String method = request.getMethod();
            String uri = request.getRequestURI();
            String query = request.getQueryString();
            String user = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "anonymous";
            String fullPath = query != null ? uri + "?" + query : uri;
            log.info("HTTP {} {} status={} duration={}ms user={}", method, fullPath, responseWrapper.getStatus(), duration, user);
        }
    }

    private static class StatusCaptureResponseWrapper extends jakarta.servlet.http.HttpServletResponseWrapper {

        private int httpStatus = HttpServletResponse.SC_OK;

        StatusCaptureResponseWrapper(HttpServletResponse response) {
            super(response);
        }

        @Override
        public void setStatus(int sc) {
            this.httpStatus = sc;
            super.setStatus(sc);
        }

        @Override
        public void sendError(int sc) throws IOException {
            this.httpStatus = sc;
            super.sendError(sc);
        }

        @Override
        public void sendError(int sc, String msg) throws IOException {
            this.httpStatus = sc;
            super.sendError(sc, msg);
        }

        @Override
        public void sendRedirect(String location) throws IOException {
            this.httpStatus = HttpServletResponse.SC_MOVED_TEMPORARILY;
            super.sendRedirect(location);
        }

        @Override
        public int getStatus() {
            return this.httpStatus;
        }
    }
}
