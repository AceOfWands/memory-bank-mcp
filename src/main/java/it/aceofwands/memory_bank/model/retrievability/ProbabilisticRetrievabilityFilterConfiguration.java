package it.aceofwands.memory_bank.model.retrievability;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Random;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProbabilisticRetrievabilityFilterConfiguration extends RetrievabilityFilterConfiguration{
    private Random prng = new Random();
}
