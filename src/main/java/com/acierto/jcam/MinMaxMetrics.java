package com.acierto.jcam;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class MinMaxMetrics {
    private volatile long max;
    private volatile long min;

    public MinMaxMetrics() {
        this.min = Integer.MAX_VALUE;
        this.max = Integer.MIN_VALUE;
    }

    public void addSample(long newSample) {
        synchronized (this) {
            this.min = Math.min(newSample, this.min);
            this.max = Math.max(newSample, this.max);
        }
    }

    public long getMin() {
        return min;
    }

    public long getMax() {
        return max;
    }

    public static void main(String[] args) {
        MinMaxMetrics metrics = new MinMaxMetrics();
        ForkJoinPool pool = new ForkJoinPool(2);
        pool.submit(() -> {
            for (int i = 0; i < 100000; i++) {
                metrics.addSample(new Random().nextInt(1000_000_000));
            }
        });
        pool.awaitQuiescence(5, TimeUnit.SECONDS);

        System.out.println("Minimum value: " + metrics.getMin());
        System.out.println("Maximum value: " + metrics.getMax());
    }
}
