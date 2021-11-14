Feature: Search Event
  Scenario: Search for An Event
    Given I am on the Search Events page
    When I click the targeted keyword field
    And I enter start date in the start date field
    And I enter end date in the end date field
    Then I will be listed all the events that satisfied my above requirement