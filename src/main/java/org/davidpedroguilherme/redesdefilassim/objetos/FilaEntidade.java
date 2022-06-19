package org.davidpedroguilherme.redesdefilassim.objetos;

public class FilaEntidade {

  private int servidores;
  private Integer capacidade;
  private Double minChegada;
  private Double maxChegada;
  private Double minSaida;
  private Double maxSaida;

  public FilaEntidade() {

  }

  public FilaEntidade(int servidores, Integer capacidade, Double minChegada, Double maxChegada, Double minSaida,
                      Double maxSaida) {
    this.servidores = servidores;
    this.capacidade = capacidade;
    this.minChegada = minChegada;
    this.maxChegada = maxChegada;
    this.minSaida = minSaida;
    this.maxSaida = maxSaida;
  }

  public int getServidores() {
    return servidores;
  }

  public void setServidores(int servidores) {
    this.servidores = servidores;
  }

  public Integer getCapacidade() {
    return capacidade;
  }

  public void setCapacidade(Integer capacidade) {
    this.capacidade = capacidade;
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

