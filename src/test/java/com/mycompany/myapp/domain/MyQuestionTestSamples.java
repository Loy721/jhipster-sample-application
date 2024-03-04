package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MyQuestionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MyQuestion getMyQuestionSample1() {
        return new MyQuestion().id(1L).name("name1");
    }

    public static MyQuestion getMyQuestionSample2() {
        return new MyQuestion().id(2L).name("name2");
    }

    public static MyQuestion getMyQuestionRandomSampleGenerator() {
        return new MyQuestion().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
