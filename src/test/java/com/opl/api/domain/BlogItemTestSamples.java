package com.opl.api.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BlogItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BlogItem getBlogItemSample1() {
        return new BlogItem()
            .id(1L)
            .title("title1")
            .subtitle("subtitle1")
            .publishedBy("publishedBy1")
            .approvedBy("approvedBy1")
            .authors("authors1")
            .jumbotronAltText("jumbotronAltText1");
    }

    public static BlogItem getBlogItemSample2() {
        return new BlogItem()
            .id(2L)
            .title("title2")
            .subtitle("subtitle2")
            .publishedBy("publishedBy2")
            .approvedBy("approvedBy2")
            .authors("authors2")
            .jumbotronAltText("jumbotronAltText2");
    }

    public static BlogItem getBlogItemRandomSampleGenerator() {
        return new BlogItem()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .subtitle(UUID.randomUUID().toString())
            .publishedBy(UUID.randomUUID().toString())
            .approvedBy(UUID.randomUUID().toString())
            .authors(UUID.randomUUID().toString())
            .jumbotronAltText(UUID.randomUUID().toString());
    }
}
