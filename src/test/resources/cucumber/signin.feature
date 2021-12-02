Feature: Sign In

  Scenario: Sign in Success
    Given I am on the sign in page
    When I enter "username" in Username field
    When I enter "password" in Password field
    And I click sign in button
    Then I should log in successfully

  Scenario: Sign in Incorrect Credentials
    Given I am on the sign in page
    When I enter "username" in Username field
    When I enter "npassword" in Password field
    And I click sign in button
    Then username and password do not match log in unsuccessful
  Scenario: Sign in Empty Username
    Given I am on the sign in page
    When I enter "" in Username field
    When I enter "test" in Password field
    And I click sign in button
    Then username or password is empty
  Scenario: Sign in Empty Password
    Given I am on the sign in page
    When I enter "test" in Username field
    When I enter "" in Password field
    And I click sign in button
    Then username or password is empty
  Scenario: Sign in null values
    Given I am on the sign in page
    When I enter "" in Username field
    When I enter "" in Password field
    And I click sign in button
    Then username or password is empty
  Scenario: Log Out
    Given I am on the sign in page
    When I enter "username" in Username field
    When I enter "password" in Password field
    And I click sign in button
    And I click Log Out button
    Then I am on the sign in page

