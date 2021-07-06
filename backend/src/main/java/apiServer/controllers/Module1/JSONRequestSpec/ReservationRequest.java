package apiServer.controllers.Module1.JSONRequestSpec;

import java.sql.Timestamp;

public class ReservationRequest {

    private String userId;
    private Integer deskId;
    private Timestamp fromDate;
    private Timestamp toDate;

    public ReservationRequest(String userId, Integer deskId, Timestamp fromDate, Timestamp toDate) {
        this.userId = userId;
        this.deskId = deskId;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public String getUserId() {
        return userId;
    }

    public Integer getDeskId() {
        return deskId;
    }

    public Timestamp getFromDate() {
        return fromDate;
    }

    public Timestamp getToDate() {
        return toDate;
    }

}
