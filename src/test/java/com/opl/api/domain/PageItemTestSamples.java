package com.opl.api.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PageItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PageItem getPageItemSample1() {
        return new PageItem()
            .id(1L)
            .title("title1")
            .authors("authors1")
            .menu("menu1")
            .menuWeight("menuWeight1")
            .publishedBy("publishedBy1")
            .approvedBy("approvedBy1")
            .jumbotronAltText("jumbotronAltText1");
    }

    public static PageItem getPageItemSample2() {
        return new PageItem()
            .id(2L)
            .title("title2")
            .authors("authors2")
            .menu("menu2")
            .menuWeight("menuWeight2")
            .publishedBy("publishedBy2")
            .approvedBy("approvedBy2")
            .jumbotronAltText("jumbotronAltText2");
    }

    public static PageItem getPageItemRandomSampleGenerator() {
        return new PageItem()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .authors(UUID.randomUUID().toString())
            .menu(UUID.randomUUID().toString())
            .menuWeight(UUID.randomUUID().toString())
            .publishedBy(UUID.randomUUID().toString())
            .approvedBy(UUID.randomUUID().toString())
            .jumbotronAltText(UUID.randomUUID().toString());
    }
}
