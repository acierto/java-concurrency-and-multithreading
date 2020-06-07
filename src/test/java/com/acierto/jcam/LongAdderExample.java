package com.acierto.jcam;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LongAdderExample {

    private static final int CONCURRENCY = 10;
    private static final int CONN_ATTEMPTS = 10_000;
    private static final long TEST_TIME_SECONDS = 100;


    @Test
    public void testSum() throws InterruptedException {
        final LongAdder connCounter = new LongAdder();
        final ExecutorService executor = Executors.newFixedThreadPool(CONCURRENCY);
        for(int i = 0; i < CONCURRENCY; i ++) {
            executor.submit(
                    () -> {
                        for(int j = 0; j < CONN_ATTEMPTS; j ++) {
                            try {
                                connCounter.increment();
                            } catch(final Throwable cause) {
                                cause.printStackTrace(System.err);
                            }
                        }
                    }
            );
        }
        executor.shutdown();
        executor.awaitTermination(TEST_TIME_SECONDS, TimeUnit.SECONDS);
        assertTrue(executor.isTerminated());
        assertEquals(
                CONCURRENCY * CONN_ATTEMPTS, connCounter.sum(),
                20000
        );

    }
}
