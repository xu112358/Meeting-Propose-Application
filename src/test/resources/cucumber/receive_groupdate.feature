Feature: Receive Group Date

  Scenario: Accept proposed group date
    Given I am on the new received event page
    When I click an event
    And I click the accept button
    And I click OK button
    Then the group date gets accepted

  Scenario: Event got proposed
    Given I am on the new received event page
    When I logout
    When I login and go to the send proposal page
    When I enter "root" in Username search field
    When I enter "rock" in the Keyword field
    When I enter "Los Angeles" in the Location field
    When I select a date
    When I enter "new_event_test" in the GroupDate name field
    And I click the Username Add button
    And I click event search button
    And I click add event
    And I click the propose event button
    And I logout
    And I go to the received group date page
    Then the new event should be there


  Scenario: Reject proposed group date
    Given I am on the new received event page
    When I click an event
    And I click the reject button
    And I click OK button
    Then the group date gets rejected

  Scenario: Update preference and availability for proposed group date
    Given I am on the new received event page
    When I click an event
    And I update my preference
    And I update my availability
    And I click back
    And I click an event
    Then my preference of the group date should get updated


