package com.example.demo.controller;
import com.example.demo.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SelfCallController {
    private final RestTemplate restTemplate;

    @GetMapping("/start")
    public String start(@RequestHeader(value = "X-Global-No", required = false) String globalNo) {
        log.info("[/start] 호출됨. 전달받은 globalNo = {}", globalNo);

        String response = restTemplate.getForObject("http://localhost:8080/internal", String.class);

        return "[/start] 호출 완료 → 내부 응답: " + response;
    }

    @GetMapping("/internal")
    public String internal() {
        log.info("[/internal] 내부 호출됨");
        return "내부 응답";
    }
}
