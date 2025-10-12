package it.aceofwands.memory_bank.configuration;

import it.aceofwands.memory_bank.model.retrievability.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
public class RetrievabilityFilterInitConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "retrievability-filter.settings")
    @ConditionalOnProperty(name = "retrievability-filter.type", havingValue = "threshold", matchIfMissing = true)
    public ThresholdRetrievabilityFilterConfiguration thresholdFilterConfiguration() {
        return new ThresholdRetrievabilityFilterConfiguration();
    }

    @Bean
    @ConfigurationProperties(prefix = "retrievability-filter.settings")
    @ConditionalOnProperty(name = "retrievability-filter.type", havingValue = "probabilistic")
    public ProbabilisticRetrievabilityFilterConfiguration probabilisticFilterConfiguration() {
        return new ProbabilisticRetrievabilityFilterConfiguration();
    }

    @Bean
    @ConditionalOnBean(ProbabilisticRetrievabilityFilterConfiguration.class)
    public RetrievabilityFilter initProbabilisticRetrievabilityFilter(
            ProbabilisticRetrievabilityFilterConfiguration configuration
    ) {
        return new ProbabilisticRetrievabilityFilter(configuration);
    }

    @Bean
    @ConditionalOnBean(ThresholdRetrievabilityFilterConfiguration.class)
    public RetrievabilityFilter initThresholdRetrievabilityFilter(
            ThresholdRetrievabilityFilterConfiguration configuration
    ) {
        return new ThresholdRetrievabilityFilter(configuration);
    }
}
