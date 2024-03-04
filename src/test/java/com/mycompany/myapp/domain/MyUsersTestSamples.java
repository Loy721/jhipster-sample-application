package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MyUsersTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MyUsers getMyUsersSample1() {
        return new MyUsers().id(1L).name("name1").surname("surname1");
    }

    public static MyUsers getMyUsersSample2() {
        return new MyUsers().id(2L).name("name2").surname("surname2");
    }

    public static MyUsers getMyUsersRandomSampleGenerator() {
        return new MyUsers().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).surname(UUID.randomUUID().toString());
    }
}
