package com.daoninhthai.crm.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public Counter contactsCreatedCounter(MeterRegistry registry) {
        return Counter.builder("crm_contacts_total")
                .description("Total number of contacts created")
                .tag("action", "created")
                .register(registry);
    }

    @Bean
    public Counter dealsCreatedCounter(MeterRegistry registry) {
        return Counter.builder("crm_deals_total")
                .description("Total number of deals created")
                .tag("action", "created")
                .register(registry);
    }

    @Bean
    public DistributionSummary dealsValueSummary(MeterRegistry registry) {
        return DistributionSummary.builder("crm_deals_value_total")
                .description("Distribution of deal values when closed")
                .baseUnit("currency")
                .register(registry);
    }

    @Bean
    public Timer apiRequestDurationTimer(MeterRegistry registry) {
        return Timer.builder("crm_api_request_duration")
                .description("Duration of CRM API requests")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(registry);
    }
}
