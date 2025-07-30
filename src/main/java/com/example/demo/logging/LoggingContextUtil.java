package com.example.demo.logging;

import org.slf4j.MDC;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class LoggingContextUtil {

    public static final String MDC_GLOBAL_NO = "GLOBAL_NO";
    public static final String MDC_TOPOLOGY_SEQ = "TOPOLOGY_SEQ";
    public static final String CATEGORY = "CATEGORY";

    public static void initContext(String globalNo, String topologySeq) {
        MDC.put(MDC_GLOBAL_NO, globalNo);
        MDC.put(MDC_TOPOLOGY_SEQ, topologySeq);
        MDC.put(CATEGORY, "MON");
    }

    public static void clear() {
        MDC.clear();
    }

    public static String getGlobalNo() {
        return MDC.get(MDC_GLOBAL_NO);
    }

    public static String getTopologySeq() {
        return MDC.get(MDC_TOPOLOGY_SEQ);
    }

    public static String nextTopologySeq(int nextIndex) {
        String current = getTopologySeq();
        if (current == null || current.isEmpty()) return String.valueOf(nextIndex);
        return current + "|" + nextIndex;
    }
}
