package it.aceofwands.memory_bank.model.retrievability;

public class ThresholdRetrievabilityFilter
        extends AbstractRetrievabilityFilter<ThresholdRetrievabilityFilterConfiguration>
{
    public ThresholdRetrievabilityFilter(ThresholdRetrievabilityFilterConfiguration configuration) {
        super(configuration);
    }

    @Override
    public boolean accept(double retrievabilityFactor) {
        return super.accept(retrievabilityFactor) &&
                this.isOverThreshold(retrievabilityFactor);
    }

    private boolean isOverThreshold(double retrievabilityFactor) {
        ThresholdRetrievabilityFilterConfiguration config = this.getConfiguration();
        return retrievabilityFactor >= config.getThreshold();
    }
}
