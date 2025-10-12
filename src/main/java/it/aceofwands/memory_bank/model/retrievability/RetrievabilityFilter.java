package it.aceofwands.memory_bank.model.retrievability;

public interface RetrievabilityFilter {
    boolean accept(long repetitionCount, long lastAccessTimestamp);
}
