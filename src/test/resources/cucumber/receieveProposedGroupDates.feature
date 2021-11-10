Feature: A chosen event would be sent back to the receiver when proposed group dates are finalized.

  Scenario: Receieve proposed group dates - Success
    Given I am on the event search page
    When the proposed group dates are finalized
    Then I should get a notification about the chosen event with event details


  Scenario: Receieve proposed group dates - Fail
    Given I am on the event search page
    When the proposed group dates are finalized
    And there is no common group dates
    Then I should get a notification that shows no common group dates