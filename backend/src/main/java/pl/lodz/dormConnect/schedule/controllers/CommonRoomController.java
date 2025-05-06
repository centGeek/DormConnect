package pl.lodz.dormConnect.schedule.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.dormConnect.schedule.entity.CommonRoom;
import pl.lodz.dormConnect.schedule.repositories.CommonRoomRepository;

import java.util.List;

@RestController
@RequestMapping("/api/common-room")
@Controller
class CommonRoomController {
    CommonRoomRepository repository;

    public CommonRoomController(CommonRoomRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/add")
    public CommonRoom addCommonRoom(CommonRoom commonRoom) {
        return repository.save(commonRoom);
    }

    @PostMapping("/delete/{id}")
    public void deleteCommonRoom(Long id) {
        repository.deleteCommonRoomById(id);
    }

    @PostMapping("/show")
    public List<CommonRoom> showCommonRooms() {
        return repository.findAll();
    }
}
