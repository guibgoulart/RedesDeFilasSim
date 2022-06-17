import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class EstadoFila {
    String evento;
    Integer fila;
    Double tempo;
    double[] tempoNaFila;
    double tempoTotal;

    public EstadoFila(String evento, Integer fila, Double tempo, double[] tempoNaFila) {
        this.evento = evento;
        this.fila = fila;
        this.tempo = tempo;
        this.tempoNaFila = tempoNaFila;
        this.tempoTotal = Arrays.stream(tempoNaFila).sum();
    }

    public Map<String, Double> computaPorcentagem(){
        Map<String, Double> porcentagens = new HashMap<>();
        for (int i = 0; i < tempoNaFila.length; i++) {
            porcentagens.put("" + i, (tempoNaFila[i] / tempoTotal) * 100);
        }
        return porcentagens;
    }


    @Override
    public String toString() {
        return "Fila {" +
                "evento: '" + evento + '\'' +
                ", fila: " + fila +
                ", tempo: " + tempo +
                ", tempoNaFila: " + Arrays.toString(tempoNaFila) +
                '}';
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public Integer getFila() {
        return fila;
    }

    public void setFila(Integer fila) {
        this.fila = fila;
    }

    public Double getTempo() {
        return tempo;
    }

    public void setTempo(Double tempo) {
        this.tempo = tempo;
    }

    public double[] getTempoNaFila() {
        return tempoNaFila;
    }

    public void setTempoNaFila(double[] tempoNaFila) {
        this.tempoNaFila = tempoNaFila;
    }

    public double getTempoTotal() {
        return tempoTotal;
    }

    public void setTempoTotal(double tempoTotal) {
        this.tempoTotal = tempoTotal;
    }

//    public String toStringCsvFile() {
//        return event + ";" + queue + ";" + String.format("%.04f", time) + ";" + Arrays.stream(occupiedQueueTime).mapToObj(queue -> String.format("%.04f", queue)).collect(Collectors.joining(";")) + "\n";
//    }
}
