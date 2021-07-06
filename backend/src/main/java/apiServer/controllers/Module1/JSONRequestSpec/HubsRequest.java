package apiServer.controllers.Module1.JSONRequestSpec;

import java.util.List;

public class HubsRequest {
    private Integer floorId;
    private List<Double> xlocs;
    private List<Double> ylocs;

    public HubsRequest(Integer floorId, List<Double> xlocs, List<Double> ylocs) {
        this.floorId = floorId;
        this.xlocs = xlocs;
        this.ylocs = ylocs;
    }

    public Integer getFloorId() {
        return floorId;
    }

    public List<Double> getXlocs() {
        return xlocs;
    }

    public List<Double> getYlocs() {
        return ylocs;
    }
}
