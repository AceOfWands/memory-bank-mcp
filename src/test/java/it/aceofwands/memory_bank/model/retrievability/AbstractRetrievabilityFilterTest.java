package it.aceofwands.memory_bank.model.retrievability;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbstractRetrievabilityFilterTest {
    @Mock
    RetrievabilityFilterConfiguration configuration;

    private static class TextClass extends AbstractRetrievabilityFilter<RetrievabilityFilterConfiguration> {
        public TextClass(RetrievabilityFilterConfiguration configuration) {
            super(configuration);
        }
    }

    @Test
    void calculateRetrievabilityReturnsTargetRecallProbabilityValueAfterLengthOfFirstIntervalSecondsPassedAnd1Repetition() {
        Instant fakeInstant = Instant.parse("2025-01-01T12:00:00Z");

        try (MockedStatic<Instant> mocked = mockStatic(Instant.class)) {
            mocked.when(Instant::now).thenReturn(fakeInstant);
            AbstractRetrievabilityFilter<RetrievabilityFilterConfiguration> filter = new TextClass(configuration);
            when(configuration.getLengthOfFirstInterval()).thenReturn(60 * 60 * 24 * 7.0);
            when(configuration.getRelearningRate()).thenReturn(1.2);
            when(configuration.getTargetRecallProbability()).thenReturn(0.95);
            double retrievability = filter.calculateRetrievability(
                1,
                fakeInstant.getEpochSecond() - 60 * 60 * 24 * 7
            );
            assertEquals(0.95, retrievability);
        }
    }

    @Test
    void calculateRetrievabilityReturnsTargetRecallProbabilityValueAfterLengthOfFirstIntervalSecondsPassedAnd2Repetitions() {
        Instant fakeInstant = Instant.parse("2025-01-01T12:00:00Z");

        try (MockedStatic<Instant> mocked = mockStatic(Instant.class)) {
            mocked.when(Instant::now).thenReturn(fakeInstant);
            AbstractRetrievabilityFilter<RetrievabilityFilterConfiguration> filter = new TextClass(configuration);
            when(configuration.getLengthOfFirstInterval()).thenReturn(60 * 60 * 24 * 7.0);
            when(configuration.getRelearningRate()).thenReturn(1.2);
            when(configuration.getTargetRecallProbability()).thenReturn(0.95);
            double retrievability = filter.calculateRetrievability(
                2,
                fakeInstant.getEpochSecond() - 60 * 60 * 24 * 7
            );
            assertTrue(0.95 < retrievability);
        }
    }

    @Test
    void calculateRetrievabilityCompareAfterRepetitionAndSameTimeLength() {
        Instant fakeInstant = Instant.parse("2025-01-01T12:00:00Z");

        try (MockedStatic<Instant> mocked = mockStatic(Instant.class)) {
            mocked.when(Instant::now).thenReturn(fakeInstant);
            AbstractRetrievabilityFilter<RetrievabilityFilterConfiguration> filter = new TextClass(configuration);
            when(configuration.getLengthOfFirstInterval()).thenReturn(60 * 60 * 24 * 7.0);
            when(configuration.getRelearningRate()).thenReturn(1.2);
            when(configuration.getTargetRecallProbability()).thenReturn(0.95);
            double retrievability1 = filter.calculateRetrievability(
                12,
                fakeInstant.getEpochSecond() - 60 * 60 * 24 * 5
            );
            double retrievability2 = filter.calculateRetrievability(
                13,
                fakeInstant.getEpochSecond() - 60 * 60 * 24 * 5
            );
            assertTrue(retrievability1 < retrievability2);
        }
    }
}