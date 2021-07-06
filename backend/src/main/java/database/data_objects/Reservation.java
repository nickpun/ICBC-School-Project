package database.data_objects;

import java.sql.Timestamp;

public class Reservation {
    int deskId;
    int deskNo;
    int storey;
    String buildingName;
    Timestamp fromDate;
    Timestamp toDate;

    public Reservation(int deskId, int deskNo, int storey, String buildingName, Timestamp fromDate, Timestamp toDate) {
        this.deskId = deskId;
        this.deskNo = deskNo;
        this.storey = storey;
        this.buildingName = buildingName;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public int getDeskId() {
        return deskId;
    }

    public int getDeskNo() {
        return deskNo;
    }

    public int getStorey() {
        return storey;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public Timestamp getFromDate() { return fromDate; }

    public Timestamp getToDate() {
        return toDate;
    }

}