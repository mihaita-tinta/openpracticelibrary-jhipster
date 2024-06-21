package com.opl.api.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AuthorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Author getAuthorSample1() {
        return new Author()
            .id(1L)
            .name("name1")
            .githubUsername("githubUsername1")
            .location("location1")
            .website("website1")
            .publishedBy("publishedBy1")
            .approvedBy("approvedBy1");
    }

    public static Author getAuthorSample2() {
        return new Author()
            .id(2L)
            .name("name2")
            .githubUsername("githubUsername2")
            .location("location2")
            .website("website2")
            .publishedBy("publishedBy2")
            .approvedBy("approvedBy2");
    }

    public static Author getAuthorRandomSampleGenerator() {
        return new Author()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .githubUsername(UUID.randomUUID().toString())
            .location(UUID.randomUUID().toString())
            .website(UUID.randomUUID().toString())
            .publishedBy(UUID.randomUUID().toString())
            .approvedBy(UUID.randomUUID().toString());
    }
}
