package org.davidpedroguilherme.redesdefilassim.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.davidpedroguilherme.redesdefilassim.objetos.EstadoFila;
import org.davidpedroguilherme.redesdefilassim.objetos.Fila;

public class FilaBuilder {

  public static Map<String, Fila> buildQueues(Config config) {
    Map<String, Fila> resultMap = new HashMap<>();
    config.getFilas().forEach((key, value) -> resultMap.put(key,
        new Fila(key, config.getChegadas().get(key), value, config.getRouting().get(key))));
    return resultMap;
  }

  public static Map<String, List<EstadoFila>> buildQueueStates(Config config) {
    Map<String, List<EstadoFila>> resultMap = new HashMap<>();
    config.getFilas().forEach((key, value) -> resultMap.put(key, new ArrayList<>()));
    return resultMap;
  }
}
