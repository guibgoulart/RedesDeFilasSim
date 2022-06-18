package org.davidpedroguilherme.redesdefilassim.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import org.davidpedroguilherme.redesdefilassim.objetos.EstadoFila;

public class CsvHelper {

  public static void saveCsvFile(File file, List<EstadoFila> queueStates, int capacity) {
    int i;
    StringBuilder sb = new StringBuilder();
    sb.append("Evento;org.davidpedroguilherme.redesdefilassim.objetos.Fila;Tempo;");

      for (i = 0; i < capacity - 1; i++) {
          sb.append(i + ";");
      }
    sb.append(i).append("\n");

    queueStates.forEach(item -> sb.append(item.toStringCsvFile()));

    try (PrintWriter writer = new PrintWriter(file)) {
      writer.write(sb.toString());
    } catch (FileNotFoundException e) {
      System.out.println(e.getMessage());
    }
  }
}
