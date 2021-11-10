Feature: Receivers can accept or decline, track and visualize their received proposals on the same page.

  Scenario: Receive an invitation of an event to other users - Success
    Given I am on the messages page
    When Another user send me a event notification
    Then The event information should shown on my message list

  Scenario: Receiver track and visualize an invitation of an event
    Given I am on the messages page
    When scroll up and down the page
    Then I should see a list of pending and past events with event details including name, dates, and status

  Scenario: Receiver can accept an invitation of an event
    Given I am on the messages page
    When I click the accept button of one of the invited events on the page
    Then I should accept the event and the event status becomes accepted.

  Scenario: Receiver can decline an invitation of an event
    Given I am on the messages page
    When I click the decline button of one of the invited events on the page
    Then I should decline the event and the event status becomes declined.
