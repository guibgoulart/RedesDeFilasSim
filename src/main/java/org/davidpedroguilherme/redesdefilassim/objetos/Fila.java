package org.davidpedroguilherme.redesdefilassim.objetos;

import java.util.Map;

public class Fila {

  final public String[] chavesOrdenadas;
  public double[] filaList;
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

  public Fila(String nome, Double tempoChegada, FilaEntidade filaEntidade, Map<String, Double> routing) {
    this.nome = nome;
    this.tempoChegada = tempoChegada;
    this.capacidade = filaEntidade.getCapacidade();
    this.tempoAtual = 0.0;
    this.contAtual = 0;
    this.servidores = filaEntidade.getServidores();
    this.minChegada = filaEntidade.getMinChegada();
    this.maxChegada = filaEntidade.getMaxChegada();
    this.minSaida = filaEntidade.getMinSaida();
    this.maxSaida = filaEntidade.getMaxSaida();
    this.filaList = new double[this.capacidade == null ? 100 : filaEntidade.getCapacidade() + 1];
    this.routing = routing;
    chavesOrdenadas = routing.keySet()
        .stream()
        .toArray(String[]::new);
  }

  public Map<String, Double> getRouting() {
    return routing;
  }

  public void setRouting(Map<String, Double> routing) {
    this.routing = routing;
  }

  public String getRoutingNamed() {
    return routing.keySet().toArray(String[]::new)[0];
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
