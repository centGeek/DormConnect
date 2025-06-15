package pl.lodz.dormproblemservice.cucumberglue;

import static org.assertj.core.api.Assertions.assertThat;

import config.PostgresContainerConfig;
import io.cucumber.java.en.*;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import pl.lodz.dormproblemservice.ProblemStatus;
import pl.lodz.dormproblemservice.entity.DormProblemEntity;
import pl.lodz.dormproblemservice.repository.DormProblemRepository;

import java.util.List;
@SpringBootTest
@ActiveProfiles("test")
@CucumberContextConfiguration
@Import(PostgresContainerConfig.class)
public class DormProblemRepositorySteps {

    @Autowired
    private DormProblemRepository repository;

    DormProblemEntity currentProblem;

    @Given("a dorm problem with status {string} and studentId {long}")
    public void a_dorm_problem_with_status_and_studentId(String status, Long studentId) {
        currentProblem = new DormProblemEntity();
        currentProblem.setProblemStatus(ProblemStatus.valueOf(status));
        currentProblem.setStudentId(studentId);
    }

    @When("I save the dorm problem")
    public void i_save_the_dorm_problem() {
        repository.updateOrInsert(currentProblem);
    }

    @Then("I should find {int} problem with status {string}")
    public void i_should_find_problems_by_status(int count, String status) {
        ProblemStatus problemStatus = ProblemStatus.valueOf(status);
        List<DormProblemEntity> found = repository.findByProblemStatus(problemStatus);
        assertThat(found).hasSize(count);
    }


}
