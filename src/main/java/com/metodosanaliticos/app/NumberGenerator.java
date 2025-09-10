package com.metodosanaliticos.app;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberGenerator
    {
        private BigInteger x0; // Seed
        private BigInteger x;  // Last number generated
        private BigInteger a;  // Multiplier
        private BigInteger c;  // Increment
        private BigInteger M;  // Module

        NumberGenerator(BigInteger x0, BigInteger a, BigInteger c, BigInteger M)
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
