package apiServer.controllers.Module1.JSONRequestSpec;

import java.util.List;

public class DesksRequest {
    private List<Integer> deskNos;
    private Integer hubId;

    public DesksRequest(List<Integer> deskNos, Integer hubId) {
        this.deskNos = deskNos;
        this.hubId = hubId;
    }

    public List<Integer> getDeskNos() {
        return deskNos;
    }

    public Integer getHubId() {
        return hubId;
    }
}
