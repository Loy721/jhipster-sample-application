package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class UserTestTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static UserTest getUserTestSample1() {
        return new UserTest().id(1L).grade(1);
    }

    public static UserTest getUserTestSample2() {
        return new UserTest().id(2L).grade(2);
    }

    public static UserTest getUserTestRandomSampleGenerator() {
        return new UserTest().id(longCount.incrementAndGet()).grade(intCount.incrementAndGet());
    }
}
