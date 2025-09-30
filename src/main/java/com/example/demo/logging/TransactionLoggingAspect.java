package com.example.demo.logging;

import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class TransactionLoggingAspect {

    @Pointcut("@annotation(logTransaction)")
    public void logPointcut(LogTransaction logTransaction) {}

    @Before("@annotation(logTransaction)")
    public void logBefore(JoinPoint joinPoint, LogTransaction logTransaction) {
        LogUtil.logTransaction(logTransaction.svcName(), "T", null, null);
    }

    @AfterReturning("@annotation(logTransaction)")
    public void logAfter(JoinPoint joinPoint, LogTransaction logTransaction) {
        LogUtil.logTransaction(logTransaction.svcName(), "R", "I", Map.of("message", "응답 완료"));
    }

    @AfterThrowing(value = "@annotation(logTransaction)", throwing = "ex")
    public void logException(JoinPoint joinPoint, LogTransaction logTransaction, Throwable ex) {
        LogUtil.logTransaction(logTransaction.svcName(), "R", "E", Map.of("message", "에러 발생", "error", ex.getMessage()));
    }
}