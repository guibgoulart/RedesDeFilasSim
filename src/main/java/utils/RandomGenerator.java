package utils;

public class RandomGenerator {

//    private final double A = 1234090.77;
//    private final double M = Math.pow(2, 20);
//    private final double c = 1074020.69;
//    private double x;

    static double[] congruenteLinear(int x0, int a, int c, int m, int tamanho) {

        double[] numerosAleatorios = new double[tamanho];

        numerosAleatorios[0] = x0;

        for (int i = 1; i < tamanho; i++) {
            numerosAleatorios[i] = (((numerosAleatorios[i-1] * a) + c) % m);
        }
        for (int i = 0; i < tamanho; i++) {
            numerosAleatorios[i] = (numerosAleatorios[i] / m);
        }
        return numerosAleatorios;
    }
}
