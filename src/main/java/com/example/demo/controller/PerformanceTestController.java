package com.example.demo.controller;

import java.util.Random;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.logging.LogTransaction;
import com.example.demo.logging.LogUtil;
import com.example.demo.logging.LoggingContextUtil;
import com.example.demo.logging.RestTemplateUtil;

import lombok.Data;

@RestController
public class PerformanceTestController {
	private final Random random = new Random();
	private final RestTemplate restTemplate = new RestTemplate();

	@Data
    public static class TestScenario {
        private long delayMs = 0;
        private Boolean randomDelay = false; // 랜덤 지연
        private int failRate = 0; // 0~100
        private int apiCallCnt = 0; // 연계 api 호출 건 수 
        private int compositApiCallCnt = 1; // 동시 API 호출 건 수
        private Boolean randomCompositApiCall = false; // 랜덤 다건 API 호출 여부(최대 10회 이내)
        private long externalApiDelayMs = 0; // 외부 호출 전 지연 (옵션)
    }
    
    /**
	 * 통합 성능 테스트 API
	 * 
	 * @param delayMs    지연시간(ms), 기본값0 (지연 없슴)
	 * @param randomDelay    랜덤 지연 여부
	 * @param failRate   실패 확률(0~100), 기본 0
	 * @param apiCallCnt   연계 api 호출 건 수 
	 * @param compositApiCallCnt   동시 API 호출 건 수
	 * @param randomCompositApiCall   랜덤 다건 API 호출 여부(최대 10회 이내)
	 * @param externalApiDelayMs   실패 확률(0~100), 기본 0
	 * @return 지연 및 실패가 반영된 응답
	 * @throws InterruptedException
	 */
	@LogTransaction(svcName = "/scenario")
    @PostMapping("scenario")
    public ResponseEntity<String> scenario(@RequestBody TestScenario secnario) throws InterruptedException {
		//System.out.println("testWithScenario = " + scenario.toString());
		//LoggingContextUtil.initContext(LogUtil.getGlobalNo(), "1");
        return perfomance(secnario);
    }
	
	@LogTransaction(svcName = "/performance")
    @PostMapping("/performance")
    public ResponseEntity<String> performance(@RequestBody TestScenario scenario) throws InterruptedException {
		//System.out.println("testWithScenario = " + scenario.toString());
        return perfomance(scenario);
    }	

	private ResponseEntity<String> perfomance(TestScenario scenario) throws InterruptedException {
		// 1. 내부 지연
        if (scenario.getDelayMs() > 0) {
            Thread.sleep(scenario.getDelayMs());
        }

        // 2. 실패 확률 체크
        boolean fail = scenario.getFailRate() > 0 && random.nextInt(100) < scenario.getFailRate();
        if (fail) {
            throw new RuntimeException("의도적 예외 발생");
        }

        // 3. 외부 API 호출 (지연 포함)
        String externalApiResult = "";
        String externalApiUrl = "http://localhost:8080/performance";
        
        if(scenario.getRandomCompositApiCall()) {
        	int apiCallCnt = 1 + random.nextInt(9);
        	//System.out.println("[apiCallCnt] : " + apiCallCnt);
        	scenario.setCompositApiCallCnt( apiCallCnt);
        }
        
        if (scenario.getApiCallCnt() > 0 && scenario.getCompositApiCallCnt() > 0) {
        	scenario.setApiCallCnt(scenario.getApiCallCnt()-1);
        	for (int i = 0; i < scenario.getCompositApiCallCnt(); i++) {
	        	try {
	            	
	                if (scenario.getExternalApiDelayMs() > 0) {
	                	if(scenario.getRandomDelay()) {
	                		Thread.sleep(scenario.getExternalApiDelayMs() + random.nextInt(3000));
	                	}
	                    Thread.sleep(scenario.getExternalApiDelayMs());
	                }
	                HttpEntity<Object> entity = RestTemplateUtil.buildRequestWithTracingHeaders(scenario, i+1);
	                ResponseEntity<String> responseEntity = restTemplate.exchange(externalApiUrl, HttpMethod.POST, entity, String.class);
	                externalApiResult = responseEntity.getBody();
	
	            } catch (RestClientException | InterruptedException e) {
	                externalApiResult += ", 외부 API 호출 실패: " + e.getMessage();
	            }
	        	//System.out.println(externalApiResult);
	        	
        	}
        }

        // 4. 최종 응답 반환
        String response = "내부 지연시간: " + scenario.getDelayMs() + "ms, 실패 없음";
        if (!externalApiResult.isEmpty()) {
            response += ", 외부 API 결과: " + externalApiResult;
        }
        //System.out.println(response);
        return ResponseEntity.ok(response);
	}	
	
}
