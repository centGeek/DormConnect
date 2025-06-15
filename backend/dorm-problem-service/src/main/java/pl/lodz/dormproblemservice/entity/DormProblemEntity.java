package pl.lodz.dormproblemservice.entity;

import jakarta.persistence.*;
import lombok.*;
import pl.lodz.dormproblemservice.ProblemStatus;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Builder
@Table(name = "dorm_problem")
public class DormProblemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long studentId;
    private String name;
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
        if (!(o instanceof DormProblemEntity)) return false;
        DormProblemEntity that = (DormProblemEntity) o;
        return that.getId() == getId()
                && that.getStudentId() == getId()
                && !that.getDescription().equals(getDescription())
                && !that.getProblemDate().equals(getProblemDate())
                && !(that.getProblemStatus() == getProblemStatus());
    }

    public DormProblemEntity(long studentId, String name, String description, String answer, LocalDate problemDate,
                             LocalDate submittedDate, ProblemStatus problemStatus) {
        this.studentId = studentId;
        this.name = name;
        this.description = description;
        this.answer = answer;
        this.problemDate = problemDate;
        this.submittedDate = submittedDate;
        this.problemStatus = problemStatus;
    }
}
