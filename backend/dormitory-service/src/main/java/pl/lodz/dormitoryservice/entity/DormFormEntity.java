package pl.lodz.dormitoryservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dorm_forms")
public class DormFormEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private LocalDate startDate;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private boolean isProcessed;

    @Column(length = 500)
    private String comments;

    @Column(nullable = false)
    private Integer priorityScore;


}
