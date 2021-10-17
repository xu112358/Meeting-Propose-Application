Feature: Sign In

  #Scenario: Sign in
  #  Given I am on the sign in page
  #  When I enter "username" in Username field
  #  When I enter "password" in Password field
  #  And I click sign in button
  #  Then I should log in successfully

  Scenario: Sign in
    Given I am on the sign in page
    When I enter "username" in Username field
    When I enter "npassword" in Password field
    And I click sign in button
    Then username and password do not match log in unsuccessful