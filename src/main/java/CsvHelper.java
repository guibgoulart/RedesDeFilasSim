import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class CsvHelper {
    public static void saveCsvFile(File file, List<EstadoFila> queueStates, int capacity) {
        int i;
        StringBuilder sb = new StringBuilder();
        sb.append("Evento;Fila;Tempo;");

        for (i = 0; i < capacity - 1; i++) sb.append(i+";");
        sb.append(i).append("\n");

        queueStates.forEach(item -> sb.append(item.toStringCsvFile()));

        try (PrintWriter writer = new PrintWriter(file)) {
            writer.write(sb.toString());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
