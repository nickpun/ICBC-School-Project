package database;

import database.data_objects.*;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.*;
import java.util.*;

public class ModuleOneQueriesSpec {

    private static String userId = "100000";
    private static String firstName = "Bob";
    private static String lastName = "The Builder";
    private static String dept = "Software";

    private static int buildingId;
    private static String address = "4949 Rando St";
    private static String name = "A name that I just came up with";

    private static int floorId;
    private static int storey = 1;
    private static String imgUrl = "";

    private static List<Double> addXlocs = new ArrayList<>(Arrays.asList(1.0, 2.0, 3.0));
    private static List<Double> addYlocs = new ArrayList<>(Arrays.asList(4.0, 5.0, 6.0));
    private static List<Integer> listOfHubIds = null;
    private static List<Integer> hubIdList = new ArrayList<>();

    private static List<Integer> deskIds = new ArrayList<>();
    private static List<Integer> deskNos = new ArrayList<>();
    private static int deskId;

    private static Long toDateLong = System.currentTimeMillis() / 1000 * 1000 + 15465600000L;
    private static Timestamp fromDate = new Timestamp(1613779200000L);
    private static Timestamp toDate = new Timestamp(toDateLong);
    private static List<Reservation> reservationList = new ArrayList<>();

    private static List<Boolean> availabilityList = new ArrayList<>(Arrays.asList(false, true, true, true, true));

    private static Timestamp oldFromDate = new Timestamp(1611878400000L);
    private static Timestamp oldToDate = new Timestamp(1612137599000L);

    @BeforeClass
    public static void beforeAll() {

        deskNos.add(10);
        deskNos.add(11);
        deskNos.add(12);
        deskNos.add(13);
        deskNos.add(14);

        try {
            int rowsAffected = AdministrativeQueries.addEmployee(userId, firstName, lastName, dept);
            Assert.assertEquals(1, rowsAffected);
            buildingId = ModuleOneQueries.addBuilding(address, name);
            floorId = ModuleOneQueries.addFloor(buildingId, storey, "");
            listOfHubIds = ModuleOneQueries.addHubs(floorId, addXlocs, addYlocs);
            Integer hubId = listOfHubIds.get(0);
            deskIds = ModuleOneQueries.addDesks(deskNos, hubId);
            deskId = deskIds.get(0);
            ModuleOneQueries.makeReservation(userId, deskId, fromDate, toDate);
            ModuleOneQueries.makeReservation(userId, deskId, oldFromDate, oldToDate);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }
    }

    @Test
    public void testViewReservation() {

        Reservation tempReservation = new Reservation(deskId, deskNos.get(0), storey, name, fromDate, toDate);

        try {
            reservationList = ModuleOneQueries.getReservations(userId);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }

        Assert.assertEquals(tempReservation.getDeskId(), reservationList.get(0).getDeskId());
        Assert.assertEquals(tempReservation.getDeskNo(), reservationList.get(0).getDeskNo());
        Assert.assertEquals(tempReservation.getStorey(), reservationList.get(0).getStorey());
        Assert.assertEquals(tempReservation.getBuildingName(), reservationList.get(0).getBuildingName());
        Assert.assertEquals(tempReservation.getToDate().getTime(),
                reservationList.get(0).getToDate().getTime());
        Assert.assertEquals(tempReservation.getFromDate().getTime(),
                reservationList.get(0).getFromDate().getTime());

    }

    @Test
    public void testGetBuilding() {

        List<Floor> buildingFloors = new ArrayList<>();
        Building tempBuilding = new Building(buildingId, address, name, buildingFloors);


        List<Building> buildingList = null;
        try {
            buildingList = ModuleOneQueries.getBuildings();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }
        Assert.assertEquals(tempBuilding.getAddress(), buildingList.get(buildingList.size()-1).getAddress());
        Assert.assertEquals(tempBuilding.getName(), buildingList.get(buildingList.size()-1).getName());
        Assert.assertEquals(floorId, buildingList.get(buildingList.size()-1).getFloors().get(0).getFloorId());

    }

    @Test
    public void testGetHubs() {

        String query = "SELECT hub_id, floor_id, ST_X(location) as xloc, ST_Y(location) as yloc\n" +
                       "FROM Hubs\n" +
                       "WHERE hub_id = ?\n" +
                       "OR hub_id = ?\n" +
                       "OR hub_id = ?";
        PreparedStatement statement = null;
        ResultSet rs = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query);
            for (int i = 0; i < 3; i++) {
                statement.setInt( i + 1, listOfHubIds.get(i));
            }

