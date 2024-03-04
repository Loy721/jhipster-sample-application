package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FilesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Files getFilesSample1() {
        return new Files().id(1L).topic("topic1").content("content1");
    }

    public static Files getFilesSample2() {
        return new Files().id(2L).topic("topic2").content("content2");
    }

    public static Files getFilesRandomSampleGenerator() {
        return new Files().id(longCount.incrementAndGet()).topic(UUID.randomUUID().toString()).content(UUID.randomUUID().toString());
    }
}
