Feature: 60 seconds auto logged out

  Scenario: Log in user's session (less than 60 seconds still logged in)
    Given I am on the sign in page and already signed in as root
    When I stay on the page passively for 10 seconds
    Then I am still logged in

  Scenario: Log in user's session (more than 60 seconds auto logged out)
    Given I am on the sign in page and signed in as root
    When I stay on the page passively for 61 seconds
    Then I am automatically logged out