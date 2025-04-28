package pl.lodz.dormConnect.dorm.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class GroupedRoomsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Tutaj to i tak generuje group_name
    private String groupName;

    @OneToMany
    private List<RoomEntity> rooms;

}
