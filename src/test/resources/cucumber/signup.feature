Feature: Sign Up
#  Scenario: Open Sign Up page from index page
#    Given I am on the index page
#    When I click the sign up button
#    Then I should be on the sign up page

  Scenario: Sign up Unsuccessfully
    Given I am on the Sign Up page
    When I enter "username" in Username field
    And I enter "password" in Password field
    And I enter "password" in Re-Password field
    And I enter "firstname" in First Name field
    And I enter "lastname" in Last Name field
    And I press create account button
    Then I should sign up unsuccesfully

#  Scenario: Sign up successfully
#    Given I am on the Sign Up page
#    When I enter "unique-username" in Username field
#    And I enter "password" in Password field
#    And I enter "password" in Re-Password field
#    And I enter "firstname" in First Name field
#    And I enter "lastname" in Last Name field
#    And I press create account button
#    Then I should sign up succesfully