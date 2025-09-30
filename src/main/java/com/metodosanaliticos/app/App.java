package com.metodosanaliticos.app;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class App {
    private final static BigInteger c_x0 = new BigInteger("123456789");
    private final static BigInteger c_a  = new BigInteger("1664525");
    private final static BigInteger c_c  = new BigInteger("1013904223");
    private final static BigInteger c_M  = new BigInteger(new BigInteger("2").pow(32).toString());

    public static void main(String[] args) {
        NumberGenerator numberGenerator = new NumberGenerator(c_x0, c_a, c_c, c_M);

        List<Queue> queues = new ArrayList<>();

        Queue fila1 = new Queue(0, 1, 1, 2.0, 4.0, 1.0, 2.0);
        Queue fila2 = new Queue(1, 5, 2, 0.0, 0.0, 4.0, 6.0);
        Queue fila3 = new Queue(2, 10, 2, 0.0, 0.0, 5.0, 15.0);

        fila1.setRouting(new int[]{1, 2}, new double[]{0.8, 0.2});
        fila2.setRouting(new int[]{-1, 2, 0}, new double[]{0.2, 0.5, 0.3});
        fila3.setRouting(new int[]{-1, 1}, new double[]{0.3, 0.7});

        queues.add(fila1);
        queues.add(fila2);
        queues.add(fila3);

        Simulator simulator = new Simulator(queues, numberGenerator);

        System.out.println("Iniciando a simulação...");
        simulator.run();
        System.out.println("Simulação concluída.");
    }
}
