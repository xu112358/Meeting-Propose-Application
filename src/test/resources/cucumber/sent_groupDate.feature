Feature: Sent Group Date （/list-sent-invite）

  Scenario: Sent Group Date Main Page
    Given I am on the Sent GroupDates main page
    Then I should see a list of events with their date and status

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