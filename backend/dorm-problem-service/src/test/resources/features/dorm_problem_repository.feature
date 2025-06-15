Feature: DormProblemRepository

  Scenario: Insert a new dorm problem and find it by status
    Given a dorm problem with status "SUBMITTED" and studentId 123
    When I save the dorm problem
    Then I should find 1 problem with status "SUBMITTED"

  Scenario: Insert a new dorm problem and find it by user id
    Given a user with user id 1253
    When I save the dorm problem with user id
    Then I should find 1 problem with user id 1253