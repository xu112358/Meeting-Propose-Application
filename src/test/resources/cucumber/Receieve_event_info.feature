Feature: User receive an invitation of an event to other users

  Scenario: Receive an invitation of an event to other users - Success
    Given I am on the messages page
    When Another user send me a event notification
    Then The event information should shown on my message list