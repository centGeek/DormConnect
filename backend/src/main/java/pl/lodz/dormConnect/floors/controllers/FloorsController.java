package pl.lodz.dormConnect.floors.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.dormConnect.floors.entity.FloorEntity;
import pl.lodz.dormConnect.floors.service.FloorsService;

import java.util.List;


@RestController
@RequestMapping("/api/floors")
public class FloorsController {
    private final FloorsService floorsService;

    public FloorsController(FloorsService floorsService) {
        this.floorsService = floorsService;
    }

    @GetMapping("/get")
    public ResponseEntity<List<Integer>> getFloors(){
        List<Integer> floors = floorsService.getFloors();
        return ResponseEntity.ok(floors);
    }

    @PostMapping("/add")
    public ResponseEntity<FloorEntity> addFloor() {
        return ResponseEntity.status(HttpStatus.CREATED).body(floorsService.addFloor());
    }

}
