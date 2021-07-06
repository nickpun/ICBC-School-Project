package database.data_objects;

import java.util.List;

public class Building {

    int buildingId;
    String address;
    String name;
    List<Floor> floors;

    public Building (int buildingId, String address, String name, List<Floor> floors) {
        this.buildingId = buildingId;
        this.address = address;
        this.name = name;
        this.floors = floors;
    }

    public int getBuildingId() {
        return this.buildingId;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public List<Floor> getFloors() {
        return floors;
    }

}
