package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MyFileTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MyFile getMyFileSample1() {
        return new MyFile().id(1L).name("name1");
    }

    public static MyFile getMyFileSample2() {
        return new MyFile().id(2L).name("name2");
    }

    public static MyFile getMyFileRandomSampleGenerator() {
        return new MyFile().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
