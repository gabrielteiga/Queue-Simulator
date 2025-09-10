package com.metodosanaliticos.app;

import java.math.BigInteger;

public class App {
    // Parâmetros para o Gerador de Números Aleatórios (Congruente Linear)
    private final static BigInteger c_x0 = new BigInteger("123456789");
    private final static BigInteger c_a  = new BigInteger("1664525");
    private final static BigInteger c_c  = new BigInteger("1013904223");
    private final static BigInteger c_M  = new BigInteger(new BigInteger("2").pow(32).toString());

    public static void main(String[] args) {

        NumberGenerator numberGenerator = new NumberGenerator(
                c_x0,
                c_a,
                c_c,
                c_M
        );

        Queue fila1 = new Queue(0, 3, 2);
        
        Queue fila2 = new Queue(1, 5, 1);
        
        Queue[] filas = {fila1, fila2};

        Simulator simulator = new Simulator(filas, numberGenerator);

        System.out.println("Iniciando a simulação...");
        simulator.run();
        System.out.println("Simulação concluída.");
    }
}
