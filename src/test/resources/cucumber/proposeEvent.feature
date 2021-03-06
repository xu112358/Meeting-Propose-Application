Feature: Propose Event


  Scenario: Successfully propose event
    Given I am on the propose event page
    When I enter "root1" in Username search field
    When I enter "rock" in the Keyword field
    When I enter "Los Angeles" in the Location field
    When I select a date
    When I enter "eventName" in the GroupDate name field
    And I click the Username Add button
    And I click event search button
    And I click add event
    And I click the propose event button
    Then I successfully proposed the groupdate

  Scenario: Propose Event empty input text
    Given I am on the propose event page
    When I enter "root1" in Username search field
    When I select a date
    And I click the Username Add button
    And I click event search button
    Then input text is empty

  Scenario: Propose Event empty username
    Given I am on the propose event page
    When I enter "eventName" in the GroupDate name field
    And I click the propose event button
    Then sender users list or groupdate name is empty

  Scenario: Propose Event empty groupdate name
    Given I am on the propose event page
    When I enter "root1" in Username search field
    When I enter "" in the GroupDate name field
    And I click the propose event button
    Then sender users list or groupdate name is empty





  Scenario: remove a user
    Given I am on the propose event page
    When I enter "root1" in Username search field
    And I click the Username Add button
    And I click the Username Add button
    And I click the x next to the added user
    And I click OK button
    Then the user list should be empty

  Scenario: remove an event
    Given I am on the propose event page
    When I enter "rock" in the Keyword field
    When I enter "Los Angeles" in the Location field
    When I select a date
    And I click event search button
    And I click add event
    And I click the x next to the added user
    And I click OK button
    Then the event list should be empty

  Scenario: Propose Event add existing username
    Given I am on the propose event page
    When I enter "root1" in Username search field
    And I click the Username Add button
    And I click the propose event button
    And I click the Username Add button
    And I click the Username Add button
    Then username already exists in the sender list

  Scenario: Create a draft
    Given I am on the propose event page
    When I enter "root1" in Username search field
    When I enter "rock" in the Keyword field
    When I enter "Los Angeles" in the Location field
    When I select a date
    When I enter "eventName" in the GroupDate name field
    And I click the Username Add button
    And I click event search button
    And I click add event
    And I go to the home page
    And I go to the propose event page
    Then the added user and event should still be there
