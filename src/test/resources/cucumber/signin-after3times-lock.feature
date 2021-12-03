Feature: sign in page lock user account after 3 times failure to log in

  Scenario: sign in page first time log in with wrong password
    Given I am on the sign in page trying to log in as root
    When I entered correct username and wrong password for the root user name for the first time
    And I click on the sign in button below
    Then I should see an error message that "Username and password do not match!"

  Scenario: sign in page second time log in with wrong password
    Given I am on the sign in page trying to log in as root
    When I entered correct username and wrong password for the root user name for the second time
    And I click on the sign in button below
    Then I should see an error message that "Username and password do not match!"

  Scenario: sign in page third time log in with wrong password
    Given I am on the sign in page trying to log in as root
    When I entered correct username and wrong password for the root user name for the third time
    And I click on the sign in button below
    Then I should see an error message that "Username and password do not match!"

  Scenario: sign in page after 3 times log in with wrong password
    Given I am on the sign in page trying to log in as root
    When I entered correct username and wrong password for the root user name after the third time
    And I click on the sign in button below
    Then I should see an error message that "Your account is locked!"