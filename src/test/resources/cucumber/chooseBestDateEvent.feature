Feature: The system will determine the best overall group date and event choice for the group of users based on the preferences and availability

  Scenario: choose an optimal group date
    Given I am on the choose best group date page as an event sender
    When I receive invited users' reply of availability and preferences
    And I click on the "Finalize Event" button
    Then the system should pick and display an optimal date for the proposed event with total value of preference

  Scenario: a chosen event is sent back to receiver upon date being finalized
    Given I am on the choose best group date page
    When an event I chose has its proposed date finalized
    Then I should receive a message with the event name and finalized group date

  Scenario: choose an optimal event
    Given I am on the choose best group date page as an event sender
    When I click on date 12/14/2021 on the calendar button in the date field
    And I click on the "View Optimal Events" button
    Then the system should display a list of optimal events based on maximum preference on 12/14/2021