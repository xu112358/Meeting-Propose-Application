Feature: homepage

  Scenario: user see received final event
    Given sender has finalized the proposal with a event
    When receiver go to the home page
    And receiver find final event inside the calender
    Then receiver can reject the final event

  Scenario: user can reject received final event
    Given receiver see the final event
    When receiver click reject button on the calender
    Then receiver should reject the final event

  Scenario: user see received final event
    Given sender has finalized the proposal with a event
    When receiver go to the home page
    And receiver find final event inside the calender
    Then receiver can reject the final event

  Scenario: user can reject received final event
    Given receiver see the final event
    When receiver click reject button on the calender
    Then receiver should reject the final event