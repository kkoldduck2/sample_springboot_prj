package com.example.demo.controller;


import com.example.demo.logging.LogTransaction;
import com.example.demo.logging.RestTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class SampleController {

    @Autowired
    RestTemplate restTemplate;

    @LogTransaction(svcName = "/start")
    @GetMapping("/start")
    public ResponseEntity<String> start() {
        HttpEntity<Object> entity = RestTemplateUtil.buildEmptyRequestWithTracingHeaders(1);
        restTemplate.exchange("http://localhost:8080/child", HttpMethod.GET, entity, String.class);
        return ResponseEntity.ok("done");
    }

    @LogTransaction(svcName = "/child")
    @GetMapping("/child")
    public ResponseEntity<String> child() {
        return ResponseEntity.ok("child response");
    }

    @LogTransaction(svcName = "/child_error")
    @GetMapping("/child_error")
    public ResponseEntity<String> child_error() {
        throw new RuntimeException("의도된 에러 발생!");
    }

    @LogTransaction(svcName = "/start2")
    @GetMapping("/start2")
    public ResponseEntity<String> start2() {
        try {
            HttpEntity<Object> entity = RestTemplateUtil.buildEmptyRequestWithTracingHeaders(1);
            restTemplate.exchange("http://localhost:8080/child", HttpMethod.GET, entity, String.class);

            HttpEntity<Object> entity2 = RestTemplateUtil.buildEmptyRequestWithTracingHeaders(2);
            restTemplate.exchange("http://localhost:8080/child_error", HttpMethod.GET, entity2, String.class);

            return ResponseEntity.ok("done");

        } catch (Exception e) {
            throw e; // 다시 던지면 AOP에서 한 번 더 찍혀도 되고, 안 던지면 여기서 끝
        }
    }
}