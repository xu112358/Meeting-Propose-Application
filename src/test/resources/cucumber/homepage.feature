Feature: homepage

  Scenario: Add event feature - form pop up
    Given I am on the homepage and signed in successfully
    When I click the date of the event I want to create
    And click the add event button
    Then I should see a form to let me enter event details


  Scenario: Add event feature - add event success
    Given I am on the homepage and the event form has poped up and signed in successfully
    When I enter the name of the event "testevent"
    And I enter the 2 people I'd like to invite
    And I enter OK
    Then I should see a event info about "testevent" with 2 people invited

  Scenario: Add event feature - add event cancel
    Given I am on the homepage and the event form has poped up and signed in successfully
    When I enter the cancel button
    Then I should see a event info unchanged

  Scenario: user see received final event
    Given sender has finalized the proposal with a event
    When receiver go to the home page
    And receiver find final event inside the calender
    Then receiver can reject the final event

  Scenario: user can reject received final event
    Given receiver see the final event
    When receiver click reject button on the calender
    Then receiver should reject the final event