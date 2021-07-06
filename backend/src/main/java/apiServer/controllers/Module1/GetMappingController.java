package apiServer.controllers.Module1;

import apiServer.DateTimeUtil;
import apiServer.MapCreator;

import database.data_objects.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import database.ModuleOneQueries;

import javax.swing.*;
import java.sql.SQLException;
import java.sql.Timestamp;


import java.util.*;

@RestController
@Controller("GetModuleOneController")
public class GetMappingController {


    @GetMapping("/reservations/{id}")
    // fetches list of all upcoming reservations for this user in the next 6 months,
    // as an array of single day reservations
    public ResponseEntity< Map<String, Object> > getReservation(@PathVariable String id) {
        try {
            List<Reservation> data = ModuleOneQueries.getReservations(id);


            long day = 86400000;
            // before returning, split apart all reservations
            List<SingleDayReservation> splitData = new ArrayList<>();
            // timezone here doesn't matter actually, switching to work with underlying long
            long currDateLong = java.util.Calendar.getInstance(TimeZone.getTimeZone("America/Vancouver")).getTimeInMillis();

            // Necessary offset to work with stored data as designed
            long timeZoneOffset = TimeZone.getTimeZone("America/Vancouver").getOffset(currDateLong);
            currDateLong = currDateLong + timeZoneOffset;
            for (Reservation reservation : data) {
                long fromDateLong = reservation.getFromDate().getTime();
                long toDateLong = reservation.getToDate().getTime();

                // one day at a time, create SingleDayReservations for each day in the range
                for (long iterDateLong = fromDateLong; iterDateLong < toDateLong; iterDateLong += day) {
                    // guarding against older parts of reservations, add a day in case we're partway through the day of the reservation
                    if (iterDateLong + day >= currDateLong) {  // Note: pretends we're comparing PST times
                        splitData.add(new SingleDayReservation(
                                reservation.getDeskId(),
                                reservation.getDeskNo(),
                                reservation.getStorey(),
                                reservation.getBuildingName(),
                                new Timestamp(iterDateLong)));
                    }
                }
            }
            return new ResponseEntity<>(    MapCreator.putInMap("data", splitData),
                    HttpStatus.OK   );
        } catch (SQLException e) {
            return new ResponseEntity<>(    null,
                                            HttpStatus.INTERNAL_SERVER_ERROR   );
        }
    }

    @GetMapping("/buildings")
    // fetches list of all buildings
    public ResponseEntity< Map<String, Object> > getBuildings() {
        try {
            List<Building> data = ModuleOneQueries.getBuildings();
            return new ResponseEntity<>(    MapCreator.putInMap("data", data),
                                            HttpStatus.OK   );
        } catch (SQLException e) {
            return new ResponseEntity<>(    null,
                                            HttpStatus.INTERNAL_SERVER_ERROR   );
        }
    }

    @GetMapping("/floors")
    // fetches the URL storing a floorplan for a particular floor, as well as a list of all Hubs on the floor,
    // along with whether they have any desks fully available to book for a given date range
    public ResponseEntity< Map<String, Object> > getFloorPlan(@RequestParam ("floorId") Integer floorId,
                                                              @RequestParam("fromDate") String fromDate,
                                                              @RequestParam("toDate") String toDate) {
        try {
            Timestamp tsFromDate = DateTimeUtil.getTimestamp(fromDate);
            Timestamp tsToDate = DateTimeUtil.getTimestamp(toDate);
            FloorPlan data = ModuleOneQueries.getFloorPlan(floorId);
            for (Hub hub: data.getListOfHubs()) {
                List<Desk> desks = ModuleOneQueries.getDesks(hub.getHubId(), tsFromDate, tsToDate);
                for (Desk desk: desks) {
                    if (desk.getIsAvailable()) {
                        hub.setHasAvailableDesks(true);
                        break;
                    }
                }
            }
            return new ResponseEntity<>(    MapCreator.putInMap("data", data),
                                            HttpStatus.OK   );
        } catch (SQLException e) {
            return new ResponseEntity<>(    null,
                                            HttpStatus.INTERNAL_SERVER_ERROR   );
        }
    }

    @GetMapping("/floors/admin/{floorId}")
    // fetches the URL storing a floorplan for a particular floor, as well as a list of all Hubs on the floor,
    public ResponseEntity< Map<String, Object> > getFloorPlanAdmin(@PathVariable Integer floorId) {
        try {
            FloorPlan data = ModuleOneQueries.getFloorPlan(floorId);
            return new ResponseEntity<>(    MapCreator.putInMap("data", data),
                    HttpStatus.OK   );
        } catch (SQLException e) {
            return new ResponseEntity<>(    null,
                    HttpStatus.INTERNAL_SERVER_ERROR   );
        }
    }

    @GetMapping("/desks/")
    // fetches list of all desks in a given hub, and whether they are fully available for a given time period
    public ResponseEntity< Map<String, Object> > getDesks(@RequestParam("hubId") Integer hubId,
                                                          @RequestParam("fromDate") String fromDate,
                                                          @RequestParam("toDate") String toDate) {
        try {
            Timestamp tsFromDate = DateTimeUtil.getTimestamp(fromDate);
            Timestamp tsToDate = DateTimeUtil.getTimestamp(toDate);

            List<Desk> data = ModuleOneQueries.getDesks(hubId, tsFromDate, tsToDate);
                return new ResponseEntity<>(    MapCreator.putInMap("data", data),
                                                HttpStatus.OK   );
        } catch (Exception e) {
            return new ResponseEntity<>(    null,
                                            HttpStatus.INTERNAL_SERVER_ERROR   );
        }
    }

    @GetMapping("/desks/admin/{hubId}")
    // fetches list of all desks in a hub, regardless of availability,
    // for floor editing purposes
    public ResponseEntity< Map<String, Object> > getDesksAdmin(@PathVariable Integer hubId) {
        try {
            List<Desk> data = ModuleOneQueries.getDesksAdmin(hubId);
            return new ResponseEntity<>(    MapCreator.putInMap("data", data),
                                            HttpStatus.OK   );
        }
        catch (Exception e) {
            return new ResponseEntity<>(    null,
                    HttpStatus.INTERNAL_SERVER_ERROR   );
        }
    }
}


//https://www.baeldung.com/spring-boot-json
