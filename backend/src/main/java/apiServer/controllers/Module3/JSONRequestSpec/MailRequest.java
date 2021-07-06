package apiServer.controllers.Module3.JSONRequestSpec;

import java.sql.Timestamp;

public class MailRequest {
    private String userId;
    private String sender;
    private Timestamp arrivalDate;
    private String returnAddress;

    public MailRequest(String userId, String sender, Timestamp arrivalDate, String returnAddress) {
        this.userId = userId;
        this.sender = sender;
        this.arrivalDate = arrivalDate;
        this.returnAddress = returnAddress;
    }

    public String getUserId() {
        return userId;
    }

    public String getSender() {
        return sender;
    }

    public Timestamp getArrivalDate() {
        return arrivalDate;
    }

    public String getReturnAddress() {
        return returnAddress;
    }
}
