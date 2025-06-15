Feature: DormProblemRepository

  Scenario: Insert a new dorm problem and find it by status
    Given a dorm problem with status "SUBMITTED" and studentId 123
    When I save the dorm problem
    Then I should find 1 problem with status "SUBMITTED"