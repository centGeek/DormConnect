package pl.lodz.dormConnect.dorm.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.dormConnect.dorm.DTO.GroupRoomsRequest;
import pl.lodz.commons.entity.GroupedRoomsEntity;
import pl.lodz.commons.entity.RoomEntity;
import pl.lodz.commons.repository.jpa.GroupedRoomsRepository;
import pl.lodz.commons.repository.jpa.RoomRepository;

import java.util.List;
import java.util.Optional;

@Service
public class GroupedRoomsService {

    private final GroupedRoomsRepository groupedRoomsRepository;

    private final RoomRepository roomRepository;

    @Autowired
    public GroupedRoomsService(GroupedRoomsRepository groupedRoomsRepository, RoomRepository roomRepository) {
        this.groupedRoomsRepository = groupedRoomsRepository;
        this.roomRepository = roomRepository;
    }

    public List<GroupedRoomsEntity> getAllGroups() {
        return groupedRoomsRepository.findAll();
    }

    @Transactional
    public GroupedRoomsEntity addGroup(GroupRoomsRequest groupRoomsRequest) {
        // Pobierz pokoje na podstawie ID
        List<RoomEntity> rooms = roomRepository.findAllById(groupRoomsRequest.roomIds());

        if (rooms.size() != groupRoomsRequest.roomIds().size()) {
            throw new IllegalArgumentException("Niektóre pokoje nie zostały znalezione");
        }

        // Stwórz nową grupę
        GroupedRoomsEntity group = new GroupedRoomsEntity();
        group.setGroupName(groupRoomsRequest.groupName());
        group.setRooms(rooms);

        // Jeśli RoomEntity ma odwrotne odniesienie do grupy – ustaw je
        for (RoomEntity room : rooms) {
            room.setGroupedRooms(group);
        }

        // Zapisz grupę (pokój może być zapisany automatycznie przez kaskadę lub trzeba go zapisać osobno – zależnie od mapowania)
        return groupedRoomsRepository.save(group);
    }


    public Optional<GroupedRoomsEntity> findById(Long id) {
        return groupedRoomsRepository.findById(id);
    }
}
