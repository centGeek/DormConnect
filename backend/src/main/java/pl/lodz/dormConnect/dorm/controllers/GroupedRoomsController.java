package pl.lodz.dormConnect.dorm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.dormConnect.dorm.entities.GroupedRoomsEntity;
import pl.lodz.dormConnect.dorm.services.GroupedRoomsService;

import java.util.List;

@RestController
@RequestMapping("/api/dorm/group")
public class GroupedRoomsController {

    private final GroupedRoomsService groupedRoomsService;

    @Autowired
    public GroupedRoomsController(GroupedRoomsService groupedRoomsService) {
        this.groupedRoomsService = groupedRoomsService;
    }

    @GetMapping
    public List<GroupedRoomsEntity> getAllGroups() {
        return groupedRoomsService.getAllGroups();
    }

    @PostMapping("/create")
    public ResponseEntity<GroupedRoomsEntity> createGroup(@RequestBody GroupedRoomsEntity group) {
        return ResponseEntity.ok(groupedRoomsService.addGroup(group));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupedRoomsEntity> getGroup(@PathVariable Long id) {
        return groupedRoomsService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
