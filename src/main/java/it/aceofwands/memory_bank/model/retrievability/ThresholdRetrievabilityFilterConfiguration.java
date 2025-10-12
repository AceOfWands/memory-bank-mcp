package it.aceofwands.memory_bank.model.retrievability;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ThresholdRetrievabilityFilterConfiguration extends RetrievabilityFilterConfiguration {
    private double threshold = 0.0;
}
