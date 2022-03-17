Feature: Boilerplate feature for automation POC

  Scenario: The dependencies are loaded correctly
    Given the tests are executed
    When the browser opens at www.google.com
    Then the Google search page is loaded