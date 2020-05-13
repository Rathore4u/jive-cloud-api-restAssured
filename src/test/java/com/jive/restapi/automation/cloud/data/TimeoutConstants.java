package com.jive.restapi.automation.cloud.data;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TimeoutConstants {
    public final int XS = 2000;
    public final int S = 4000;
    public final int M  = 5000;
    public final int L = 10000;

    @UtilityClass
    public class Times {
        public final int ONE_HUNDRED_TIMES = 100;
        public final int TWO_HUNDRED_TIMES = 200;
        public final int ONE_THOUSAND_TIMES = 1000;
    }
}
