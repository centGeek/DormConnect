package pl.lodz.dormConnect.dormProblem.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Builder
@Table(name = "dorm_problem")
public class DormProblem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long studentId;
    private String description;
    @Temporal(TemporalType.DATE)
    private LocalDate problemDate;
    @Enumerated(EnumType.STRING)
    private ProblemStatus problemStatus;
    
}
