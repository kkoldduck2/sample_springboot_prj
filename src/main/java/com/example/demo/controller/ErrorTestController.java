package com.example.demo.controller;

import com.example.demo.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/error-test")
@Slf4j
public class ErrorTestController {
    @GetMapping("/runtime")
    public String throwRuntime() {
        log.info("RuntimeException 발생 시도");
        throw new RuntimeException("테스트용 런타임 예외");
    }

    @GetMapping("/null")
    public String throwNull() {
        log.info("NullPointerException 발생 시도");
        String str = null;
        return str.toString();  // NullPointerException
    }

    @GetMapping("/custom")
    public String throwCustom() {
        log.info("CustomException 발생 시도");
        throw new CustomException("사용자 정의 예외 발생");
    }

    @GetMapping("/ok")
    public String success() {
        log.info("정상 요청");
        return "OK";
    }
}
