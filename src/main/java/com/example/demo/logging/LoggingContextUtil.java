package com.example.demo.logging;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

public class LoggingContextUtil {

	public static final String MDC_GLOBAL_NO = "GLOBAL_NO";
	public static final String MDC_TOPOLOGY_SEQ = "TOPOLOGY_SEQ";
	public static final String MDC_CATEGORY = "CATEGORY";
	public static final String MDC_SERVICE = "SERVICE";

	public static void initContext(String globalNo, String topologySeq) {

		MDC.put(MDC_GLOBAL_NO, globalNo);
	
		MDC.put(MDC_TOPOLOGY_SEQ, topologySeq);
		MDC.put(MDC_CATEGORY, "MON");
		MDC.put(MDC_SERVICE, "my-spring-service");
	}

	public static void clear() {
		MDC.clear();
	}

	public static String getGlobalNo() {
		return MDC.get(MDC_GLOBAL_NO);
	}

	public static String getResponseSystem() {
		return MDC.get(MDC_SERVICE);
	}

	public static String getTopologySeq() {
		return MDC.get(MDC_TOPOLOGY_SEQ);
	}

	public static String nextTopologySeq(int nextIndex) {
		String current = getTopologySeq();
		if (current == null || current.isEmpty())
			return String.valueOf(nextIndex);
		return current + "|" + nextIndex;
	}
		
	public static String getHeaderValue(String headerName) {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attributes == null) {
			return null;
		}
		HttpServletRequest request = attributes.getRequest();
		return request.getHeader(headerName);
	}
}
