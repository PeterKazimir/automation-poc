# automation-poc

Goal:

Create Web UI test which proves that kayak.ch always returns a roundtrip flight below certain price.
The test should take the following parameters:
  - From and To airports
  - Date range
  - Max price

The project should include a gradle build file, which can be used to execute the test.

## Phase 1 - discovery

Phase goals:

* get familiar with a website itself, explore it, learn about the "ecosystem" in which the tested feature exists
* try to figure out, what technology (FE stack) is used, are there known challenges or best practices for this particular FE stack?
* identify challenges for the test automation for example:
  * does the page load content asynchronously?
  * do parts of the page load on scroll? 
  * does the page contain some fancy control elements?
* estabilish set of safe starting point assumptions, for example:
  * is it safe to assume that we are only intersted in the form displayed on the main page?
  * is it safe to assume that a logged in user will not see a different flight options / combinations
  * ...
* try to identify API calls which are critical for UI to work, can I leverage them?
