package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TestsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Tests getTestsSample1() {
        return new Tests().id(1L).name("name1").questions("questions1");
    }

    public static Tests getTestsSample2() {
        return new Tests().id(2L).name("name2").questions("questions2");
    }

    public static Tests getTestsRandomSampleGenerator() {
        return new Tests().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).questions(UUID.randomUUID().toString());
    }
}
