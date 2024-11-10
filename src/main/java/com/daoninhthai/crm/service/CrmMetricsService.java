package com.daoninhthai.crm.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.Callable;

@Service
@RequiredArgsConstructor
public class CrmMetricsService {

    private final Counter contactsCreatedCounter;
    private final Counter dealsCreatedCounter;
    private final DistributionSummary dealsValueSummary;
    private final Timer apiRequestDurationTimer;

    public void recordContactCreated() {
        contactsCreatedCounter.increment();
    }

    public void recordDealCreated() {
        dealsCreatedCounter.increment();
    }

    public void recordDealClosed(BigDecimal value) {
        if (value != null) {
            dealsValueSummary.record(value.doubleValue());
        }
    }

    public <T> T recordApiCall(Callable<T> callable) throws Exception {
        return apiRequestDurationTimer.recordCallable(callable);
    }

    public void recordApiCall(Runnable runnable) {
        apiRequestDurationTimer.record(runnable);
    }
}
