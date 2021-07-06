package apiServer.controllers.Module1.JSONRequestSpec;

public class FloorRequest {
    private Integer buildingId;
    private Integer storey;
    private String floorPlanURL;

    public FloorRequest(Integer buildingId, Integer storey, String floorPlanURL) {
        this.buildingId = buildingId;
        this.storey = storey;
        this.floorPlanURL = floorPlanURL;
    }

    public Integer getBuildingId() {
        return buildingId;
    }

    public Integer getStorey() {
        return storey;
    }

    public String getFloorPlanURL() {
        return floorPlanURL;
    }
}
