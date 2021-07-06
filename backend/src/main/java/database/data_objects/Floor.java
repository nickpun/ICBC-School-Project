package database.data_objects;

public class Floor {

    int floorId;
    int storey;
    int buildingId;

    public Floor (int floorId, int storey, int buildingId) {
        this.floorId = floorId;
        this.storey = storey;
        this.buildingId = buildingId;
    }

    public int getFloorId() {
        return floorId;
    }

    public int getStorey() {
        return storey;
    }

    public int getBuildingId() {
        return buildingId;
    }
}
