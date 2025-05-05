package pl.lodz.dormConnect.schedule.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.dormConnect.database.entity.UserEntity;
import pl.lodz.dormConnect.dorm.entities.RoomEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CommonRoomAssigment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private RoomEntity room;

    @ManyToMany
    private List<UserEntity> users = new ArrayList<>();

    private Date startDate;
    private Date endDate;
}
