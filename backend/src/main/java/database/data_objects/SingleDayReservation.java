package database.data_objects;

import java.sql.Timestamp;

public class SingleDayReservation {
    int deskId;
    int deskNo;
    int storey;
    String buildingName;
    Timestamp date;

    public SingleDayReservation(int deskId, int deskNo, int storey, String buildingName, Timestamp date) {
        this.deskId = deskId;
        this.deskNo = deskNo;
        this.storey = storey;
        this.buildingName = buildingName;
        this.date = date;
    }

    // convert from database's format, implicitly assuming the from date is what represents the day properly
    public SingleDayReservation(Reservation res) {
        this.deskId = res.getDeskId();
        this.deskNo = res.getDeskNo();
        this.storey = res.getStorey();
        this.buildingName = res.getBuildingName();
        this.date = res.getFromDate();
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

    public Timestamp getDate() { return date; }

}