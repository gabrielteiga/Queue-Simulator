
package com.metodosanaliticos.app;
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
}