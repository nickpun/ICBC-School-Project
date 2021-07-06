package database.data_objects;

import java.net.URL;
import java.sql.Blob;
import java.util.List;

public class FloorPlan {
    int floorId;
    String url;
    List<Hub> hubs;

    public FloorPlan(int floorId, String url, List<Hub> hubs) {
        this.floorId = floorId;
        this.url = url;
        this.hubs = hubs;
    }

    public int getFloorId() {
        return floorId;
    }

    public String getUrl() {
        return url;
    }

    public List<Hub> getListOfHubs() {
        return hubs;
    }
}
