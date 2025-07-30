package com.example.demo.logging;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

public class RestTemplateUtil {
    public static HttpEntity<Object> buildRequestWithTracingHeaders(Object body, int topologyStepIndex) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Global-No", LoggingContextUtil.getGlobalNo());
        headers.set("X-Topology-Seq", LoggingContextUtil.nextTopologySeq(topologyStepIndex));
        return new HttpEntity<>(body, headers);
    }

    public static HttpEntity<Object> buildEmptyRequestWithTracingHeaders(int topologyStepIndex) {
        return buildRequestWithTracingHeaders(null, topologyStepIndex);
    }
}
