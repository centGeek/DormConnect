package pl.lodz.dormConnect.dorm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.dormConnect.dorm.DTO.GroupRoomsRequest;
import pl.lodz.dormConnect.dorm.DTO.GroupedRoomsDTO;
import pl.lodz.dormConnect.dorm.mapper.GroupedRoomsMapper;
import pl.lodz.dormConnect.dorm.services.GroupedRoomsService;
import pl.lodz.entity.GroupedRoomsEntity;

import java.util.List;

@RestController
@RequestMapping("/api/dorm/room/group")
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
    public ResponseEntity<GroupedRoomsEntity> createGroup(@RequestBody GroupRoomsRequest groupRoomsRequest) {
        return ResponseEntity.ok(groupedRoomsService.addGroup(groupRoomsRequest));
    }


    @GetMapping("/{id}")
    public ResponseEntity<GroupedRoomsDTO> getGroupById(@PathVariable Long id) {
        return groupedRoomsService.findById(id)
                .map(GroupedRoomsMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
