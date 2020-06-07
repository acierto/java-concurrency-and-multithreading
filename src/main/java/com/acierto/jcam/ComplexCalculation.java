package com.acierto.jcam;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ComplexCalculation {

    public BigInteger calculateResult(BigInteger base1,
                                      BigInteger power1,
                                      BigInteger base2,
                                      BigInteger power2) {

        BigInteger result = BigInteger.ZERO;
        List<PowerCalculatingThread> threads = new ArrayList<>(2);
        threads.add(new PowerCalculatingThread(base1, power1));
        threads.add(new PowerCalculatingThread(base2, power2));

        for (PowerCalculatingThread thread : threads) {
            try {
                thread.start();
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            result = result.add(thread.getResult());
        }

        return result;
    }

    private static class PowerCalculatingThread extends Thread {
        private BigInteger result = BigInteger.ZERO;
        private final BigInteger base;
        private final BigInteger power;

        public PowerCalculatingThread(final BigInteger base, final BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
            result = base.pow(power.intValue());
        }

        public BigInteger getResult() {
            return result;
        }
    }

    public static void main(String[] args) {
        ComplexCalculation cc = new ComplexCalculation();
        BigInteger result = cc.calculateResult(new BigInteger("10000"), new BigInteger("500"),
                new BigInteger("30000"), new BigInteger("700"));
        System.out.println("Result is: " + result);
    }
}
