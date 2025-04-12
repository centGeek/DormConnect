package pl.lodz.dormConnect.dorm.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
    private Long id;

    //Tutaj to i tak generuje group_name
    private String groupName;

    @OneToMany
    private List<RoomEntity> rooms;

}
