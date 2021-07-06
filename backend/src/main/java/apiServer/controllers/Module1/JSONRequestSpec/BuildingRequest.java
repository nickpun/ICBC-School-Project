package apiServer.controllers.Module1.JSONRequestSpec;

public class BuildingRequest {
    private String address;
    private String buildingName;

    public BuildingRequest(String address, String buildingName) {
        this.address = address;
        this.buildingName = buildingName;
    }

    public String getAddress() {
        return address;
    }

    public String getBuildingName() {
        return buildingName;
    }

}
