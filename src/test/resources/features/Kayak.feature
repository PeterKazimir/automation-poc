@kayak
Feature: Roundtrip flight offers

  Background:
    Given the user visits kayak.ch landing page
    And the user searches for roundtrip offers

  Scenario Outline: Roundtrip between two airports
    Given the departure airport is <departure>
    And the destination airport is <destination>
    And the departure date is <departure date>
    And the return date is <return date>
    And the desired maximum price is <max price>
    Then all search results are roundtrip offers
    And at least one result is cheaper than <max price>
    Examples:
      | departure | destination | departure date | return date | max price |
      | ZRH       | VIE         | 30.09.2022     | 10.10.2022  | 800       |
