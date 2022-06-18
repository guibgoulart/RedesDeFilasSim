package org.davidpedroguilherme.redesdefilassim.util;

import java.util.List;
import java.util.Map;
import org.davidpedroguilherme.redesdefilassim.objetos.FilaDTO;

public class Config {

  private int aleatorioPorSeed;
  private String modo;
  private List<Integer> seeds;
  private Map<String, Double> chegadas;
  private Map<String, FilaDTO> filas;
  private Map<String, Map<String, Double>> routing;

  public Config() {

  }

  public Config(int aleatorioPorSeed, String modo, List<Integer> seeds, Map<String, Double> chegadas,
      Map<String, FilaDTO> filas, Map<String, Map<String, Double>> routing) {
    this.aleatorioPorSeed = aleatorioPorSeed;
    this.modo = modo;
    this.seeds = seeds;
    this.chegadas = chegadas;
    this.filas = filas;
    this.routing = routing;
  }

  public int getAleatorioPorSeed() {
    return aleatorioPorSeed;
  }

  public void setAleatorioPorSeed(int aleatorioPorSeed) {
    this.aleatorioPorSeed = aleatorioPorSeed;
  }

  public String getModo() {
    return modo;
  }

  public void setModo(String modo) {
    this.modo = modo;
  }

  public List<Integer> getSeeds() {
    return seeds;
  }

  public void setSeeds(List<Integer> seeds) {
    this.seeds = seeds;
  }

  public Map<String, Double> getChegadas() {
    return chegadas;
  }

  public void setChegadas(Map<String, Double> chegadas) {
    this.chegadas = chegadas;
  }

  public Map<String, FilaDTO> getFilas() {
    return filas;
  }

  public void setFilas(Map<String, FilaDTO> filas) {
    this.filas = filas;
  }

  public Map<String, Map<String, Double>> getRouting() {
    return routing;
  }

  public void setRouting(Map<String, Map<String, Double>> routing) {
    this.routing = routing;
  }
}
