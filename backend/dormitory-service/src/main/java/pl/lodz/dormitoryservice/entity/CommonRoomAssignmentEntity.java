package pl.lodz.dormitoryservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

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
    @Nullable
    @JoinColumn(name = "common_room_id")
    private CommonRoomEntity commonRoom;

    private List<Long> usersId = new ArrayList<>();

    private boolean archived;

    private Date startDate;
    private Date endDate;

    public CommonRoomAssignmentEntity(CommonRoomEntity commonRoom, List<Long> usersId, boolean archived, Date startDate, Date endDate) {
        this.commonRoom = commonRoom;
        this.usersId = usersId;
        this.archived = archived;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
