package com.opl.api.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class LinkItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static LinkItem getLinkItemSample1() {
        return new LinkItem().id(1L).url("url1").sortIndex(1);
    }

    public static LinkItem getLinkItemSample2() {
        return new LinkItem().id(2L).url("url2").sortIndex(2);
    }

    public static LinkItem getLinkItemRandomSampleGenerator() {
        return new LinkItem().id(longCount.incrementAndGet()).url(UUID.randomUUID().toString()).sortIndex(intCount.incrementAndGet());
    }
}
