package database;

import database.data_objects.*;

import java.sql.*;
import java.util.*;

public class ModuleOneQueries {

    /*
    * Method to create a new row in the Reservation table
    * @param userId: Id of the user who is making the Reservation
    * @param deskid: Id of the desk which the user is reserving
    * @param fromDate: Timestamp of when the Reservation begins
    * @param toDate: Timestamp of when the Reservation ends
    * @return Integer: Number of rows affected (1 if the Reservation is made)
    * @exception SQLException: Failure to connect to database or query fails
    */
    public static Integer makeReservation(String userId, Integer deskId, Timestamp fromDate, Timestamp toDate) throws SQLException {
        String query = "INSERT INTO Reservations\n" +
                       "VALUES (?, ?, ?, ?)";
        PreparedStatement statement = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, userId);
            statement.setInt(2, deskId);
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            statement.setTimestamp(3, fromDate, calendar);
            statement.setTimestamp(4, toDate, calendar);
            return statement.executeUpdate();

        } finally {
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
     * Method to delete a row from the Reservation table
     * @param userId: Id of the user whose Reservation is being deleted
     * @param deskid: Id of the desk of which is being deleted
     * @param fromDate: Timestamp of when the Reservation begins
     * @param toDate: Timestamp of when the Reservation ends
     * @return Integer: Number of rows affected (1 if the Reservation is deleted)
     * @exception SQLException: Failure to connect to database or query fails
     */
    public static Integer deleteReservation(String userId, Integer deskId, Timestamp fromDate, Timestamp toDate) throws SQLException {
        String query = "DELETE FROM Reservations\n" +
                       "WHERE user_id = ?\n" +
                       "AND desk_id = ?\n" +
                       "AND from_date = ?\n" +
                       "AND to_date = ?";
        PreparedStatement statement = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query);
            statement.setString(1, userId);
            statement.setInt(2, deskId);
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            statement.setTimestamp(3, fromDate, calendar);
            statement.setTimestamp(4, toDate, calendar);
            return statement.executeUpdate();

        } finally {
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
     * Method to get the Reservation row that is tied to the user and desk id and is contained within the given timestamps
     * @param userId: Id of the user whose Reservation is being searched for
     * @param deskid: Id of the desk of which is being searched for
     * @param fromDate: Timestamp of when the Reservation begins
     * @param toDate: Timestamp of when the Reservation ends
     * @return Map<String, Object>: A map containing the details of the found Reservation; returns null if not found
     * @exception SQLException: Failure to connect to database or query fails
     */
    public static Map<String, Object> checkReservationDetails(String userId, Integer deskId, Timestamp fromDate, Timestamp toDate) throws SQLException {
        String query = "SELECT * FROM Reservations\n" +
                       "WHERE user_id = ?\n" +
                       "AND desk_id = ?\n" +
                       "AND from_date <= ?\n" +
                       "AND to_date >= ?" +
                       "AND to_date >= CURRENT_TIMESTAMP";
        PreparedStatement statement = null;
        ResultSet rs = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()){
            statement = connection.prepareStatement(query);
            statement.setString(1, userId);
            statement.setInt(2, deskId);
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            statement.setTimestamp(3, fromDate, calendar);
            statement.setTimestamp(4, toDate, calendar);
            rs = statement.executeQuery();

            if (!rs.isBeforeFirst()) {
                return null;
            }

            rs.next();
            String getUserId = rs.getString(1);
            int getDeskId = rs.getInt(2);
            Timestamp getFromDate = rs.getTimestamp(3, calendar);
            Timestamp getToDate = rs.getTimestamp(4, calendar);

            Map<String, Object> reservationMap = new HashMap<>();
            reservationMap.put("UserId", getUserId);
            reservationMap.put("DeskId", getDeskId);
            reservationMap.put("FromDate", getFromDate);
            reservationMap.put("ToDate", getToDate);

            return reservationMap;

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { System.err.println(e.getMessage()); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
     * Method to update an existing Reservation
     * @param userId: Id of the user whose Reservation is being updated
     * @param deskid: id of the desk of which is being updated
     * @param fromDate: Timestamp of when the Reservation begins
     * @param toDate: Timestamp of when the Reservation ends
     * @return Integer: Number of rows affected (1 if the Reservation is updated, 0 if no updates made)
     * @exception SQLException: Failure to connect to database or query fails
     */
    public static Integer updateReservation(int deskId, Timestamp oldFromDate, Timestamp oldToDate, Timestamp newFromDate, Timestamp newToDate) throws SQLException {
        String query = "UPDATE Reservations\n" +
                       "SET from_date = ?,\n" +
                       "    to_date = ?\n" +
                       "WHERE desk_id = ?\n" +
                       "AND from_date = ?\n" +
                       "AND to_date = ?";
        PreparedStatement statement = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query);
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            statement.setTimestamp(1, newFromDate, calendar);
            statement.setTimestamp(2, newToDate, calendar);
            statement.setInt(3, deskId);
            statement.setTimestamp(4, oldFromDate, calendar);
            statement.setTimestamp(5, oldToDate, calendar);
            return statement.executeUpdate();

        } finally {
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
     * Method to get all existing Reservations for a certain user
     * @param userId: Id of the user requesting for their Reservations
     * @return List<Reservation>: a list of Reservations that belongs to the user
     * @exception SQLException: Failure to connect to database or query fails
     */
    public static List<Reservation> getReservations(String userId) throws SQLException {
        String query = "SELECT R.desk_id, D.desk_no, F.storey, B.building_name, R.from_date, R.to_date\n" +
                       "FROM Reservations R, Desks D, Floors F, Buildings B, Hubs H\n" +
                       "WHERE R.user_id = ? \n" +
                       "AND R.desk_id = D.desk_id \n" +
                       "AND D.hub_id = H.hub_id \n" +
                       "AND H.floor_id = F.floor_id \n" +
                       "AND F.building_id = B.building_id\n" +
                       "AND R.to_date >= ?\n" +
                       "AND R.to_date < DATE_ADD(?, INTERVAL 6 MONTH)";
        PreparedStatement statement = null;
        ResultSet rs = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query);
            statement.setString(1, userId);
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()), calendar);
            statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()), calendar);
            rs = statement.executeQuery();

            List<Reservation> reservationsList = new ArrayList<>();
            while(rs.next()) {
                int deskId = rs.getInt(1);
                int deskNo = rs.getInt(2);
                int storey = rs.getInt(3);
                String buildingName = rs.getString(4);
                calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                Timestamp fromDate = rs.getTimestamp(5, calendar);
                Timestamp toDate = rs.getTimestamp(6, calendar);

                Reservation tempReservation = new Reservation(deskId, deskNo, storey, buildingName, fromDate, toDate);
                reservationsList.add(tempReservation);
            }

            return reservationsList;

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { System.err.println(e.getMessage()); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
     * Method to get all existing Buildings in the database
     * @return List<Building>: a list of all Buildings that exist in the database
     * @exception SQLException: Failure to connect to database or query fails
     */
    public static List<Building> getBuildings() throws SQLException {
        String query = "SELECT * FROM Buildings";

        PreparedStatement statement = null;
        ResultSet rs = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query);
            rs = statement.executeQuery();

            List<Building> buildingList = new ArrayList<>();
            while (rs.next()) {
                int buildingId = rs.getInt(1);
                String buildingAddress = rs.getString(2);
                String buildingName = rs.getString(3);
                List<Floor> floorList = getFloors(buildingId);

                Building building = new Building(buildingId, buildingAddress, buildingName, floorList);
                buildingList.add(building);
            }

            return buildingList;

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { System.err.println(e.getMessage()); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }

    }

    /*
     * Method to get all Floors tied to a certain Building
     * @param buildingId: the id of the Building
     * @return List<Floor>: a list of all Floors that is tied to the buildingId
     * @exception SQLException: Failure to connect to database or query fails
     */
    private static List<Floor> getFloors(int buildingId) throws SQLException {
        String query = "SELECT * FROM Floors WHERE building_id = " + buildingId;

        PreparedStatement statement = null;
        ResultSet rs = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query);
            rs = statement.executeQuery();

            List<Floor> floorList = new ArrayList<>();
            while (rs.next()) {
                int floorId = rs.getInt(1);
                int storey = rs.getInt(3);

                Floor floor = new Floor(floorId, storey, buildingId);
                floorList.add(floor);
            }

            return floorList;

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { System.err.println(e.getMessage()); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }

    }

    /*
     * Method to add a Building to the database
     * @param address: the address of the Building
     * @param buildingName: the name of the Building
     * @return Integer: the auto-incremented id of the newly created Building
     * @exception SQLException: Failure to connect to database or query fails
     */
    public static Integer addBuilding(String address, String buildingName) throws SQLException {
        String query = "INSERT INTO Buildings (address, building_name)\n" +
                       "VALUES (?, ?)";
        PreparedStatement statement = null;
        ResultSet rs = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, address);
            statement.setString(2, buildingName);
            statement.executeUpdate();

            rs = statement.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { System.err.println(e.getMessage()); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
     * Method to add a Floor to the database
     * @param buildingId: the id of the Building of which to add the Floor to
     * @param storey: the storey of the Floor
     * @param floorPlan: an InputStream of the floor plan's image
     * @return Integer: the auto-incremented id of the newly created Floor
     * @exception SQLException: Failure to connect to database or query fails
     */
    public static Integer addFloor(int buildingId, int storey, String floorPlan) throws SQLException {
        String query = "INSERT INTO Floors (building_id, storey, floor_plan)\n" +
                       "VALUES (?, ?, ?)";

        PreparedStatement statement = null;
        ResultSet rs = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, buildingId);
            statement.setInt(2, storey);
            statement.setString(3, floorPlan);
            statement.executeUpdate();

            rs = statement.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { System.err.println(e.getMessage()); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
     * Method to add a list of Hubs to the database
     * @param floorId: the id of the Floor of which to add the Hubs to
     * @param xlocs: a list of x locations of which the Hub was added to the floor plan image
     * @param ylocs: a list of y locations of which the Hub was added to the floor plan image
     * @return List<Integer>: a list of auto-incremented ids of the newly created Hubs
     * @exception SQLException: Failure to connect to database or query fails
     */
    public static List<Integer> addHubs(int floorId, List<Double> xlocs, List<Double> ylocs) throws SQLException {
        String query = "INSERT INTO Hubs (floor_id, location)\n" +
                       "VALUES (?, Point(?, ?))";

        PreparedStatement statement = null;
        ResultSet rs = null;
        List<Integer> listOfHubIds = new ArrayList<>();

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, floorId);

            for (int i = 0; i < xlocs.size(); i++) {
                statement.setDouble(2, xlocs.get(i));
                statement.setDouble(3, ylocs.get(i));
                statement.executeUpdate();

                rs = statement.getGeneratedKeys();
                rs.next();
                listOfHubIds.add(rs.getInt(1));
            }

            return listOfHubIds;

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { System.err.println(e.getMessage()); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
     * Method to add a list of Desks to the database
     * @param deskNos: a list of desk numbers declared by the admin to label the Desks with
     * @param hubId: the id of the Hub of which to add the Desks to
     * @return List<Integer>: a list of auto-incremented ids of the newly created Hubs
     * @exception SQLException: Failure to connect to database or query fails
     */
    public static List<Integer> addDesks(List<Integer> deskNos, int hubId) throws SQLException {
        String query = "INSERT INTO Desks (desk_no, hub_id)\n" +
                       "VALUES (?, ?)";
        PreparedStatement statement = null;
        ResultSet rs = null;
        List<Integer> listOfDeskIds = new ArrayList<>();

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(2, hubId);

            for (int deskNo : deskNos) {
                statement.setInt(1, deskNo);
                statement.executeUpdate();

                rs = statement.getGeneratedKeys();
                rs.next();
                listOfDeskIds.add(rs.getInt(1));
            }

            return listOfDeskIds;

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { System.err.println(e.getMessage()); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
    * This method will delete a Building
    * It will cascade and delete all corresponding Floors, Hubs and Desks
    * @param buildingId: The ID of the building that is to be deleted
    * @return int: The number of rows affected
    * @Exception SQLException: Failure to connect to database or query fails
    */
    public static int deleteBuilding(int buildingId) throws SQLException {
        String query = "DELETE FROM Buildings\n" +
                       "WHERE building_id = ?";
        PreparedStatement statement = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, buildingId);
            return statement.executeUpdate();

        } finally {
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
     * This method will delete a Floor
     * It will cascade and delete all corresponding Hubs and Desks
     * @param floorId: The ID of the Floor that is to be deleted
     * @return int: The number of rows affected
     * @Exception SQLException: Failure to connect to database or query fails
     */
    public static int deleteFloor(int floorId) throws SQLException {
        String query = "DELETE FROM Floors\n" +
                       "WHERE floor_id = ?";
        PreparedStatement statement = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, floorId);
            return statement.executeUpdate();

        } finally {
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
    * This is the method that will delete a Hub
    * @param hubId: the ID of the hub that is to be deleted
    * @return int: The number of rows affected
    * @Exception SQLException: Failure to connect to database or query fails
    */
    public static int deleteHub(int hubId) throws SQLException {
        String query = "DELETE FROM Hubs\n" +
                       "WHERE hub_id = ?";
        PreparedStatement statement = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, hubId);
            return statement.executeUpdate();

        } finally {
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
     * This is the method that will delete a Desk
     * @param hubId: the ID of the Desk that is to be deleted
     * @return int: The number of rows affected
     * @Exception SQLException: Failure to connect to database or query fails
     */
    public static int deleteDesk(int deskId) throws SQLException {
        String query = "DELETE FROM Desks\n" +
                       "WHERE desk_id = ?";
        PreparedStatement statement = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, deskId);
            return statement.executeUpdate();

        } finally {
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
    * This is the method that updates the floor_plan column of a floor
    * @param floorId: The ID of the floor in question
    * @param floorPlan: The InputStream objects that will be converted into a blob using
    *                   statement.setBlob()
    * @return: Nothing
    * Exception SQL Exception: Failure to connect to database or query fails
    */
    public static Integer updateFloorPlan(int floorId, String floorPlan) throws SQLException {
        String query = "UPDATE Floors\n" +
                       "SET floor_plan = ?\n" +
                       "WHERE floor_id = ?";
        PreparedStatement statement = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()){
            statement = connection.prepareStatement(query);
            statement.setString(1, floorPlan);
            statement.setInt(2, floorId);
            return statement.executeUpdate();

        } finally {
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
    * This is the method that retrieves a FloorPlan object from a given floor
    * @param floorId: The ID of the floor in question
    * @return FloorPlan: a FloorPlan object. See data_objects.FloorPlan for more information
    * @exception SQLException: Failure to connect to database or query fails
    */
    public static FloorPlan getFloorPlan(int floorId) throws SQLException {
        String query = "SELECT floor_plan\n" +
                       "FROM Floors\n" +
                       "WHERE floor_id = ?";
        PreparedStatement statement = null;
        ResultSet rs = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query);
            statement.setInt(1, floorId);
            rs = statement.executeQuery();

            rs.next();
            List<Hub> hubs = getHubs(floorId);
            String url = rs.getString(1);

            return new FloorPlan(floorId, url, hubs);

        } finally {
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
            try { if (rs != null) rs.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
    * This is the method that returns a list of hubs in a given floor
    * @param floorId: The ID of the floor in question
    * @return List<Hub>: A list of hubs
    * @exception SQLException: Failure to connect to database or query fails
    */
    private static List<Hub> getHubs(int floorId) throws SQLException {
        String query = "SELECT hub_id, ST_X(location) as xloc, ST_Y(location) as yloc\n" +
                       "FROM Hubs\n" +
                       "WHERE floor_id = ?";
        PreparedStatement statement = null;
        ResultSet rs = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query);
            statement.setInt(1, floorId);
            rs = statement.executeQuery();

            List<Hub> hubs = new ArrayList<>();
            while(rs.next()) {
                int hubId = rs.getInt(1);
                double xloc = rs.getDouble(2);
                double yloc = rs.getDouble(3);
                hubs.add(new Hub(hubId, xloc, yloc, floorId));
            }

            return hubs;

        } finally {
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
            try { if (rs != null) rs.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
    * This is the method that returns a list of desks within a given hub. The list of Desks returned
    * includes a isAvailability field for each desk that indicates whether a given desk is available
    * during a given time period
    * @param hubId: The ID of the hub in question
    * @fromDate: The beginning date of the time period
    * @param toDate: The end date of the time period
    * @return List<Desk>: A list of desks
    * @exception SQLException: Failure to connect to database or query fails
    */
    public static List<Desk> getDesks(int hubId, Timestamp fromDate, Timestamp toDate) throws SQLException {
        String query = "SELECT desk_id, desk_no\n" +
                       "FROM Desks\n" +
                       "WHERE hub_id = ?";
        PreparedStatement statement = null;
        ResultSet rs = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query);
            statement.setInt(1, hubId);
            rs = statement.executeQuery();

            List<Desk> desks = new ArrayList<>();
            while(rs.next()) {
                int deskId = rs.getInt(1);
                int deskNo = rs.getInt(2);
                boolean isAvailable = checkAvailability(deskId, fromDate, toDate);
                desks.add(new Desk(deskId, deskNo, isAvailable, hubId));
            }

            return desks;

        } finally {
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
            try { if (rs != null) rs.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
     * This is the method that returns a list of desks within a given hub. Will be used in the admin tab.
     * @param hubId: The ID of the hub in question
     * @return List<Desk>: A list of desks tied to the given Hub
     * @exception SQLException: Failure to connect to database or query fails
     */
    public static List<Desk> getDesksAdmin(int hubId) throws SQLException {
        String query = "SELECT desk_id, desk_no\n" +
                       "FROM Desks\n" +
                       "WHERE hub_id = ?";
        PreparedStatement statement = null;
        ResultSet rs = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query);
            statement.setInt(1, hubId);
            rs = statement.executeQuery();

            List<Desk> desks = new ArrayList<>();
            while(rs.next()) {
                int deskId = rs.getInt(1);
                int deskNo = rs.getInt(2);
                desks.add(new Desk(deskId, deskNo, false, hubId));
            }

            return desks;

        } finally {
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
            try { if (rs != null) rs.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

    /*
    * This is the method that checks whether a given desk is available in the given time period
    * @param deskId: The ID of the desk in question
    * @param fromDate: The beginning date of the time period
    * @param toDate: The end date of the time period
    * @return boolean: Whether or not the desk is available in the given time period
    * @exception SQLException: Failure to connect to database or query fails
    */
    public static boolean checkAvailability(int deskId, Timestamp fromDate, Timestamp toDate) throws SQLException {
        String query = "SELECT from_date, to_date\n" +
                       "FROM Reservations\n" +
                       "WHERE desk_id = ?\n" +
                       "AND (\n" +
                       "(from_date <= ? AND to_date >= ?)\n" +
                       "OR (from_date <= ? AND to_date >= ?)\n" +
                       "OR (from_date >= ? AND to_date <= ?))";
        PreparedStatement statement = null;
        ResultSet rs = null;

        try (Connection connection = DatabaseConnectorPool.getConnection()) {
            statement = connection.prepareStatement(query);
            statement.setInt(1, deskId);
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            statement.setTimestamp(2, fromDate, calendar);
            statement.setTimestamp(3, fromDate, calendar);
            statement.setTimestamp(4, toDate, calendar);
            statement.setTimestamp(5, toDate, calendar);
            statement.setTimestamp(6, fromDate, calendar);
            statement.setTimestamp(7, toDate, calendar);
            rs = statement.executeQuery();

            return !rs.isBeforeFirst();

        } finally {
            try { if (statement != null) statement.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
            try { if (rs != null) rs.close(); } catch (SQLException e) { System.err.println(e.getMessage()); }
        }
    }

}