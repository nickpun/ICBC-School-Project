package apiServer;

import database.ModuleOneQueries;
import database.data_objects.Reservation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapCreator {
    public static Map<String, Object> putInMap(String key, Object data) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(key, data);
        return map;
    }
}
