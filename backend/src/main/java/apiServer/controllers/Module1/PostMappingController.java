package apiServer.controllers.Module1;

import apiServer.controllers.Module1.JSONRequestSpec.*;
import database.ModuleOneQueries;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;

@RestController
@Controller("PostModuleOneController")
public class PostMappingController {

    @PostMapping("/reservations")
    public synchronized ResponseEntity<String> addReservation(@RequestBody ReservationRequest request) {
        // Extract data
        String userId = request.getUserId();
        int deskId = request.getDeskId();
        Timestamp fromDate = request.getFromDate();
        Timestamp toDate = request.getToDate();

        long day = 86400000;
        long sec = 1000;

        // Truncate Hours-Minutes-Seconds-Nanos


//        fromDate.setTime((long) Math.floor((double) fromDate.getTime()/day)*day);
//
//        toDate.setTime((long) Math.floor((double) toDate.getTime()/day)*day + day - sec);


        // Initialize Response Entity with failure
        String body = "Failure";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        // Check if requested reservation date is available
        try {
            if (ModuleOneQueries.checkAvailability(deskId, fromDate, toDate)) {
                // Calculate day before and after requested reservation date
                Timestamp prevFromDate = new Timestamp(fromDate.getTime() - day);

                Timestamp prevToDate = new Timestamp(fromDate.getTime() - sec);
                Timestamp nextFromDate = new Timestamp(toDate.getTime() + sec);

                Timestamp nextToDate = new Timestamp(toDate.getTime() + day);

                // Add new reservation based on neighbouring dates
                try {
                    // Get reservations for neighbouring dates if they exist
                    Map<String, Object> prevRes;
                    Map<String, Object> nextRes;
                    prevRes = ModuleOneQueries.checkReservationDetails(userId, deskId, prevFromDate, prevToDate);
                    nextRes = ModuleOneQueries.checkReservationDetails(userId, deskId, nextFromDate, nextToDate);

                    // If neighbouring dates are reserved, use those reservations' dates in new reservation
                    if ((prevRes != null) && (nextRes != null)) {
                        fromDate = (Timestamp) prevRes.get("FromDate");
                        toDate = (Timestamp) nextRes.get("ToDate");
                        Timestamp oldFromDate = (Timestamp) nextRes.get("FromDate");
                        Timestamp oldToDate = (Timestamp) prevRes.get("ToDate");
                        ModuleOneQueries.updateReservation(deskId, fromDate, oldToDate, fromDate, toDate);
                        ModuleOneQueries.deleteReservation(userId, deskId, oldFromDate, toDate);

                    } else if (prevRes != null) {
                        fromDate = (Timestamp) prevRes.get("FromDate");
                        Timestamp oldToDate = (Timestamp) prevRes.get("ToDate");
                        ModuleOneQueries.updateReservation(deskId, fromDate, oldToDate, fromDate, toDate);

                    } else if (nextRes != null) {
                        toDate = (Timestamp) nextRes.get("ToDate");
                        Timestamp oldFromDate = (Timestamp) nextRes.get("FromDate");
                        ModuleOneQueries.updateReservation(deskId, oldFromDate, toDate, fromDate, toDate);

                    } else {
                        ModuleOneQueries.makeReservation(userId, deskId, fromDate, toDate);
                    }

                    // Change Response Entity to success
                    body = "Success";
                    status = HttpStatus.OK;

                } catch (SQLException e) {
                    // Failed - Internal Server Error
                }
            } else {
                // Requested reservation date is unavailable
                body = "Reservation unavailable";
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        } catch (SQLException e) {
            // Failed - Internal Server Error
        }
        return new ResponseEntity<>(body, status);
    }

    @PostMapping("/buildings")
    public ResponseEntity<String> addBuilding(@RequestBody BuildingRequest request) {
        String body = "Success";
        HttpStatus status = HttpStatus.OK;
        try {
            ModuleOneQueries.addBuilding(request.getAddress(), request.getBuildingName());
        } catch (SQLException e) {
            body = "Failure";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(body, status);
    }

    @PostMapping("/floors")
    public ResponseEntity<String> addFloor(@RequestBody FloorRequest request) {
        String body = "Success";
        HttpStatus status = HttpStatus.OK;
        try {
            ModuleOneQueries.addFloor(request.getBuildingId(), request.getStorey(), request.getFloorPlanURL());
        } catch (SQLException e) {
            body = "Failure";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(body, status);
    }

    @PostMapping("/hubs")
    public ResponseEntity<String> addHubs(@RequestBody HubsRequest request) {
        String body = "Success";
        HttpStatus status = HttpStatus.OK;
        try {
            ModuleOneQueries.addHubs(request.getFloorId(), request.getXlocs(), request.getYlocs());
        } catch (SQLException e) {
            body = "Failure";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(body, status);
    }

    @PostMapping("/desks")
    public ResponseEntity<String> addDesks(@RequestBody DesksRequest request) {
        String body = "Success";
        HttpStatus status = HttpStatus.OK;
        try {
            ModuleOneQueries.addDesks(request.getDeskNos(), request.getHubId());
        } catch (SQLException e) {
            body = "Failure";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(body, status);
    }

}

// https://www.baeldung.com/spring-response-entity
