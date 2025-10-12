package it.aceofwands.memory_bank.util;

import java.time.Instant;

public class ClockUtils {
    public static long getCurrentTimestampSeconds() {
        return Instant.now().getEpochSecond();
    }

    public static String getCurrentDateTimeString() {
        return Instant.now().toString();
    }
}
