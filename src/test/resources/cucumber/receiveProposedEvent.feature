Feature: Users receive the proposed events

  Scenario: Users reply to the sender with availability and preferences
    Given I am on the show availability and preference page
    When I click on "Invited Events" button
    And I enter my preference for the event on a scale of 1-5 (1 is the least and 5 is the most preferred)
    And I select my availability from the drop down menu "yes", "no", "maybe"
    And I click on the "Reply to Sender" button
    Then I should reply to the sender with my availability and preferences successfully

  Scenario: Users mark their unavailable date ranges
    Given I am on the show availability and preference page
    When choose the dates and times that I will not be available on a calendar
    And I click on the "Mark as Unavailable" button
    Then sender will not be able to send proposals during the chosen dates and times

  Scenario: Block users by searching
    Given I am on the show availability and preference page
    When I enter the username "username" in the search field
    And I click on the "Block User" button
    Then I should have blocked the user "username" from sending me proposed group dates

  Scenario: Display a List of blocked users
    Given I am on the show availability and preference page
    When I click on the "List of Blocked Users" button
    Then I should see a list of usernames with blocked users that I added to prevent them from sending me proposed group dates

  Scenario: Remove blocked users by searching
    Given I am on the show availability and preference page
    When I enter the username "username" in the search field
    And I click on the "Unblock User" button
    Then I should have unblocked the user "username" so that "username" can send me proposed group dates again

  Scenario: Remove blocked users from the list of blocked users
    Given I am on the show availability and preference page
    When I click on the "List of Blocked Users" button
    And I click on a username "username" from the list of blocked users displayed
    And I click on the "Unblock User" button
    Then I should have unblocked the user "username" so that "username" can send me proposed group dates again