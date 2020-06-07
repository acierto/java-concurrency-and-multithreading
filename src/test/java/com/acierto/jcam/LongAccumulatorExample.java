package com.acierto.jcam;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LongAccumulatorExample {
    private static final long TEST_TIME_SECONDS = 100;

    private final LongAccumulator accumulator = new LongAccumulator(Long::sum, 0L);
    private final ExecutorService executorService = Executors.newFixedThreadPool(8);

    @Test
    public void test() throws InterruptedException {

        int numberOfThreads = 4;
        int numberOfIncrements = 100;

        Runnable accumulateAction = () -> IntStream
                .rangeClosed(0, numberOfIncrements)
                .forEach(accumulator::accumulate);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.execute(accumulateAction);
        }

        executorService.shutdown();
        executorService.awaitTermination(TEST_TIME_SECONDS, TimeUnit.SECONDS);

        assertTrue(executorService.isTerminated());
        assertEquals(20200, accumulator.get());
    }
}
