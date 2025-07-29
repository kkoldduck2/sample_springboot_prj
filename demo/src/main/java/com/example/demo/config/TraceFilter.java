package com.example.demo.config;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class TraceFilter implements Filter {
    private static final String HEADER_GLOBAL_NO = "X-Global-No";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        try {
            HttpServletRequest request = (HttpServletRequest) req;

            // 비즈니스 추적용 globalNo
            String globalNo = request.getHeader(HEADER_GLOBAL_NO);
            if (globalNo != null && !globalNo.isBlank()) {
                MDC.put("globalNo", globalNo);
            }

            // APM trace.id는 Agent가 MDC에 자동으로 넣음 (enable_log_correlation=true)

            chain.doFilter(req, res);
        } finally {
            MDC.clear();
        }
    }
}