            rs = statement.executeQuery();

            int i = 0;
            while(rs.next()) {
                Integer hubId = rs.getInt(1);
                int tempFloorId = rs.getInt(2);
                Double xloc = rs.getDouble(3);
                Double yloc = rs.getDouble(4);

                Assert.assertEquals(listOfHubIds.get(i), hubId);
                Assert.assertEquals(floorId, tempFloorId);
                Assert.assertEquals(addXlocs.get(i), xloc);
                Assert.assertEquals(addYlocs.get(i), yloc);

                hubIdList.add(hubId);
                i++;
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();

        } finally {
            try { statement.close(); } catch (Exception e) { System.out.println(e.getMessage()); }
            try { rs.close(); } catch (Exception e) { System.out.println(e.getMessage()); }
        }

    }

    @Test
    public void testGetDesk() {

        List<Desk> desks = new ArrayList<>();
        List<Desk> desks2 = new ArrayList<>();
        int hubId = listOfHubIds.get(0);

        try {
            desks = ModuleOneQueries.getDesks(hubId, fromDate, toDate);
            desks2 = ModuleOneQueries.getDesksAdmin(hubId);
            for (int i = 0; i < deskIds.size(); i++) {
                Assert.assertEquals(deskIds.get(i).intValue(), desks.get(i).getDeskId());
                Assert.assertEquals(deskIds.get(i).intValue(), desks2.get(i).getDeskId());

                Assert.assertEquals(deskNos.get(i).intValue(), desks.get(i).getDeskNo());
                Assert.assertEquals(deskNos.get(i).intValue(), desks2.get(i).getDeskNo());

                Assert.assertEquals(availabilityList.get(i), desks.get(i).getIsAvailable());

                Assert.assertEquals(hubId, desks.get(i).getHubId());
                Assert.assertEquals(hubId, desks2.get(i).getHubId());
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }

        Assert.assertEquals(deskNos.size(), desks.size());

    }

    @Test
    public void testGetUpdateFloorPlan() {

        FloorPlan floorPlan = null;

        try {
            floorPlan = ModuleOneQueries.getFloorPlan(floorId);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }

        Assert.assertEquals(floorId, floorPlan.getFloorId());
        Assert.assertEquals(listOfHubIds.size(), floorPlan.getListOfHubs().size());
        Assert.assertEquals("", floorPlan.getUrl());

        try {
            imgUrl = "https://drive.google.com/file/d/1d2s7PADqYEfcfsTiWjRaLBkWb5hp_cOb/view?usp=sharing";
            ModuleOneQueries.updateFloorPlan(floorId, imgUrl);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }

        try {
            floorPlan = ModuleOneQueries.getFloorPlan(floorId);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }

        Assert.assertEquals(imgUrl, floorPlan.getUrl());

    }

    @Test
    public void testCheckReservationFromDatePass() {
        // checkFromDate is the same value as fromDate
        Timestamp checkFromDate = new Timestamp(1613779200000L);
        Timestamp checkToDate = new Timestamp(toDateLong);
        Map<String, Object> expectedMap = new HashMap<>();
        Map<String, Object> returnedMap;

        expectedMap.put("UserId", userId);
        expectedMap.put("DeskId", deskId);
        expectedMap.put("FromDate", new Timestamp(fromDate.getTime()));
        expectedMap.put("ToDate", new Timestamp(toDate.getTime()));

        try {
            returnedMap = ModuleOneQueries.checkReservationDetails(userId, deskId, checkFromDate, checkToDate);

            Assert.assertNotEquals(null, returnedMap);
            Assert.assertEquals(expectedMap.get("UserId"), returnedMap.get("UserId"));
            Assert.assertEquals(expectedMap.get("DeskId"), returnedMap.get("DeskId"));
            Assert.assertEquals(expectedMap.get("FromDate"), returnedMap.get("FromDate"));
            Assert.assertEquals(expectedMap.get("ToDate"), returnedMap.get("ToDate"));
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }
    }

    @Test
    public void testCheckReservationFromDateFail() {
        // checkFromDate is 1 sec before fromDate used to create the Reservation
        Timestamp checkFromDate = new Timestamp(1613779199000L);
        Timestamp checkToDate = new Timestamp(1613865599000L);
        Map<String, Object> returnedMap;

        try {
            returnedMap = ModuleOneQueries.checkReservationDetails(userId, deskId, checkFromDate, checkToDate);

            Assert.assertNull(returnedMap);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }
    }

    @Test
    public void testCheckReservationToDatePass() {
        // checkToDate is the same value as toDate
        Timestamp checkFromDate = new Timestamp(1613952000000L);
        Timestamp checkToDate = new Timestamp(1614038399000L);
        Map<String, Object> expectedMap = new HashMap<>();
        Map<String, Object> returnedMap;

        expectedMap.put("UserId", userId);
        expectedMap.put("DeskId", deskId);
        expectedMap.put("FromDate", new Timestamp(fromDate.getTime()));
        expectedMap.put("ToDate", new Timestamp(toDate.getTime()));

        try {
            returnedMap = ModuleOneQueries.checkReservationDetails(userId, deskId, checkFromDate, checkToDate);

            Assert.assertNotEquals(null, returnedMap);
            Assert.assertEquals(expectedMap.get("UserId"), returnedMap.get("UserId"));
            Assert.assertEquals(expectedMap.get("DeskId"), returnedMap.get("DeskId"));
            Assert.assertEquals(expectedMap.get("FromDate"), returnedMap.get("FromDate"));
            Assert.assertEquals(expectedMap.get("ToDate"), returnedMap.get("ToDate"));
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }
    }

    @Test
    public void testCheckReservationToDateFail() {
        // checkToDate is 1 sec past toDate used to create the Reservation
        Timestamp checkFromDate = new Timestamp(1613952000000L);
        Timestamp checkToDate = new Timestamp(1635473735000L);
        Map<String, Object> returnedMap;

        try {
            returnedMap = ModuleOneQueries.checkReservationDetails(userId, deskId, checkFromDate, checkToDate);

            Assert.assertNull(returnedMap);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }
    }

    @Test
    public void testUpdateReservation() {
        Timestamp updateFromDate = new Timestamp(1613692800000L);
        Timestamp updateToDate = new Timestamp(1614124799000L);

        try {
            int rowsAffected = ModuleOneQueries.updateReservation(deskId, fromDate, toDate, updateFromDate, updateToDate);
            Assert.assertEquals(1, rowsAffected);

            rowsAffected = ModuleOneQueries.updateReservation(deskId, updateFromDate, updateToDate, fromDate, toDate);
            Assert.assertEquals(1, rowsAffected);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }

    }

    @Test
    public void testUpdateNonExistentReservation() {
        Timestamp updateFromDate = new Timestamp(1613692800000L);
        Timestamp updateToDate = new Timestamp(1614124799000L);
        Timestamp fakeFromDate = new Timestamp(1613606400000L);
        Timestamp fakeToDate = new Timestamp(1614211199000L);

        try {
            int rowsAffected = ModuleOneQueries.updateReservation(deskId, fakeFromDate, fakeToDate, updateFromDate, updateToDate);
            Assert.assertEquals(0, rowsAffected);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }
    }

    @Test
    public void testGetOldReservation() {
        // tests that old Reservations do not get returned
        Map<String, Object> returnedMap;

        try {

            returnedMap = ModuleOneQueries.checkReservationDetails(userId, deskId, oldFromDate, oldToDate);
            Assert.assertNull(returnedMap);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();        }
    }

    @AfterClass
    public static void afterClass() {

        try {
            int oldReservationRowsAffected = ModuleOneQueries.deleteReservation(userId, deskId, oldFromDate, oldToDate);
            Assert.assertEquals(1, oldReservationRowsAffected);

            int reservationRowsAffected = ModuleOneQueries.deleteReservation(userId, deskId, fromDate, toDate);
            Assert.assertEquals(1, reservationRowsAffected);

            int deskRowsAffected = ModuleOneQueries.deleteDesk(deskId);
            Assert.assertEquals(1, deskRowsAffected);

            int hubId = listOfHubIds.get(0);
            int hubRowsAffected = ModuleOneQueries.deleteHub(hubId);
            Assert.assertEquals(1, hubRowsAffected);

            int floorRowsAffected = ModuleOneQueries.deleteFloor(floorId);
            Assert.assertEquals(1, floorRowsAffected);

            int buildingRowsAffected = ModuleOneQueries.deleteBuilding(buildingId);
            Assert.assertEquals(1, buildingRowsAffected);

            int employeeRowsAffected = AdministrativeQueries.deleteEmployee(userId);
            Assert.assertEquals(1, employeeRowsAffected);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }
    }

}