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
    private String answer;
    @Temporal(TemporalType.DATE)
    private LocalDate problemDate;
    @Temporal(TemporalType.DATE)
    private LocalDate submittedDate;
    @Enumerated(EnumType.STRING)
    private ProblemStatus problemStatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DormProblem)) return false;
        DormProblem that = (DormProblem) o;
        return that.getId() == getId()
                && that.getStudentId() == getId()
                && !that.getDescription().equals(getDescription())
                && !that.getProblemDate().equals(getProblemDate())
                && !(that.getProblemStatus() == getProblemStatus());
    }
    
}
