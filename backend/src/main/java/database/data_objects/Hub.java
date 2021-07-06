package database.data_objects;

import java.util.List;

public class Hub {

    int hubId;
    double xloc;
    double yloc;
    int floorId;
    boolean hasAvailableDesks;

    public Hub (int hubId, double xloc, double yloc, int floorId) {
        this.hubId = hubId;
        this.xloc = xloc;
        this.yloc = yloc;
        this.floorId = floorId;
        this.hasAvailableDesks = false;  // false by default, not affecting constructor signature for retro-compat
    }

    public int getHubId() { return hubId; }

    public double getXloc() {
        return xloc;
    }

    public double getYloc() {
        return yloc;
    }

    public int getFloorId() { return floorId; }

    public boolean getHasAvailableDesks() { return hasAvailableDesks; }

    public void setHasAvailableDesks(boolean availability) { hasAvailableDesks = availability; }
}
