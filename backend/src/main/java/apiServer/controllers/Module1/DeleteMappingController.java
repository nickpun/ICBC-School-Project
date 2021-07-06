package apiServer.controllers.Module1;

import apiServer.controllers.Module1.JSONRequestSpec.ReservationRequest;
import apiServer.controllers.Module1.JSONRequestSpec.SingleDayReservationRequest;
import apiServer.objectHierarchy.Greeting;
import database.ModuleOneQueries;
import database.data_objects.Reservation;
import database.data_objects.SingleDayReservation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RestController
@Controller("DeleteModuleOneController")
public class DeleteMappingController {
    // Range Implementation
//    @DeleteMapping("/reservations")
//    public ResponseEntity<String> deleteReservation(@RequestBody List<ReservationRequest> requestArray) {
//        String body = "Success";
//        HttpStatus status = HttpStatus.OK;
//
//        long day = 86400000;
//        long sec = 1000;
//
//        try {
//            for (ReservationRequest reservation: requestArray) {
//                Integer userId = reservation.getUserId();
//                Integer deskId = reservation.getDeskId();
//                Timestamp fromDate = reservation.getFromDate();
//                Timestamp toDate = reservation.getToDate();
//
//                // Truncate Hours-Minutes-Seconds-Nanos
//                fromDate.setTime((long) Math.floor((double) fromDate.getTime()/day)*day);
//                toDate.setTime((long) Math.floor((double) toDate.getTime()/day)*day + day - sec);
//
//                if (ModuleOneQueries.checkAvailability(deskId, fromDate, toDate)) {
//                    throw new SQLException("Reservation being deleted from does not exist");
//                    // This is gonna crash the program if thrown right? Maybe we should return a special error code instead
//                }
//
//                // Calculate day before and after requested reservation date, for updating records
//                Timestamp prevToDate = new Timestamp(fromDate.getTime() - sec);
//                Timestamp nextFromDate = new Timestamp(toDate.getTime() + sec);
//
//                // Find the details for the reservation this is deleting from
//                Map<String, Object> currentRes;
//                currentRes = ModuleOneQueries.checkReservationDetails(userId, deskId, fromDate, toDate);
//
//                // possible race condition here, if the blocking reservation is deleted by another request while we're here
//                if (currentRes == null) {
//                    throw new SQLException("No reservation to delete for this user");
//                }
//
//                if ((int) currentRes.get("UserId") != userId) {
//                    throw new SQLException("UserId not matched");
//                }
//                Timestamp currFromDate = (Timestamp) currentRes.get("FromDate");
//                Timestamp currToDate = (Timestamp) currentRes.get("ToDate");
//
//                // Case by case: split reservation, end reservation sooner,
//                // start reservation later, or delete altogether
//                if ((currFromDate.getTime() != fromDate.getTime()) && (currToDate.getTime() != toDate.getTime())) {
//                    ModuleOneQueries.updateReservation(deskId, currFromDate, currToDate, currFromDate, prevToDate);
//                    ModuleOneQueries.makeReservation(userId, deskId, nextFromDate, currToDate);
//
//                } else if ((currFromDate.getTime() != fromDate.getTime()) ) {
//                    ModuleOneQueries.updateReservation(deskId, currFromDate, currToDate, currFromDate, prevToDate);
//
//                } else if ((currToDate.getTime() != toDate.getTime())) {
//                    ModuleOneQueries.updateReservation(deskId, currFromDate, currToDate, nextFromDate, currToDate);
//
//                } else {
//                    ModuleOneQueries.deleteReservation(userId, deskId, fromDate, toDate);
//                }
//            }
//            return new ResponseEntity<>(body, status);
//        } catch (SQLException e) {
//            body = e.getMessage();
//            status = HttpStatus.INTERNAL_SERVER_ERROR;
//            return new ResponseEntity<>(body, status);
//        }
//    }

