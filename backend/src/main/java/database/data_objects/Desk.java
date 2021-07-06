package database.data_objects;

public class Desk {

    int deskId;
    int deskNo;
    Boolean isAvailable;
    int hubId;

    public Desk(int deskId, int deskNo, Boolean isAvailable, int hubId) {
        this.deskId = deskId;
        this.deskNo = deskNo;
        this.isAvailable = isAvailable;
        this.hubId = hubId;

    }

    public int getDeskId() { return deskId; }


    public int getDeskNo() { return deskNo; }

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public int getHubId() { return hubId; }
}
