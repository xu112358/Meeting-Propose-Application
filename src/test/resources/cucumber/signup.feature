Feature: Sign Up
#  Scenario: Open Sign Up page from index page
#    Given I am on the index page
#    When I click the sign up button
#    Then I should be on the sign up page

  Scenario: Sign up Unsuccessfully Same Username
    Given I am on the Sign Up page
    When I enter "username" in Username field
    And I enter "password" in Password field
    And I enter "password" in Re-Password field
    And I press create account button
    Then I should sign up unsuccesfully

  Scenario: Sign up Unsuccessfully Password Mismatch
    Given I am on the Sign Up page
    When I enter "test" in Username field
    And I enter "password" in Password field
    And I enter "1" in Re-Password field
    And I press create account button
    Then Signup Password Mismatch
  Scenario: One Input Missing
    Given I am on the Sign Up page
    When I enter "" in Username field
    And I enter "password" in Password field
    And I enter "1" in Re-Password field
    And I press create account button
    Then Fill up all inputs
  Scenario: All Values Null
    Given I am on the Sign Up page
    When I enter "" in Username field
    And I enter "" in Password field
    And I enter "" in Re-Password field
    And I press create account button
    Then Fill up all inputs
#  Scenario: Sign up successfully
#    Given I am on the Sign Up page
#    When I enter "unique-username" in Username field
#    And I enter "password" in Password field
#    And I enter "password" in Re-Password field
#    And I press create account button
#    Then I should sign up succesfully