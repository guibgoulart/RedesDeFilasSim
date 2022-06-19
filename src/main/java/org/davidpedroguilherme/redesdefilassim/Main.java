package org.davidpedroguilherme.redesdefilassim;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.davidpedroguilherme.redesdefilassim.objetos.EstadoFila;
import org.davidpedroguilherme.redesdefilassim.objetos.Fila;
import org.davidpedroguilherme.redesdefilassim.objetos.FilaEntidade;
import org.davidpedroguilherme.redesdefilassim.util.Config;
import org.davidpedroguilherme.redesdefilassim.util.FilaBuilder;

public class Main {

  static final String CHEGADA = "chegada";
  static final String PASSAGEM = "passagem";
  static final String SAIDA = "saida";

  static Double tempoTotal;

  static Double tempo = 0.00;
  static int execucoes;
  static Map<String, Fila> filas;
  static Map<String, List<EstadoFila>> estadoFilas = new HashMap<>();
  static List<Escalonador> listaEscalonador;
  static ArrayList<Double> listaAleatorios;

  public static void main(String[] args) throws IOException {

    // criação de arquivos de output
    File outputDir = new File("./output");
    if(!outputDir.exists()) {
      outputDir.mkdir();
    }
    File output = new File("./output/output.txt");
      if (output.exists()) {
          output.delete();
      }

    // instanciamento de variáveis de execução
    int aleatorioPorSeed = 100000;
    List<Integer> seeds = Arrays.asList(11);
    Map<String, Double> chegadas = Map.of("F1", 1.0);
    FilaEntidade f1 = new FilaEntidade(1, null, 1.0, 4.0, 1.0, 1.5);
    FilaEntidade f2 = new FilaEntidade(3, 5, null, null, 5.0, 10.0);
    FilaEntidade f3 = new FilaEntidade(2, 8, null, null, 10.0, 20.0);
    Map<String, FilaEntidade> filasConfig = Map.of("F1", f1, "F2", f2, "F3", f3);
    LinkedHashMap<String, Double> transferenciasF1 = montarDicionario(List.of("F2", "F3"), List.of(0.8, 0.2));
    LinkedHashMap<String, Double> transferenciasF2 = montarDicionario(List.of("F1", "F3", "SAIDA"),
        List.of(0.3, 0.5, 0.2));
    LinkedHashMap<String, Double> transferenciasF3 = montarDicionario(List.of("F2", "SAIDA"), List.of(0.7, 0.3));
    Map<String, Map<String, Double>> transferencias = Map.of("F1", transferenciasF1, "F2", transferenciasF2, "F3",
        transferenciasF3);

    Config config = new Config(aleatorioPorSeed, "tandem", seeds, chegadas, filasConfig, transferencias);

    // numero de execuçoes
    execucoes = config.getSeeds().size();

    BufferedWriter writer = new BufferedWriter(new FileWriter(output, true)); // inicia escrita nos arquivos

    writer.write("\n" +
            "\n" +
            "                                                       \n" +
            " ____ ___ __  __ _   _ _        _    ____   ___  ____  \n" +
            "/ ___|_ _|  \\/  | | | | |      / \\  |  _ \\ / _ \\|  _ \\ \n" +
            "\\___ \\| || |\\/| | | | | |     / _ \\ | | | | | | | |_) |\n" +
            " ___) | || |  | | |_| | |___ / ___ \\| |_| | |_| |  _ < \n" +
            "|____|___|_|  |_|\\___/|_____/_/   \\_|____/ \\___/|_| \\_\\\n" +
            "                                                       \n" +
            "                                                       \n" +
            "                     ____  _____                       \n" +
            "                    |  _ \\| ____|                      \n" +
            "                    | | | |  _|                        \n" +
            "                    | |_| | |___                       \n" +
            "                    |____/|_____|                      \n" +
            "                                                       \n" +
            "                                                       \n" +
            "            _____ ___ _        _    ____               \n" +
            "           |  ___|_ _| |      / \\  / ___|              \n" +
            "           | |_   | || |     / _ \\ \\___ \\              \n" +
            "           |  _|  | || |___ / ___ \\ ___) |             \n" +
            "           |_|   |___|_____/_/   \\_|____/              \n" +
            "                                                       \n" +
            "\n");
    writer.write("Desenvolvido em junho de 2022 por Guilherme Goulart, Pedro Portella e David Neto\n");
    writer.write("Para a cadeira de Simulação e Métodos Analíticos da PUCRS ministrada pelo professor Afonso Sales\n\n");

    for (int i = 0; i < execucoes; i++) {
      // qual seed estamos usando
      System.out.println("Executando simulação utilizando seed: " + config.getSeeds().get(i));

      System.out.println("Modo de simulação: " + config.getModo());
      listaEscalonador = new ArrayList<>();

      // constroi filas e estado das filas
      filas = FilaBuilder.criaFila(config);
      estadoFilas = FilaBuilder.criaEstadoFila(config);

      // cria lista de aleatorios
      listaAleatorios = new ArrayList<>();
      listaAleatorios.addAll(
          GeradorAleatorios.gerarListaAleatorios(config.getSeeds().get(i), config.getAleatorioPorSeed()));

      // inserção dos valores nulos (primeira linha)
      estadoFilas.forEach(
        (key, value) ->
          value.add(new EstadoFila("-", 0, 0.00, filas.get(key).filaList.clone())));

      filas.forEach(
        (key, value) ->
          value.filaList[0] = filas.get("F1").getTempoChegada());

      // inserção do primeiro evento (chegada)
      estadoFilas.forEach(
        (key, value) ->
          value.add(new EstadoFila("chegada", 1, 1.00, filas.get(key).filaList.clone())));

      // executa primeira chegada
      chegada("F1", filas.get("F1").getTempoChegada());
      System.out.println();

      while (!listaAleatorios.isEmpty()) { // laço geral de execução
        try {
          Escalonador menorTempo = menor(); // recupera o evento com o menor tempo
          filas.forEach(
            (key, value) ->
              value.filaList[value.getContAtual()] += menorTempo.tempoBruto - tempo); // atualiza o tempo de todas tabelas pois um evento novo ocorreu

          switch (menorTempo.evento.toLowerCase()) { // verifica qual o próximo evento
            case CHEGADA -> chegada("F1", menorTempo.tempoBruto);
            case PASSAGEM -> passagem(menorTempo.fila, menorTempo.proximaFilaOpcional, menorTempo.tempoBruto);
            case SAIDA -> saida(menorTempo.fila, menorTempo.tempoBruto);
            default -> throw new RuntimeException();
          }

          estadoFilas.forEach(  // adiciona um novo estado na fila
            (key, value) -> value.add(
              new EstadoFila(menorTempo.evento, filas.get(key).getContAtual(),
                menorTempo.tempoBruto, filas.get(key).filaList.clone())));
          listaEscalonador.remove(menorTempo); // remove o último evento executado
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      writer.write("Simulação: #" + (i + 1) + "\n\n");
      for (Map.Entry<String, List<EstadoFila>> item : estadoFilas.entrySet()) {
        writer.write("*********************************************\n");
        writer.write("Fila: " + item.getKey() + "\n");
        writer.write("Estado: " + "      " + "Tempo: " + "          " + "Porcentagem: " + "      " + "\n");
        item.getValue().get(item.getValue().size() - 1).tempoNaFila().forEach(
          (key, value) ->
          {
            try {
              writer.write(key + "      " + value + "      " + item.getValue().get(item.getValue().size() - 1).computaPorcentagem().get(key) + "\n");
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          }
        );
        tempoTotal = item.getValue().get(item.getValue().size() - 1).getTempoTotal();
      }
      writer.write("=============================================\n");
      writer.write("Tempo total: " + tempoTotal.toString() + "\n");
      writer.write("=============================================\n");

      writer.write("\n");
      writer.write("#############################################\n");
      writer.write("\n");
    }
    writer.close();
    System.out.println("Simulação da fila encerrada.");
  }

  private static void chegada(String fila, double temp) { // método para lidar com chegada
    tempo = temp;
    Fila f = filas.get(fila);
    if (f.getCapacidade() == null || f.getContAtual() < f.getCapacidade()) { // verifica se tem capacidade
      f.setContAtual(f.getContAtual() + 1);
      if (f.getContAtual() <= f.getServidores()) { // verifica se pode realizar auto-atendimento
        agendaMovimentoFila(fila); // agenda chegada
      }
    }
    listaEscalonador.add(new Escalonador(fila, CHEGADA)); // adiciona uma chegada
  }

  private static void passagem(String de, String para, double temp) { // metodo de executar passagem
    tempo = temp;

    if (de.equals("") || para.equals("")) {
        return;
    }

    Fila origem = filas.get(de);
    Fila dest = filas.get(para);

    origem.setContAtual(origem.getContAtual() - 1);
    if (origem.getContAtual() >= origem.getServidores()) { // verifica se há possibilidade de passagem
      agendaMovimentoFila(de); // agenda passagem
    }
    if (dest.getCapacidade() != null && dest.getContAtual() >= dest.getCapacidade()) {
      return;
    }
    dest.setContAtual(dest.getContAtual() + 1);
    if (dest.getContAtual() <= dest.getServidores()) { // verifica se há possibilidade de saída
      listaEscalonador.add(new Escalonador(para, SAIDA)); // executa saída
    }
  }

  private static void saida(String fila, double temp) { // metodo de executar saida
    tempo = temp;
    Fila f = filas.get(fila);

    f.setContAtual(f.getContAtual() - 1);
    if (f.getContAtual() >= f.getServidores()) { // verifica se ha possibilidade de saida
      agendaMovimentoFila(fila); // agenda saida
    }
  }

  private static void agendaMovimentoFila(String fila) { // metodo de agendar passagem ou saida
    String proximaFila = proxFila(fila);
    if (proximaFila.isEmpty()) {
      System.err.println("Fila não tem próximo destino nem saída");
    } else if (proximaFila.equalsIgnoreCase(SAIDA)) {
      listaEscalonador.add(new Escalonador(fila, SAIDA));
    } else if (filas.get(fila).getContAtual() > 0) {
      listaEscalonador.add(new Escalonador(fila, proximaFila, PASSAGEM));
    }
  }

  private static Escalonador menor() { // metodo para recuperar evento com menor tempo
    return listaEscalonador.stream().min((o1, o2) -> o1.tempoBruto < o2.tempoBruto ? -1 : 1).get();
  }

  private static LinkedHashMap<String, Double> montarDicionario(List<String> chaves, List<Double> valores) { // metodo para facilitar instanciamento dos objetos da fila
    if (chaves.size() != valores.size()) {
        return null;
    }
    LinkedHashMap<String, Double> dicionario = new LinkedHashMap<>();
    for (int i = 0; i < chaves.size(); i++) {
      dicionario.put(chaves.get(i), valores.get(i));
    }
    return dicionario;
  }

  private static class Escalonador { // classe responsavel pela tabela de eventos

    String fila;
    String proximaFilaOpcional;
    String evento;
    double tempoBruto;
    double aleatorio;

    public Escalonador(String fila, String evento) {
      inicializacao(fila, null, evento);
    }

    public Escalonador(String fila, String proximaFila, String evento) {
      inicializacao(fila, proximaFila, evento);
    }

    private void inicializacao(String fila, String proximaFila, String evento) {
      if (listaAleatorios.isEmpty()) {
          return;
      }
      this.fila = fila;
      this.proximaFilaOpcional = proximaFila;
      this.evento = evento;
      Fila f = filas.get(fila);
      double rd = listaAleatorios.remove(0);
      if (evento.equals(CHEGADA)) {
        aleatorio = ((f.getMaxChegada() - f.getMinChegada()) * rd) + f.getMinChegada();
      } else if (evento.equals(SAIDA)) {
        aleatorio = ((f.getMaxSaida() - f.getMinSaida()) * rd) + f.getMinSaida();
      } else if (evento.equals(PASSAGEM)) {
        aleatorio = ((f.getMaxSaida() - f.getMinSaida()) * rd) + f.getMinSaida();
      } else {
        aleatorio = Double.MAX_VALUE;
      }
      tempoBruto = tempo + aleatorio;
    }

    @Override
    public String toString() {
      return "Escalonador {" +
          "evento: '" + evento + '\'' +
          ", tempo: " + tempoBruto +
          ", aleatorio: " + aleatorio +
          '}';
    }
  }

  private static String proxFila(String de) { // método para lidar com roteamento de uma fila para outra
    double random = listaAleatorios.remove(0); // finaliza uma execução
    Map<String, Double> routing = filas.get(de).getRouting();
    String[] chavesOrdenadas = filas.get(de).chavesOrdenadas;
    if (routing == null || chavesOrdenadas == null) {
      return "";
    }
    double aux = 0;
    for (String key : chavesOrdenadas) { // como uma fila pode ter multiplas saidas e essas saidas tem prob diferentes, precisamos garantir que ele va para a fila correta
      double prox = routing.get(key);
      if (aux <= random && random <= aux + prox) {
        return key; // retorna a proxima fila
      }
      aux += prox;
    }
    return "";
  }
}