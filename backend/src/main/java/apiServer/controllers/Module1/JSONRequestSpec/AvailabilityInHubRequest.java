package apiServer.controllers.Module1.JSONRequestSpec;

import java.sql.Timestamp;

public class AvailabilityInHubRequest {
    private Integer hubId;
    private Timestamp fromDate;
    private Timestamp toDate;

    public AvailabilityInHubRequest(Integer hubId, Timestamp fromDate, Timestamp toDate) {
        this.hubId = hubId;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public Integer getHubId() {
        return hubId;
    }

    public Timestamp getFromDate() {
        return fromDate;
    }

    public Timestamp getToDate() {
        return toDate;
    }
}
