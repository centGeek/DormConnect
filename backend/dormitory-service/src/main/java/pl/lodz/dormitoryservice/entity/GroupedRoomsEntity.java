package pl.lodz.dormitoryservice.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")

public class GroupedRoomsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Tutaj to i tak generuje group_name
    private String groupName;

    @OneToMany(cascade = CascadeType.ALL)
    private List<RoomEntity> rooms;

    public GroupedRoomsEntity(String groupName, List<RoomEntity> rooms) {
        this.groupName = groupName;
        this.rooms = rooms;
    }
}
