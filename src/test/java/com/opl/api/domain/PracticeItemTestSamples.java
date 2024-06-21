package com.opl.api.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PracticeItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PracticeItem getPracticeItemSample1() {
        return new PracticeItem()
            .id(1L)
            .title("title1")
            .objective("objective1")
            .publishedBy("publishedBy1")
            .approvedBy("approvedBy1")
            .authors("authors1")
            .numberOfPeopleRequired("numberOfPeopleRequired1")
            .timeLength("timeLength1")
            .expectedParticipants("expectedParticipants1");
    }

    public static PracticeItem getPracticeItemSample2() {
        return new PracticeItem()
            .id(2L)
            .title("title2")
            .objective("objective2")
            .publishedBy("publishedBy2")
            .approvedBy("approvedBy2")
            .authors("authors2")
            .numberOfPeopleRequired("numberOfPeopleRequired2")
            .timeLength("timeLength2")
            .expectedParticipants("expectedParticipants2");
    }

    public static PracticeItem getPracticeItemRandomSampleGenerator() {
        return new PracticeItem()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .objective(UUID.randomUUID().toString())
            .publishedBy(UUID.randomUUID().toString())
            .approvedBy(UUID.randomUUID().toString())
            .authors(UUID.randomUUID().toString())
            .numberOfPeopleRequired(UUID.randomUUID().toString())
            .timeLength(UUID.randomUUID().toString())
            .expectedParticipants(UUID.randomUUID().toString());
    }
}
