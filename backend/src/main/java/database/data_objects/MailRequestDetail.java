package database.data_objects;

import java.sql.Timestamp;

public class MailRequestDetail {
    int requestId;
    String name;
    String contact;
    String status;
    String location;
    Timestamp requestDate;
    String requestType;

    public MailRequestDetail(int requestId, String name, String contact, String status, String location, Timestamp requestDate, String requestType) {
        this.requestId = requestId;
        this.name = name;
        this.contact = contact;
        this.status = status;
        this.location = location;
        this.requestDate = requestDate;
        this.requestType = requestType;
    }

    public int getRequestId() {
        return requestId;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }

    public String getStatus() {
        return status;
    }

    public String getLocation() {
        return location;
    }

    public Timestamp getRequestDate() {
        return requestDate;
    }

    public String getRequestType() {
        return requestType;
    }
}
