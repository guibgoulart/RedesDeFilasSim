import java.util.Map;

public class Fila {

    private String nome;
    private Double tempoChegada;
    private double tempoAtual;
    private int contAtual;
    private Integer capacidade;
    private int servidores;
    private Double minChegada;
    private Double maxChegada;
    private Double minSaida;
    private Double maxSaida;
    private Map<String, Double> routing;
    public double[] filaList;
    final public String[] orderedKeys;

    public Fila(String nome, Double tempoChegada, FilaDTO filaDTO, Map<String, Double> routing){
        this.nome = nome;
        this.tempoChegada = tempoChegada;
        this.capacidade = filaDTO.getCapacidade();
        this.tempoAtual = 0.0;
        this.contAtual = 0;
        this.servidores = filaDTO.getServidores();
        this.minChegada = filaDTO.getMinChegada();
        this.maxChegada = filaDTO.getMaxChegada();
        this.minSaida = filaDTO.getMinSaida();
        this.maxSaida = filaDTO.getMaxSaida();
        this.filaList = new double[this.capacidade == null ? 100 : filaDTO.getCapacidade() + 1];
        this.routing = routing;
        orderedKeys = routing.keySet()
                .stream().sorted((a, b) -> a.split("\\s")[0].compareTo(b.split("\\s")[0]))
                .toArray(String[]::new);
    }

    public Map<String, Double> getRouting() {
        return routing;
    }

    public String getRoutingNamed() {
        return routing.keySet().toArray(String[]::new)[0];
    }

    public void setRouting(Map<String, Double> routing) {
        this.routing = routing;
    }


    @Override
    public String toString() {
        return "Fila {" +
                "nome: '" + nome + '\'' +
                '}';
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getTempoChegada() {
        return tempoChegada;
    }

    public void setTempoChegada(Double tempoChegada) {
        this.tempoChegada = tempoChegada;
    }

    public double getTempoAtual() {
        return tempoAtual;
    }

    public void setTempoAtual(double tempoAtual) {
        this.tempoAtual = tempoAtual;
    }

    public int getContAtual() {
        return contAtual;
    }

    public void setContAtual(int contAtual) {
        this.contAtual = contAtual;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }

    public int getServidores() {
        return servidores;
    }

    public void setServidores(int servidores) {
        this.servidores = servidores;
    }

    public Double getMinChegada() {
        return minChegada;
    }

    public void setMinChegada(Double minChegada) {
        this.minChegada = minChegada;
    }

    public Double getMaxChegada() {
        return maxChegada;
    }

    public void setMaxChegada(Double maxChegada) {
        this.maxChegada = maxChegada;
    }

    public Double getMinSaida() {
        return minSaida;
    }

    public void setMinSaida(Double minSaida) {
        this.minSaida = minSaida;
    }

    public Double getMaxSaida() {
        return maxSaida;
    }

    public void setMaxSaida(Double maxSaida) {
        this.maxSaida = maxSaida;
    }
}
