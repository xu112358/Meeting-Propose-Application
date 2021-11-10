Feature: User send an invitation of an event to other users.

  Scenario: Send Envent invitation - success
    Given I am on the event search page
    When I type the GroupDate Name
    And I type the user email I would like to invite
    And I enter the proposed event information
    And  I click the propose event button
    Then the invited users should get a notification of my invitation and I should get get a notification of success invitation

  Scenario: Send Envent invitation - missing users info
    Given I am on the event search page
    When I type the GroupDate Name
    And I enter the proposed event information
    And  I click the propose event button
    Then I should get prompted about missing invite users

  Scenario: Send Envent invitation - missing GroupDate info
    Given I am on the event search page
    When I type the user email I would like to invite
    And I enter the proposed event information
    And  I click the propose event button
    Then I should get prompted about missing GroupDate info

  Scenario: Send Envent invitation - missing event info
    Given I am on the event search page
    When I type the GroupDate Name
    And I type the user email I would like to invite
    And  I click the propose event button
    Then I should get prompted about missing event info