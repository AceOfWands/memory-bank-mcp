package it.aceofwands.memory_bank.model.retrievability;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@RequiredArgsConstructor
public abstract class AbstractRetrievabilityFilter<T extends RetrievabilityFilterConfiguration>
        implements RetrievabilityFilter
{
    @Getter
    private final T configuration;

    @Override
    public boolean accept(long repetitionCount, long lastAccessTimestamp) {
        return this.accept(this.calculateRetrievability(repetitionCount, lastAccessTimestamp));
    }

    protected boolean accept(double retrievabilityFactor) {
        return isEnabled();
    }

    protected boolean isEnabled() {
        return configuration.getEnabled();
    }

    protected double calculateRetrievability(long repetitionCount, long lastAccessTimestamp) {
        // https://polona2.pl/item/two-components-of-long-term-memory,MTE2MDU1MDU/1/#info:metadata
        long t = Instant.now().getEpochSecond() - lastAccessTimestamp;
        double k = configuration.getTargetRecallProbability();
        double c1 = configuration.getLengthOfFirstInterval();
        double c2 = configuration.getRelearningRate();
        return Math.exp(t * Math.log(k) / (c1 * Math.pow(c2, repetitionCount - 1)));
    }
}
