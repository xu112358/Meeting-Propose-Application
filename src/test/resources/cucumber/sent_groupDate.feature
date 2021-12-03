Feature: Sent Group Date （/list-sent-invite）


  Scenario: Sent Group Date Main Page
    Given I am on the propose event page
    When I enter "root1" in Username search field
    When I enter "rock" in the Keyword field
    When I enter "Los Angeles" in the Location field
    When I select a date
    When I enter "test event" in the GroupDate name field
    And I click the Username Add button
    When I clear the Username search field
    And I click event search button
    And I click add event
    When I clear the Keyword field
    When I enter "pop" in the Keyword field
    And I click event search button
    And I click add event
    And I click the propose event button
    And I go to the sent groudates page
    Then I should see a list of events with their date and status

  Scenario: Delete event from proposal
    Given I am on the Sent GroupDates main page
    When I click the "test event" proposal
    And I delete an event
    And I click OK button
    Then there should be one less event

  Scenario:  Delete user from proposal
    Given I am on the Sent GroupDates main page
    When I click the "test event" proposal
    And I delete a user
    And I click OK button
    Then there should be one less user


  Scenario: Sent Group Date Events datils
    Given I am on the Sent GroupDates main page
    When I click into one of the event
    Then I should see the preference and availability of the receivers about the events.

  Scenario: Sent Group Date Events finalize - accept
    Given I am on the Sent GroupDates main page
    When I click into one of the event
    And I click finalize button
    And I see the stats of the event
    And I click yes
    Then I should see the finalized event.

  Scenario: Sent Group Date Events finalize - No
    Given I am on the Sent GroupDates main page
    When I click into one of the event
    And I click finalize button
    And I see the stats of the event
    And I click No
    Then I will be direct back to the sent group date page

  Scenario: Sent Group Date Events confirm
    Given I am on the Sent GroupDates main page
    When I click into one of the event
    And I click confirm button
    And I see the warning saying Are you sure you want to Confirm this GroupDate?
    And I click OK
    Then I can see the event pop on the set groupdate page

  Scenario: Sent Group Date Events Back
    Given I am on the Sent GroupDates main page
    When I click into one of the event
    And I click Back button
    Then I can will be direct back to the receive-groupDates page

  Scenario: Add a user to sent proposal
    Given root1 is on the Sent GroupDates main page
    When root1 type "minyiche1" in the username search box
    And root1 click add username button
    Then root1 should see there is one more user in the receiver table

  Scenario: Add an event to sent proposal
    Given root1 is on the Sent GroupDates main page
    When root1 enter "rock" in Keyword field
    When root1 enter "Los Angeles" in Location field
    When root1 select a event date
    And root1 click event search button
    And root1 add the first event in the result into the proposal
    Then root1 should see there is one more event in the receiver table


  Scenario: Delete the last event from sent proposal
    Given root1 is on the event lists page of proposal "testDelete"
    And root1 delete the only event
    And root1 click OK button
    Then root1 will delete the invite because it is the last event and root1 will be sent to the Sent GroupDates main page



