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
    public void dormProblemWithStatusAndStudentId(String status, Long studentId) {
        currentProblem = new DormProblemEntity();
        currentProblem.setProblemStatus(ProblemStatus.valueOf(status));
        currentProblem.setStudentId(studentId);
    }

    @Given("a user with user id {long}")
    public void userWithUserId(Long userId) {
        currentProblem = new DormProblemEntity();
        currentProblem.setStudentId(userId);
    }

    @When("I save the dorm problem")
    public void saveDormProblem() {
        repository.updateOrInsert(currentProblem);
    }

    @When("I save the dorm problem with user id")
    public void saveDormProblemWithStudentId() {
        repository.updateOrInsert(currentProblem);
    }

    @Then("I should find {int} problem with status {string}")
    public void shouldFindProblemsByStatus(int count, String status) {
        ProblemStatus problemStatus = ProblemStatus.valueOf(status);
        List<DormProblemEntity> found = repository.findByProblemStatus(problemStatus);
        assertThat(found).hasSize(count);
    }

    @Then("I should find {int} problem with user id {long}")
    public void shouldFindProblemWithUserId(int count, long userId) {
        List<DormProblemEntity> found = repository.findByUserId(userId);
        assertThat(found).hasSize(count);

    }
}
