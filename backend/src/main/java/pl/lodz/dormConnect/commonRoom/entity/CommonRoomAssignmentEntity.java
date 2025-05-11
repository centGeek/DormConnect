package pl.lodz.dormConnect.commonRoom.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "common_room_assignment")
public class CommonRoomAssignmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "common_room_id")
    private CommonRoomEntity commonRoom;

    private List<Long> usersId = new ArrayList<>();

    private boolean archived;

    private Date startDate;
    private Date endDate;
}
