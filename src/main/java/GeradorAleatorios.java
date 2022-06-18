import java.util.ArrayList;
import java.util.List;

public class GeradorAleatorios {
    private final double A = 1234090.77;
    private final double M = Math.pow(2, 20);
    private final double c = 1074020.69;
    private double x;

    public static List<Double> gerarListaAleatorios(int seed, int tamanho){
        List<Double> resultado = new ArrayList<>();
        Random r = new Random(seed);
        for (int i = 0; i < tamanho; i++)
            resultado.add(r.next());
        return resultado;
    }
}

class Random {

    private final double A = 1234090.77;
    private final double M = Math.pow(2, 20);
    private final double c = 1074020.69;
    private double x;

    public Random(double x) {
        this.x = x;
    }

    public double next(){
        x = ((A * x + c) % M) / M;
        return x;
    }
}
