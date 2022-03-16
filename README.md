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

#### Phase 1 - outcomes

Page displays a cookie popup. In Chrome it also shows a suggestion for login with google account when applicable. I don't observe this behavior in Safari.
The DOM of the page contains references to React, so the page is likely built with it.
One of the first things I spotted is, that element ids look like randomly generated strings. Looking at the `<body>`, the id changes after each page reaload. It is safe to **assume, that ids are not suitable for test automation** on this page.

The flight search opens on the home page itself. It is also accessible from navigation on the left. I will work with an **assumption, that this is a default landing page and I don't have to use navigation after opening the page**.

I try searching for a destination airport. Each letter input triggers a search api call and top 4 results are displayed on the frontend. Interestingly enough, searching for a combination of numbers, instead of airport name also provides some results. However, searching for special characters does not provide any results. Deleting an invalid search, e.g. $$$$$$ with a backspace will trigger 6 search calls. Normally I would say this is silly, but on the other hand, I am impressed by the API performance.

Every now and then I observe a /measure API call. I assume this is for performance measurement.

Randomly a popup appears, trying to refer to a different portals.

Submitting a search form reroutes to a URL which reflects the search criteria, in my case `/flights/ZRH-VIE/2022-04-15/2022-04-22?sort=bestflight_a`. If I was interested, I could probably construct some URLs that would pose a challenge to the system, e.g. non existent airports, dates in a wrong order or non existing dates, maybe dates from the past... basically anything that the UI tries to prevent a normal user to do. But not now. As the URL hints, the default results page is "Best option" which is not ordered by price. For the sake of the main goal, I am more interested in flights ordered by price - cheapest first. I **assume I can switch to cheapest options ordering and ignore the best option**.

On the network side I see `/roundTrip` API request. The payload of this request is just departure and destination airport. It doesn't send any dates. The response however returns a huge list of price estimations. One estimation per day, up to January 2023. This is interesting and might hint to a limitation on the search. It doesn't even give me full year worth of estimations. This makes me want to check what happens if I search fligts more than a year from now. Anyways, these are just price estimations, not full offers.

Then I see a couple of calls that seem like tracking data, information about filter settings, google syndication (google pixel) and gdpr consents.

The backbone of the search seems to be a call to `flights/results/FlightSearchPoll`. The page repeatedly polls the API for new results. This poses a challenge to test automation, because the page updates as it receives new results and they are not just added at the end of the list, but on top as well. The amount of poll calls is unpredictable, but I haven't seen it go above 30. The worst part is, that it takes a generous amount of time. Easily polling for 1-1.5 minutes.

<img width="809" alt="Polling waterfall" src="https://user-images.githubusercontent.com/20669553/158700961-aff029af-fc06-4f73-9451-9efbf1f9f538.png">

Luckily for me, there is a funky "progress bar" on the page. I could probably use it to figure out when the page is ready.

When it comes to a price, the page offers price calculation in different currencies. For the sake of this exercise, I **assume the max price is defined in CHF**.

On the left side of the results page I can manipulate search criteria with different filter settings. Right now, the only potentially relevant setting seems to be a price slider, which tells me the price of the cheapest and the most expensive flights right away, or I can use it to limit the results only to the relevant ones (the price is <= max price). However, the slider seems a bit wonky and making it work with automation might take a long time and it will probably introduce flakiness. That's how I feel about it.

I think I have collected appropriate amount of information for me to go to the next phase.
