package it.aceofwands.memory_bank.model.retrievability;

import java.util.Random;

public class ProbabilisticRetrievabilityFilter
        extends AbstractRetrievabilityFilter<ProbabilisticRetrievabilityFilterConfiguration>
{
    public ProbabilisticRetrievabilityFilter(ProbabilisticRetrievabilityFilterConfiguration configuration) {
        super(configuration);
    }

    @Override
    public boolean accept(double retrievabilityFactor) {
        return super.accept(retrievabilityFactor) &&
                this.isAcceptedByProbability(retrievabilityFactor);
    }

    private boolean isAcceptedByProbability(double retrievabilityFactor) {
        var configuration = this.getConfiguration();
        Random prng = configuration.getPrng();
        return prng.nextDouble() < retrievabilityFactor;
    }
}
