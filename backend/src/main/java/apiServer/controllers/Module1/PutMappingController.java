package apiServer.controllers.Module1;

import apiServer.controllers.Module1.JSONRequestSpec.*;

import database.ModuleOneQueries;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.sql.SQLException;

@RestController
@Controller("PutModuleOneController")
public class PutMappingController {

    @PutMapping("/floors/{floorId}/plan")
    public ResponseEntity<String> addBuilding(@PathVariable Integer floorId, @RequestBody FloorplanUpdateRequest request) {
        String body = "Success";
        HttpStatus status = HttpStatus.OK;
        try {
            ModuleOneQueries.updateFloorPlan(floorId, request.getFloorPlanURL());
        } catch (SQLException e) {
            body = "Failure";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(body, status);
    }

}

// https://www.baeldung.com/spring-response-entity

