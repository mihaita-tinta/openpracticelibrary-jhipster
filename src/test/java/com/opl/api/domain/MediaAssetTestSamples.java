package com.opl.api.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MediaAssetTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static MediaAsset getMediaAssetSample1() {
        return new MediaAsset().id(1L).sortIndex(1);
    }

    public static MediaAsset getMediaAssetSample2() {
        return new MediaAsset().id(2L).sortIndex(2);
    }

    public static MediaAsset getMediaAssetRandomSampleGenerator() {
        return new MediaAsset().id(longCount.incrementAndGet()).sortIndex(intCount.incrementAndGet());
    }
}
