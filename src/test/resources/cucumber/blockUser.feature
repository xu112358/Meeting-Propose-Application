Feature: Block User Feature - User Settings
  Scenario: Add blocked users - success
    Given I am on the user setting page and signed in as unique-username
    When I enter "test1" in the Username Search form
    And "test1" is not in the blocked user list
    And I click the Blocked This Username button
    Then I should see "test1" appear on the Username list

  Scenario: Add blocked users - already in list
    Given I am on the user setting page and signed in as unique-username
    When I enter "test1" in the Username Search form
    And "test1" is in the blocked user list
    And I click the Blocked This Username button
    Then I should see an error message of test1 is already on your blocked list

  Scenario: Add blocked users - add self
    Given I am on the user setting page and signed in as unique-username
    When I enter "unique-username" in the Username Search form
    And I click the Blocked This Username button
    Then I should see an error message of You cannot block yourself!

  Scenario: Add blocked users - add empty
    Given I am on the user setting page and signed in as unique-username
    When I enter nothing in the Username Search form
    And I click the Blocked This Username button
    Then I should see an error message of Input Username is Empty!

  Scenario: Remove blocked users - cancle
    Given I am on the user setting page and signed in as "unique-username" and "test1" is on my blocked user list
    When I click the remove button of "test1"
    And I see a warning saying "Are you sure you want to remove this blocked user?"
    And I click cancel button
    Then test1 will still be on the blocked user list

  Scenario: Remove blocked users - success
    Given I am on the user setting page and signed in as "unique-username" and "test1" is on my blocked user list
    When I click the remove button of "test1"
    And I see a warning saying "Are you sure you want to remove this blocked user?"
    And I click OK button
    Then test1 will be deleted from the blocked user list

