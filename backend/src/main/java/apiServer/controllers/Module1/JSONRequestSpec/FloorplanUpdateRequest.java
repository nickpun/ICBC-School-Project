package apiServer.controllers.Module1.JSONRequestSpec;

public class FloorplanUpdateRequest {
    private String floorPlanURL;

    public FloorplanUpdateRequest(String floorPlanURL) {
        this.floorPlanURL = floorPlanURL;
    }

    public String getFloorPlanURL() {
        return floorPlanURL;
    }
}
