import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilaBuilder {

    public static Map<String, Fila> buildQueues(ConfigDTO config){
        Map<String, Fila> resultMap = new HashMap<>();
        config.getFila().forEach((key, value) -> resultMap.put(key, new Fila(key, config.getChegadas().get(key), value, config.getRouting().get(key))));
        return resultMap;
    }

    public static Map<String, List<EstadoFila>> buildQueueStates(ConfigDTO config){
        Map<String, List<EstadoFila>> resultMap = new HashMap<>();
        config.getFilas().forEach((key, value) -> resultMap.put(key, new ArrayList<>()));
        return resultMap;
    }

}
