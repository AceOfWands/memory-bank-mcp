package it.aceofwands.memory_bank.model.retrievability;

import lombok.Data;

@Data
public abstract class RetrievabilityFilterConfiguration {
    private Boolean enabled = true;
    private double targetRecallProbability = 0.95; // the fraction of knowledge you want to still be recallable at the end of an “optimum” repetition interval
    private double lengthOfFirstInterval = 7*24*60*60; // 7 days
    private double relearningRate = 1.5;
}
