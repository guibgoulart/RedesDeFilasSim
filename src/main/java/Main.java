import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class Main {
    static final String CHEGADA = "chegada";
    static final String PASSAGEM = "passagem";
    static final String SAIDA = "saida";

    static Double tempo = 0.00;
    static int execucoes;
    static Map<String, Fila> filas;
    static Map<String, List<EstadoFila>> estadoFilas = new HashMap<>();
    static List<Escalonador> listaEscalonador;
    static ArrayList<Double> listaAleatorios;

    public static void main(String[] args) throws IOException, URISyntaxException {
        if (args.length == 0) return;

        File output = new File("./output.txt");
        if (output.exists()) output.delete();

        //dto.ConfigDTO config = FileReader.readConfigFileFromPath(args[0]);
        //boolean withCsv = args.length == 2 && Objects.equals(args[1], "with-csv");

        // numero de execuçoes
        execucoes = config.getSeeds().size();

        for (int i = 0; i < execucoes; i++) {
            // qual seed estamos usando
            System.out.println("Executing queue simulation using seed: " + config.getSeeds().get(i));

            // n sei qq isso
            System.out.println("Simulation mode: " + config.getMode());
            listaEscalonador = new ArrayList<>();

            // constroi filas e estado das filas
            filas = FilaBuilder.buildQueues(config);
            estadoFilas = FilaBuilder.buildQueueStates(config);

            // cria lista de aleatorios
            listaAleatorios = new ArrayList<>();
            listaAleatorios.addAll(
                    ListGenerator.generateListOfRandomNumbers(config.getSeeds().get(i), config.getRandNumbersPerSeed()));

            estadoFilas.forEach(
                    (key, value) ->
                            value.add(new EstadoFila("-", 0, 0.00, filas.get(key).filaList.clone())));

            filas.forEach(
                    (key, value) ->
                            value.filaList[0] = filas.get("Q1").getTempoChegada());

            estadoFilas.forEach(
                    (key, value) ->
                            value.add(new EstadoFila("chegada", 1, 1.00, filas.get(key).filaList.clone())));

            chegada("Q1", filas.get("Q1").getTempoChegada());
            System.out.println();
            while (!listaAleatorios.isEmpty()) {
                try {
                    var menor = menor();
                    filas.forEach(
                            (key, value) ->
                                    value.filaList[value.getContAtual()] += menor.tempoBruto - tempo);

                    switch (menor.evento.toLowerCase()) {
                        case CHEGADA: {
                            chegada("Q1", menor.tempoBruto);
                            break;
                        }
                        case PASSAGEM: {
                            passagem(menor.fila, menor.proximaFilaOpcional, menor.tempoBruto);
                            break;
                        }
                        case SAIDA: {
                            saida(menor.fila, menor.tempoBruto);
                            break;
                        }
                        default:
                            break;
                    }
                    estadoFilas.forEach(
                            (key, value) -> value.add(
                                    new EstadoFila(menor.evento, filas.get(key).getContAtual(),
                                            menor.tempoBruto, filas.get(key).filaList.clone())));
                    listaEscalonador.remove(menor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(output, true));

            writer.write("Execution: " + (i + 1) + ":\n");
            for (Map.Entry<String, List<EstadoFila>> item : estadoFilas.entrySet()) {
                writer.write("Queue: " + item.getKey() + "\n");
                writer.write("Queue final state: " + item.getValue().get(item.getValue().size() - 1) + "\n");
                writer.write("Percentages: " + item.getValue().get(item.getValue().size() - 1).computaPorcentagem().toString() + "\n");
                writer.write("Total Time: " + item.getValue().get(item.getValue().size() - 1).getTempoTotal() + "\n");
                writer.write("----------------------------------------------------\n");
                Integer aux = filas.get(item.getKey()).getCapacidade();
//                if (withCsv)
//                    CsvHelper.saveCsvFile(new File("output" + (i + 1) + item.getKey() + ".csv"), item.getValue(), aux == null ? 100 : aux + 1);
            }
            writer.write("\n\n");
            writer.close();
            System.out.println("Output of execution " + (i + 1) + " saved.\n");
        }
        System.out.println("Queue simulation has finished.");
    }

    private static String proxFila(String from) {
        double random = listaAleatorios.remove(0);
        var routing = filas.get(from).getRouting();
        var chavesOrdenadas = filas.get(from).orderedKeys;
        if (routing == null || chavesOrdenadas == null) {
            return "";
        }
        double floor = 0;
        for (String key : chavesOrdenadas) {
            double next = routing.get(key);
            if (floor <= random && random <= floor + next) {
                return key.split("\\s")[1];
            }
            floor += next;
        }
        System.err.printf("Incorrect queue routing in sample.yaml, for queue \"%s\".\n", from);
        return "";
    }

    private static class Escalonador {
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
            if (listaAleatorios.isEmpty())
                return;
            this.fila = fila;
            this.proximaFilaOpcional = proximaFilaOpcional;
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

    private static void chegada(String fila, double temp) {
        tempo = temp;
        Fila f = filas.get(fila);
        if (f.getCapacidade() == null || f.getContAtual() < f.getCapacidade()) { // if can add more
            f.setContAtual(f.getContAtual() + 1);
            if (f.getContAtual() <= f.getServidores()) { // if can auto-attended
                agendaMovimentoFila(fila);
            }
        }
        listaEscalonador.add(new Escalonador(fila, CHEGADA));
    }

    private static void passagem(String de, String para, double temp) {
        tempo = temp;

        if (de.equals("") || para.equals("")) return;

        Fila origem = filas.get(de);
        Fila dest = filas.get(para);

        origem.setContAtual(origem.getContAtual() - 1);
        if (origem.getContAtual() >= origem.getServidores()) { // if source server can serve
            agendaMovimentoFila(de);
        }
        if (dest.getCapacidade() != null && dest.getContAtual() >= dest.getCapacidade()) {
            return;
        }
        dest.setContAtual(dest.getContAtual() + 1);
        if (dest.getContAtual() <= dest.getServidores()) {
            listaEscalonador.add(new Escalonador(para, SAIDA));
        }
    }

    private static void saida(String fila, double temp) {
        tempo = temp;
        Fila f = filas.get(fila);

        f.setContAtual(f.getContAtual() - 1);
        if (f.getContAtual() >= f.getServidores()) {
            agendaMovimentoFila(fila);
        }
    }

    private static void agendaMovimentoFila(String fila) {
        String proximaFila = proxFila(fila);
        if (proximaFila.isEmpty()) {
            System.err.println("Queue doesn't have any destination nor exit");
        } else if (proximaFila.equalsIgnoreCase(SAIDA)) {
            listaEscalonador.add(new Escalonador(fila, SAIDA));
        } else if (filas.get(fila).getContAtual() > 0) { // action loss
            listaEscalonador.add(new Escalonador(fila, proximaFila, PASSAGEM));
        }
    }

    private static Escalonador menor() {
        return listaEscalonador.stream().min((o1, o2) -> o1.tempoBruto < o2.tempoBruto ? -1 : 1).get();
    }
}
