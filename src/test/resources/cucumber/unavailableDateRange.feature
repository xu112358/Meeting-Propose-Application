Feature: indicate unavailable date range - user settings

  Scenario: add unavailable date range with empty start date
    Given I am on the user setting page and signed in as root
    When I enter nothing for the select start start date
    And I select an end date from the end date calendar
    And I click on the "Add Date Range" button
    Then I should see an error message of Start Date or End Date is Empty!

  Scenario: add unavailable date range with empty end date
    Given I am on the user settings page and signed in as root
    When I select a start date from the start date calendar
    And I enter nothing for the select end date
    And I click on the "Add Date Range" button
    Then I should see an error message of Start Date or End Date is Empty!

  Scenario: add unavailable date range with empty start date and empty end date
    Given I am on the user settings page and signed in as root
    When I enter nothing for the select start date
    And I enter nothing for the select end date
    And I click on the "Add Date Range" button
    Then I should see an error message of Start Date or End Date is Empty!

  Scenario: successfully add unavailable date range
    Given I am on the user settings page and signed in as root
    When I select "01/23/2019" from the select start date calendar
    And I select "01/23/2019" from the select end date calendar
    And I click on the "Add Date Range" button below
    Then my unavailable date range will appear on the unavailable date range list

  Scenario: successfully remove unavailable date range
    Given I am on the user settings page and signed in as root
    When I select "01/23/2019" from the select start date calendar
    And I select "01/23/2019" from the select end date calendar
    And I click on the "Add Date Range" button below
    And I click on the "remove" button of the date range "01/23/2019" to "01/23/2019"
    Then my unavailable date range will be deleted from the unavailable date range list