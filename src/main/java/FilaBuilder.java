import java.util.*;

public class FilaBuilder {

    public static Map<String, Fila> buildQueues(Config config){
        Map<String, Fila> resultMap = new HashMap<>();
        config.getFilas().forEach((key, value) -> resultMap.put(key, new Fila(key, config.getChegadas().get(key), value, config.getRouting().get(key))));
        return resultMap;
    }

    public static Map<String, List<EstadoFila>> buildQueueStates(Config config){
        Map<String, List<EstadoFila>> resultMap = new HashMap<>();
        config.getFilas().forEach((key, value) -> resultMap.put(key, new ArrayList<>()));
        return resultMap;
    }
}
