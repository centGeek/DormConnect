package pl.lodz.dormConnect.dorm.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.dormConnect.dorm.entities.GroupedRoomsEntity;
import pl.lodz.dormConnect.dorm.entities.RoomEntity;
import pl.lodz.dormConnect.dorm.repositories.GroupedRoomsRepository;

import java.util.List;
import java.util.Optional;

@Service
public class GroupedRoomsService {
    private final GroupedRoomsRepository groupedRoomsRepository;

    @Autowired
    public GroupedRoomsService(GroupedRoomsRepository groupedRoomsRepository) {
        this.groupedRoomsRepository = groupedRoomsRepository;
    }

    public List<GroupedRoomsEntity> getAllGroups() {
        return groupedRoomsRepository.findAll();
    }

    public GroupedRoomsEntity addGroup(GroupedRoomsEntity group) {
        if (group.getRooms() != null) {
            for (RoomEntity room : group.getRooms()) {
                room.setGroupedRooms(group);
            }
        }

        return groupedRoomsRepository.save(group);
    }


    public Optional<GroupedRoomsEntity> findById(Long id) {
        return groupedRoomsRepository.findById(id);
    }
}
