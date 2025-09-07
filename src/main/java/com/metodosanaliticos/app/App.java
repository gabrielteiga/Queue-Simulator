package com.metodosanaliticos.app;

import java.math.BigDecimal;
import java.math.BigInteger;

public class App {
    private final static BigInteger c_x0 = new BigInteger("123456789");
    private final static BigInteger c_a  = new BigInteger("1664525");
    private final static BigInteger c_c  = new BigInteger("1013904223");
    private final static BigInteger c_M  = new BigInteger(new BigInteger("2").pow(32).toString());

    public static void main(String[] args) {
        NumberGenerator numberGenerator = new NumberGenerator(
                c_x0,
                c_a ,
                c_c ,
                c_M
            );

        for (int i = 1000000; i > 0; i--)
        {
            double randomNumber = numberGenerator.nextRandom();
            System.out.println(randomNumber);
        }
    }

    public static class NumberGenerator
    {
        private BigInteger x0; // Seed
        private BigInteger x;  // Last number generated
        private BigInteger a;  // Multiplier
        private BigInteger c;  // Increment
        private BigInteger M;  // Module

        private NumberGenerator(BigInteger x0, BigInteger a, BigInteger c, BigInteger M)
        {
            this.x0 = new BigInteger(x0.toString());
            this.x  = this.x0;
            this.a  = new BigInteger(a.toString());
            this.c  = new BigInteger(c.toString());
            this.M  = new BigInteger(M.toString());
        }

        public double nextRandom()
        {
            this.x = ((this.a.multiply(this.x)).add(this.c)).mod(this.M);
            return new BigDecimal(this.x).divide(new BigDecimal(this.M)).doubleValue();
        }
    }
}