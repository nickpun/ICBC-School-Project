package apiServer.controllers.Module1.JSONRequestSpec;

import java.sql.Timestamp;

public class SingleDayReservationRequest {

    private String userId;
    private Integer deskId;
    private Timestamp date;

    public SingleDayReservationRequest(String userId, Integer deskId, Timestamp date) {
        this.userId = userId;
        this.deskId = deskId;
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public Integer getDeskId() {
        return deskId;
    }

    public Timestamp getDate() {
        return date;
    }

}
