Feature: Propose Event

  Scenario: Successfully propose event
    Given I am on the propose event page
    When I enter "rithikp06" in Username search field
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
    When I enter "rithikp06" in Username search field
    When I select a date
    And I click the Username Add button
    And I click event search button
    Then input text is empty

  Scenario: Propose Event empty username
    Given I am on the propose event page
    When I enter "" in Username search field
    When I enter "eventName" in the GroupDate name field
    And I click the propose event button
    Then sender users list or groupdate name is empty

  Scenario: Propose Event empty groupdate name
    Given I am on the propose event page
    When I enter "rithikp06" in Username search field
      When I enter "" in the GroupDate name field
      And I click the propose event button
      Then sender users list or groupdate name is empty

  Scenario: Propose Event add existing username
    Given I am on the propose event page
    When I enter "rithikp06" in Username search field
    And I click the Username Add button
    And I click the Username Add button
    Then username already exists in the sender list