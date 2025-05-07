package pl.lodz.dormConnect.commonRoom.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.dormConnect.commonRoom.entity.CommonRoomAssigment;
import pl.lodz.dormConnect.commonRoom.service.CommonRoomAssigmentService;

import java.util.List;

@RestController
@RequestMapping("/api/common-room-assigment")
class CommonRoomAssigmentController {
    private final CommonRoomAssigmentService service;

    public CommonRoomAssigmentController(CommonRoomAssigmentService service) {
        this.service = service;
    }

    @GetMapping("/get/{common_room_id}")
    public List<CommonRoomAssigment> getAssigmentsByCommonRoomId(@PathVariable Long common_room_id) {
        return service.getCommonRoomAssgimentsByCommonRoomId(common_room_id);
    }
}
