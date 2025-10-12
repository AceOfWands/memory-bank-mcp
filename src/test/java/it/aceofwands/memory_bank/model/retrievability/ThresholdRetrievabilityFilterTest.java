package it.aceofwands.memory_bank.model.retrievability;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class ThresholdRetrievabilityFilterTest {

    @Test
    void acceptReturnsFalseIfFilterIsNotEnabledButValueIsOverThreshold() {
        ThresholdRetrievabilityFilterConfiguration configuration = new ThresholdRetrievabilityFilterConfiguration();
        configuration.setThreshold(0.1);
        configuration.setEnabled(false);
        ThresholdRetrievabilityFilter filter = new ThresholdRetrievabilityFilter(configuration);
        assertFalse(filter.accept(0.5));
    }

    @Test
    void acceptReturnsTrueIfFilterIsEnabledAndValueIsOverThreshold() {
        ThresholdRetrievabilityFilterConfiguration configuration = new ThresholdRetrievabilityFilterConfiguration();
        configuration.setThreshold(0.4);
        configuration.setEnabled(true);
        ThresholdRetrievabilityFilter filter = new ThresholdRetrievabilityFilter(configuration);
        assertTrue(filter.accept(0.5));
    }

    @Test
    void acceptReturnsFalseIfFilterIsEnabledAndValueIsUnderThreshold() {
        ThresholdRetrievabilityFilterConfiguration configuration = new ThresholdRetrievabilityFilterConfiguration();
        configuration.setThreshold(0.7);
        configuration.setEnabled(true);
        ThresholdRetrievabilityFilter filter = new ThresholdRetrievabilityFilter(configuration);
        assertFalse(filter.accept(0.5));
    }

    @Test
    void acceptWith1RepetitionAndSecondsByNowReturnsFalseIfFilterIsNotEnabledButValueIsOverThreshold() {
        Instant fakeInstant = Instant.parse("2025-01-01T12:00:00Z");

        try (MockedStatic<Instant> mocked = mockStatic(Instant.class)) {
            mocked.when(Instant::now).thenReturn(fakeInstant);
            ThresholdRetrievabilityFilterConfiguration configuration = new ThresholdRetrievabilityFilterConfiguration();
            configuration.setThreshold(0.1);
            configuration.setEnabled(false);
            ThresholdRetrievabilityFilter filter = new ThresholdRetrievabilityFilter(configuration);
            assertFalse(filter.accept(1, fakeInstant.getEpochSecond() - 60 * 60));
        }
    }

    @Test
    void acceptWith1RepetitionAndSecondsByNowReturnsTrueIfFilterIsEnabledAndValueIsOverThreshold() {
        Instant fakeInstant = Instant.parse("2025-01-01T12:00:00Z");

        try (MockedStatic<Instant> mocked = mockStatic(Instant.class)) {
            mocked.when(Instant::now).thenReturn(fakeInstant);
            ThresholdRetrievabilityFilterConfiguration configuration = new ThresholdRetrievabilityFilterConfiguration();
            configuration.setThreshold(0.4);
            configuration.setEnabled(true);
            ThresholdRetrievabilityFilter filter = new ThresholdRetrievabilityFilter(configuration);
            assertTrue(filter.accept(1, fakeInstant.getEpochSecond() - 60 * 60));
        }
    }

    @Test
    void acceptWith1RepetitionAnd1YearAgoReturnsFalseIfFilterIsEnabledAndValueIsUnderThreshold() {
        Instant fakeInstant = Instant.parse("2025-01-01T12:00:00Z");

        try (MockedStatic<Instant> mocked = mockStatic(Instant.class)) {
            mocked.when(Instant::now).thenReturn(fakeInstant);
            ThresholdRetrievabilityFilterConfiguration configuration = new ThresholdRetrievabilityFilterConfiguration();
            configuration.setThreshold(0.7);
            configuration.setEnabled(true);
            ThresholdRetrievabilityFilter filter = new ThresholdRetrievabilityFilter(configuration);
            assertFalse(filter.accept(1, fakeInstant.getEpochSecond() - 60 * 60 * 24 * 365));
        }
    }

    @Test
    void acceptWith12RepetitionAndSecondsByNowReturnsFalseIfFilterIsNotEnabledButValueIsOverThreshold() {
        Instant fakeInstant = Instant.parse("2025-01-01T12:00:00Z");

        try (MockedStatic<Instant> mocked = mockStatic(Instant.class)) {
            mocked.when(Instant::now).thenReturn(fakeInstant);
            ThresholdRetrievabilityFilterConfiguration configuration = new ThresholdRetrievabilityFilterConfiguration();
            configuration.setThreshold(0.1);
            configuration.setEnabled(false);
            ThresholdRetrievabilityFilter filter = new ThresholdRetrievabilityFilter(configuration);
            assertFalse(filter.accept(12, fakeInstant.getEpochSecond() - 60 * 60));
        }
    }

    @Test
    void acceptWith12RepetitionAndSecondsByNowReturnsTrueIfFilterIsEnabledAndValueIsOverThreshold() {
        Instant fakeInstant = Instant.parse("2025-01-01T12:00:00Z");

        try (MockedStatic<Instant> mocked = mockStatic(Instant.class)) {
            mocked.when(Instant::now).thenReturn(fakeInstant);
        ThresholdRetrievabilityFilterConfiguration configuration = new ThresholdRetrievabilityFilterConfiguration();
        configuration.setThreshold(0.4);
        configuration.setEnabled(true);
        ThresholdRetrievabilityFilter filter = new ThresholdRetrievabilityFilter(configuration);
        assertTrue(filter.accept(12, fakeInstant.getEpochSecond() - 60*60));
        }
    }

    @Test
    void acceptWith9RepetitionAnd5YearsAgoReturnsFalseIfFilterIsEnabledAndValueIsUnderThreshold() {
        Instant fakeInstant = Instant.parse("2025-01-01T12:00:00Z");

        try (MockedStatic<Instant> mocked = mockStatic(Instant.class)) {
            mocked.when(Instant::now).thenReturn(fakeInstant);
            ThresholdRetrievabilityFilterConfiguration configuration = new ThresholdRetrievabilityFilterConfiguration();
            configuration.setThreshold(0.7);
            configuration.setRelearningRate(1.01);
            configuration.setEnabled(true);
            ThresholdRetrievabilityFilter filter = new ThresholdRetrievabilityFilter(configuration);
            assertFalse(filter.accept(9, fakeInstant.getEpochSecond() - 60 * 60 * 24 * 365 * 5));
        }
    }
}