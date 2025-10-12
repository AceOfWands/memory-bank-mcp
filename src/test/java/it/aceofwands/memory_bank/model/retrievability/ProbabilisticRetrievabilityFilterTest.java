package it.aceofwands.memory_bank.model.retrievability;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class ProbabilisticRetrievabilityFilterTest {
    @Mock
    private Random randomMock;

    @Test
    void acceptReturnsFalseIfFilterIsNotEnabledButOkRandom() {
        ProbabilisticRetrievabilityFilterConfiguration configuration = new ProbabilisticRetrievabilityFilterConfiguration();
        configuration.setPrng(randomMock);
        configuration.setEnabled(false);
        ProbabilisticRetrievabilityFilter filter = new ProbabilisticRetrievabilityFilter(configuration);
        assertFalse(filter.accept(0.5));
    }

    @Test
    void acceptReturnsTrueIfFilterIsEnabledAndOkRandom() {
        ProbabilisticRetrievabilityFilterConfiguration configuration = new ProbabilisticRetrievabilityFilterConfiguration();
        configuration.setPrng(randomMock);
        Mockito.when(randomMock.nextDouble()).thenReturn(0.25);
        configuration.setEnabled(true);
        ProbabilisticRetrievabilityFilter filter = new ProbabilisticRetrievabilityFilter(configuration);
        assertTrue(filter.accept(0.5));
    }

    @Test
    void acceptReturnsFalseIfFilterIsEnabledButKoRandom() {
        ProbabilisticRetrievabilityFilterConfiguration configuration = new ProbabilisticRetrievabilityFilterConfiguration();
        configuration.setPrng(randomMock);
        Mockito.when(randomMock.nextDouble()).thenReturn(0.75);
        configuration.setEnabled(true);
        ProbabilisticRetrievabilityFilter filter = new ProbabilisticRetrievabilityFilter(configuration);
        assertFalse(filter.accept(0.5));
    }

    @Test
    void acceptWith1RepetitionAndSecondsFromNowReturnsFalseIfFilterIsNotEnabledButOkRandom() {
        Instant fakeInstant = Instant.parse("2025-01-01T12:00:00Z");

        try (MockedStatic<Instant> mocked = mockStatic(Instant.class)) {
            mocked.when(Instant::now).thenReturn(fakeInstant);
            ProbabilisticRetrievabilityFilterConfiguration configuration = new ProbabilisticRetrievabilityFilterConfiguration();
            configuration.setPrng(randomMock);
            configuration.setEnabled(false);
            ProbabilisticRetrievabilityFilter filter = new ProbabilisticRetrievabilityFilter(configuration);
            assertFalse(filter.accept(1, fakeInstant.getEpochSecond() - 60 * 60));
        }
    }

    @Test
    void acceptWith1RepetitionAndSecondsFromNowReturnsTrueIfFilterIsEnabledAndOkRandom() {
        Instant fakeInstant = Instant.parse("2025-01-01T12:00:00Z");

        try (MockedStatic<Instant> mocked = mockStatic(Instant.class)) {
            mocked.when(Instant::now).thenReturn(fakeInstant);
            ProbabilisticRetrievabilityFilterConfiguration configuration = new ProbabilisticRetrievabilityFilterConfiguration();
            configuration.setPrng(randomMock);
            Mockito.when(randomMock.nextDouble()).thenReturn(0.25);
            configuration.setEnabled(true);
            ProbabilisticRetrievabilityFilter filter = new ProbabilisticRetrievabilityFilter(configuration);
            assertTrue(filter.accept(1, fakeInstant.getEpochSecond() - 60 * 60));
        }
    }

    @Test
    void acceptWith1RepetitionAnd1YearAgoReturnsFalseIfFilterIsEnabledButKoRandom() {
        Instant fakeInstant = Instant.parse("2025-01-01T12:00:00Z");

        try (MockedStatic<Instant> mocked = mockStatic(Instant.class)) {
            mocked.when(Instant::now).thenReturn(fakeInstant);
            ProbabilisticRetrievabilityFilterConfiguration configuration = new ProbabilisticRetrievabilityFilterConfiguration();
            configuration.setPrng(randomMock);
            Mockito.when(randomMock.nextDouble()).thenReturn(0.75);
            configuration.setEnabled(true);
            ProbabilisticRetrievabilityFilter filter = new ProbabilisticRetrievabilityFilter(configuration);
            assertFalse(filter.accept(1, fakeInstant.getEpochSecond() - 365 * 60 * 60 * 24));
        }
    }

    @Test
    void acceptWith12RepetitionAndSecondsFromNowReturnsFalseIfFilterIsNotEnabledButOkRandom() {
        Instant fakeInstant = Instant.parse("2025-01-01T12:00:00Z");

        try (MockedStatic<Instant> mocked = mockStatic(Instant.class)) {
            mocked.when(Instant::now).thenReturn(fakeInstant);
            ProbabilisticRetrievabilityFilterConfiguration configuration = new ProbabilisticRetrievabilityFilterConfiguration();
            configuration.setPrng(randomMock);
            configuration.setEnabled(false);
            ProbabilisticRetrievabilityFilter filter = new ProbabilisticRetrievabilityFilter(configuration);
            assertFalse(filter.accept(12, fakeInstant.getEpochSecond() - 60 * 60));
        }
    }

    @Test
    void acceptWith12RepetitionAndSecondsFromNowReturnsTrueIfFilterIsEnabledAndOkRandom() {
        Instant fakeInstant = Instant.parse("2025-01-01T12:00:00Z");

        try (MockedStatic<Instant> mocked = mockStatic(Instant.class)) {
            mocked.when(Instant::now).thenReturn(fakeInstant);
            ProbabilisticRetrievabilityFilterConfiguration configuration = new ProbabilisticRetrievabilityFilterConfiguration();
            configuration.setPrng(randomMock);
            Mockito.when(randomMock.nextDouble()).thenReturn(0.25);
            configuration.setEnabled(true);
            ProbabilisticRetrievabilityFilter filter = new ProbabilisticRetrievabilityFilter(configuration);
            assertTrue(filter.accept(12, fakeInstant.getEpochSecond() - 60 * 60));
        }
    }

    @Test
    void acceptWith9RepetitionAnd5YearsAgoReturnsFalseIfFilterIsEnabledButKoRandom() {
        Instant fakeInstant = Instant.parse("2025-01-01T12:00:00Z");

        try (MockedStatic<Instant> mocked = mockStatic(Instant.class)) {
            mocked.when(Instant::now).thenReturn(fakeInstant);
            ProbabilisticRetrievabilityFilterConfiguration configuration = new ProbabilisticRetrievabilityFilterConfiguration();
            configuration.setPrng(randomMock);
            Mockito.when(randomMock.nextDouble()).thenReturn(0.75);
            configuration.setEnabled(true);
            ProbabilisticRetrievabilityFilter filter = new ProbabilisticRetrievabilityFilter(configuration);
            assertFalse(filter.accept(9, fakeInstant.getEpochSecond() - 5 * 365 * 60 * 60 * 24));
        }
    }
}