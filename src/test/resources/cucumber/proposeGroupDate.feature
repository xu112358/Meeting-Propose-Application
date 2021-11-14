Feature: A user can propose a group date to other users

  Scenario: Add Username
    Given I am on the propose group date page
    When I enter "username" in Username field
    And I click username add button
    Then I should have added "username" as a participant for the proposed event successfully

  Scenario: Events Search
    Given I am on the propose group date page
    When I enter the event keyword "arts" in the keyword search field
    And I select "Sports" in the genre drop down menu
    And I enter "Los Angeles" in the location search field
    And I click on the calendar button on in the date range selection field
    And I select 12/14/2021 in the date range selection field
    And I click on the "Events Search" button
    Then a list of filtered events by keyword, genre, location, and date range will be displayed with event description hovering by mouse

  Scenario: Add Group Name
    Given I am on the propose group date page
    When I enter "Trojans Group" in Group Name field
    And I click propose events button
    Then I should have added "Trojans Group" group name for the proposed event successfully

  Scenario: Propose Events
    Given I am on the propose group date page
    When I click "Propose Events" button
    Then I should be able to propose an event by username, keyword, genre, location, and date range for the proposed event successfully

