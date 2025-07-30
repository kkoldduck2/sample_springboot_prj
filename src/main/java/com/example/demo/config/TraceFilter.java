package com.example.demo.config;

import com.example.demo.logging.LoggingContextUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TraceFilter implements Filter {

    private static final String DEFAULT_TOPOLOGY = "1";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) request;

        String globalNo = httpReq.getHeader("X-Global-No");
        String topologySeq = httpReq.getHeader("X-Topology-Seq");

        if (globalNo == null || globalNo.isBlank()) globalNo = generateGlobalNo();
        if (topologySeq == null || topologySeq.isBlank()) topologySeq = DEFAULT_TOPOLOGY;

        LoggingContextUtil.initContext(globalNo, topologySeq);
        try {
            chain.doFilter(request, response);
        } finally {
            LoggingContextUtil.clear();
        }
    }

    private String generateGlobalNo() {
        return "000009" + System.currentTimeMillis();
    }
}
