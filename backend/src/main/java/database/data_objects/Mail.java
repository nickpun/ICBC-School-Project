package database.data_objects;

import java.sql.Timestamp;

public class Mail {

    int mailId;
    String sender;
    Timestamp arrivalDate;
    String returnAddress;

    public Mail(int mailId, String sender, Timestamp arrivalDate, String returnAddress) {
        this.mailId = mailId;
        this.sender = sender;
        this.arrivalDate = arrivalDate;
        this.returnAddress = returnAddress;
    }

    public int getMailId() {
        return mailId;
    }

    public String getSender() {
        return sender;
    }

    public Timestamp getArrivalDate() {
        return arrivalDate;
    }

    public String getReturnAddress() { return returnAddress; }
}
