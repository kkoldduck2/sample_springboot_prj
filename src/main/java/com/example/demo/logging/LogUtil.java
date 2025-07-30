package com.example.demo.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.logstash.logback.argument.StructuredArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class LogUtil {
    private static final Logger logger = LoggerFactory.getLogger("StructuredLogger");
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void logTransaction(String svcName, String trFlag, String responseType, Object message) {
        Map<String, Object> header = new HashMap<>();
        header.put("appName", "NBSS_TORD");
        header.put("svcName", svcName);
        header.put("globalNo", LoggingContextUtil.getGlobalNo());
        header.put("responseSystem", "my-spring-service");
        header.put("lgDateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        header.put("trDate", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        header.put("trTime", LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmssSSS")));
        header.put("trFlag", trFlag);
        header.put("chnlType", "UI");


        if (responseType != null) {
            header.put("responseType", responseType);
        }

        monitoring(header, message);
    }

    public static void monitoring(Map<String, Object> header, Object message) {
        try {
            logger.info("structured-log",
                    StructuredArguments.keyValue("HEADER", header),
                    StructuredArguments.keyValue("MESSAGE", toJson(message))
            );
        } catch (Exception e) {
            logger.error("LogUtil.monitoring error", e);
        }
    }

    private static String toJson(Object obj) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
}

