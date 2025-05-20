package pl.lodz.dormConnect.floors.service;

import org.springframework.stereotype.Service;
import pl.lodz.dormConnect.floors.entity.FloorEntity;
import pl.lodz.dormConnect.floors.repositories.FloorsRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class FloorsService {
    private final FloorsRepository floorsRepository;

    public FloorsService(FloorsRepository floorsRepository) {
        this.floorsRepository = floorsRepository;
    }

    public List<Integer> getFloors(){
        return floorsRepository.findAll().stream()
                .map(floorEntity -> floorEntity.getFloorNumber())
                .toList();
    }

    public FloorEntity addFloor(){
        int floorNumber = getFloors().stream().max(Integer::compare).orElse(-1) + 1;
        FloorEntity floor = new FloorEntity();
        floor.setFloorNumber(floorNumber);
        floor.setRooms(new ArrayList<>());
        floor.setCommonRooms(new ArrayList<>());
        floorsRepository.save(floor);
        return floor;
    }
}
