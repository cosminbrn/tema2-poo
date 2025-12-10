package main.globals;

import lombok.Getter;

public enum StabilityMetrics {
    RARE_FREQUENCY(1),
    OCCASIONAL_FREQUENCY(2),
    FREQUENT_FREQUENCY(3),
    ALWAYS_FREQUENCY(4),

    LOW_BUSINESS_PRIORITY(1),
    MEDIUM_BUSINESS_PRIORITY(2),
    HIGH_BUSINESS_PRIORITY(3),
    CRITICAL_BUSINESS_PRIORITY(4),

    MINOR_SEVERITY_FACTOR(1),
    MODERATE_SEVERITY_FACTOR(2),
    MAJOR_SEVERITY_FACTOR(3),

    S_BUSINESS_VALUE(1),
    M_BUSINESS_VALUE(3),
    L_BUSINESS_VALUE(6),
    XL_BUSINESS_VALUE(10),

    LOW_CUSTOMER_DEMAND(1),
    MEDIUM_CUSTOMER_DEMAND(3),
    HIGH_CUSTOMER_DEMAND(6),
    VERY_HIGH_CUSTOMER_DEMAND(10);

    @Getter
    private final int value;


    StabilityMetrics(int value) {
        this.value = value;
    }

}