    // Takes an array of single-day reservation to delete
    @DeleteMapping("/reservations")
    public ResponseEntity<String> deleteReservation(@RequestBody List<SingleDayReservationRequest> requestArray) {
        String body = "Success";
        HttpStatus status = HttpStatus.OK;

        // Make sure list is non-empty before continuing
        if (requestArray.isEmpty()) {
            return new ResponseEntity<>(body, status);
        }

        long day = 86400000;
        long sec = 1000;

        List <ReservationRequest> rangeRequestArray = new ArrayList<>();
        String userId = "err";
        Integer deskId = -1;
        String fromUserId = "err";
        Integer fromDeskId = -1;
        long currDateLong = -1;
        long fromDateLong = -1;
        long toDateLong = -1;

        // Pre-processing, to merge ranges before sending to database
        for (SingleDayReservationRequest sdReservation : requestArray) {
            userId = sdReservation.getUserId();
            deskId = sdReservation.getDeskId();
            Timestamp date = sdReservation.getDate();

//            // Truncate Hours-Minutes-Seconds-Nanos
//            // Note this means both fromDate and toDate could be used as the single date here
//            currDateLong = (long) Math.floor((double) date.getTime() / day) * day;
            currDateLong = date.getTime();

            if (fromDateLong == -1) {
                // Start of a new range
                fromUserId = userId;
                fromDeskId = deskId;
                fromDateLong = currDateLong;
                toDateLong = currDateLong + day - sec;
            } else if (userId.equals(fromUserId) && deskId.equals(fromDeskId) && toDateLong + sec == currDateLong) {
                // continuation of a range
                toDateLong += day;
            }
            else {
                // end of the previous range, so record it
                rangeRequestArray.add(new ReservationRequest(
                        fromUserId,
                        fromDeskId,
                        new Timestamp(fromDateLong),
                        new Timestamp(toDateLong)
                ));
//                fromDateLong = -1;  // start over for next loop, start next range on this reservation
                fromUserId = userId;
                fromDeskId = deskId;
                fromDateLong = currDateLong;
                toDateLong = currDateLong + day - sec;
            }
        }
        // close and record last range
        rangeRequestArray.add(new ReservationRequest(
                userId,
                deskId,
                new Timestamp(fromDateLong),
                new Timestamp(currDateLong + day - sec)
        ));


        // Send each range to deletion
        try {
            for (ReservationRequest reservation: rangeRequestArray) {
                userId = reservation.getUserId();
                deskId = reservation.getDeskId();
                Timestamp fromDate = reservation.getFromDate();
                Timestamp toDate = reservation.getToDate();

//                // Truncate Hours-Minutes-Seconds-Nanos
//                fromDate.setTime((long) Math.floor((double) fromDate.getTime()/day)*day);
//                toDate.setTime((long) Math.floor((double) toDate.getTime()/day)*day + day - sec);

                if (ModuleOneQueries.checkAvailability(deskId, fromDate, toDate)) {
                    throw new SQLException("Reservation being deleted from does not exist");
                    // This is gonna crash the program if thrown right? Maybe we should return a special error code instead
                }

                // Calculate day before and after requested reservation date, for updating records
                Timestamp prevToDate = new Timestamp(fromDate.getTime() - sec);
                Timestamp nextFromDate = new Timestamp(toDate.getTime() + sec);

                // Find the details for the reservation this is deleting from
                Map<String, Object> currentRes;
                currentRes = ModuleOneQueries.checkReservationDetails(userId, deskId, fromDate, toDate);

                // possible race condition here, if the blocking reservation is deleted by another request while we're here
                if (currentRes == null) {
                    throw new SQLException("No reservation to delete for this user");
                }

                if (!currentRes.get("UserId").equals(userId)) {
                    throw new SQLException("UserId not matched");
                }
                Timestamp currFromDate = (Timestamp) currentRes.get("FromDate");
                Timestamp currToDate = (Timestamp) currentRes.get("ToDate");

                // Case by case: split reservation, end reservation sooner,
                // start reservation later, or delete altogether
                if ((currFromDate.getTime() != fromDate.getTime()) && (currToDate.getTime() != toDate.getTime())) {
                    ModuleOneQueries.updateReservation(deskId, currFromDate, currToDate, currFromDate, prevToDate);
                    ModuleOneQueries.makeReservation(userId, deskId, nextFromDate, currToDate);

                } else if ((currFromDate.getTime() != fromDate.getTime()) ) {
                    ModuleOneQueries.updateReservation(deskId, currFromDate, currToDate, currFromDate, prevToDate);

                } else if ((currToDate.getTime() != toDate.getTime())) {
                    ModuleOneQueries.updateReservation(deskId, currFromDate, currToDate, nextFromDate, currToDate);

                } else {
                    ModuleOneQueries.deleteReservation(userId, deskId, fromDate, toDate);
                }
            }
            return new ResponseEntity<>(body, status);
        } catch (SQLException e) {
            body = e.getMessage();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            return new ResponseEntity<>(body, status);
        }
    }

    @DeleteMapping("/buildings/{buildingId}")
    public ResponseEntity<String> deleteBuilding(@PathVariable(name = "buildingId") Integer buildingId) {
        String body = "Success";
        HttpStatus status = HttpStatus.OK;
        try {
            ModuleOneQueries.deleteBuilding(buildingId);
        } catch (SQLException e) {
            body = "Failure";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(body, status);
    }

    @DeleteMapping("/floors/{floorId}")
    // Deletes a floor, and all the hubs and desks associated with it
    public ResponseEntity<String> deleteFloor(@PathVariable(name = "floorId") Integer floorId) {
        String body = "Success";
        HttpStatus status = HttpStatus.OK;
        try {
            ModuleOneQueries.deleteFloor(floorId);
        } catch (SQLException e) {
            body = "Failure";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(body, status);
    }

    @DeleteMapping("/hubs/{hubId}")
    // Deletes a hub, and all the desks associated with it
    public ResponseEntity<String> deleteHub(@PathVariable(name = "hubId") Integer hubId) {
        String body = "Success";
        HttpStatus status = HttpStatus.OK;
        try {
            ModuleOneQueries.deleteHub(hubId);
        } catch (SQLException e) {
            body = "Failure";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(body, status);
    }

    @DeleteMapping("/desks")
    // Deletes an array of desks, returning an array of the ids of all desks failed to be deleted (empty if successful)
    public ResponseEntity<List<Integer>> deleteDesk(@RequestBody List<Integer> idArray) {
        HttpStatus status = HttpStatus.OK;

        ArrayList<Integer> failedIds = new ArrayList<>();
        for (Integer deskId : idArray) {
            try {
                ModuleOneQueries.deleteDesk(deskId);
            } catch (SQLException e) {
                failedIds.add(deskId);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return new ResponseEntity<>(failedIds, status);
    }
}
