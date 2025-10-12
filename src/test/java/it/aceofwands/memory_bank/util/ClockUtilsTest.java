package it.aceofwands.memory_bank.util;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class ClockUtilsTest {
    @Test
    void testGetCurrentTimestampSeconds_Mocked() {
        Instant fakeInstant = Instant.parse("2025-01-01T12:00:00Z");

        try (MockedStatic<Instant> mocked = mockStatic(Instant.class)) {
            mocked.when(Instant::now).thenReturn(fakeInstant);

            long result = ClockUtils.getCurrentTimestampSeconds();

            assertEquals(
                1735732800,
                result,
                "Returned epoch second should match mocked instant"
            );
        }
    }

    @Test
    void testGetCurrentDateTimeString_Mocked() {
        Instant fakeInstant = Instant.parse("2025-01-01T12:00:00Z");

        try (MockedStatic<Instant> mocked = mockStatic(Instant.class)) {
            mocked.when(Instant::now).thenReturn(fakeInstant);

            String result = ClockUtils.getCurrentDateTimeString();

            assertEquals(
                "2025-01-01T12:00:00Z",
                result,
                "Returned string should match mocked instant’s toString()"
            );
        }
    }

}