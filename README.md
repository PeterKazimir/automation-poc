# automation-poc

Goal:

Create Web UI test which proves that kayak.ch always returns a roundtrip flight below certain price.
The test should take the following parameters:
  - From and To airports
  - Date range
  - Max price

The project should include a gradle build file, which can be used to execute the test.

### Table of Contents
* [Phase 1 - discovery](#phase-1---discovery)
  * [Phase 1 - outcomes](#phase-1---outcomes)
* [Phase 2 - test analysis](#phase-2---test-analysis)
* [Phase 3 - automation POC](#phase-3---automation-poc)

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

On the network side I see `/roundTrip` API request. The payload of this request is just departure and destination airport. It doesn't send any dates. The response however returns a huge list of price estimations. One estimation per day, up to January 2023. This is interesting and might hint to a limitation on the search. It doesn't even give me full year worth of estimations. This makes me want to check what happens if I search fligts more than a year from now. Anyways, these are just price estimations, not full offers. Upon further exploration, I suspect that these values are used in the date pickers to color code the days with price estimations.

Then I see a couple of calls that seem like tracking data, information about filter settings, google syndication (google pixel) and gdpr consents.

The backbone of the search seems to be a call to `flights/results/FlightSearchPoll`. The page repeatedly polls the API for new results. This poses a challenge to test automation, because the page updates as it receives new results and they are not just added at the end of the list, but on top as well. The amount of poll calls is unpredictable, but I haven't seen it go above 30. The worst part is, that it takes a generous amount of time. Easily polling for 1-1.5 minutes.

<img width="809" alt="Polling waterfall" src="https://user-images.githubusercontent.com/20669553/158700961-aff029af-fc06-4f73-9451-9efbf1f9f538.png">

Luckily for me, there is a funky "progress bar" on the page. I could probably use it to figure out when the page is ready.

When it comes to a price, the page offers price calculation in different currencies. For the sake of this exercise, I **assume the max price is defined in CHF**.

On the left side of the results page I can manipulate search criteria with different filter settings. Right now, the only potentially relevant setting seems to be a price slider, which tells me the price of the cheapest and the most expensive flights right away, or I can use it to limit the results only to the relevant ones (the price is <= max price). However, the slider seems a bit wonky and making it work with automation might take a long time and it will probably introduce flakiness. That's how I feel about it.

I think I have collected appropriate amount of information for me to go to the next phase.

## Phase 2 - test analysis

In most general terms, this is a two step process - submit a form, verify the results. Let's break it down like this.

#### Form submission

<img width="1085" alt="Form fields" src="https://user-images.githubusercontent.com/20669553/158830804-5467c2f6-bfa4-4fd5-8286-ceaa801a9ea1.png">

The form contains fields for different values. I will consider these controls to be out of scope for this exercise:
* "passengers" dropdown - out of scope
* "class" dropdown - out of scope
* "luggage" dropdown - out of scope

In scope areas are:

##### "Roundtrip" dropdown

The main goal is to prove, that the search always returns a roundtrip flight. The dropdown also allows flights that are not roundtrip. I will work with an **assumption, that the filter needs to be set to roundtrip**. Everything else would be a negative test case. Idea for future extension - check roundtrip flights created with trip builder.

##### "From" and "To" airport - text input

Users types in the desired airport info. Each keystroke triggers a search. User can find an airport multiple ways - for example, to find Zürich airport, you can type in "Zurich", "Zürich", "Zuerich", "ZRH", but it will also appear if you search for "Schweiz", "Switzerland", "Svizerra". On the other hand, search can produce multiple results, for example search for "Paris" will show 4 valid options (the search api returns 6 actually, but only 4 are displayed). 

Each airport is represented by a unique three letter code, **assumption: it seems appropriate to use this code to identify the airport precisely in both form, and the results.**

The user can choose multiple "from" and multiple "to" airports. This poses a big challenge to a definition of "roundtrip" flight. The question is, which of these combinations are considered a valid roundtrip flights (I use 2 "from" and 2 "to" airports) to illustrate.

User selects Zürich and Basel as "From" and Wien and Prag as "To".

(The example table is not exhaustive.)

| Option       | Way out - departure | Way out - landing | Return - departure | Return - landing |
| ------------ | ------------------- | ----------------- | ------------------ | ---------------- |
| 1            | Zürich              | Wien              | Wien               | Zürich           |
| 2            | Zürich              | Wien              | Wien               | Basel            |
| 3            | Zürich              | Wien              | Prag               | Zürich           |
| 4            | Zürich              | Wien              | Prag               | Basel            |

I did more exploration about this concept, and it appears, that kayak.ch considers all of these options to be valid roundtrips.

Example:
<img width="738" alt="Roundtrip to a different airport" src="https://user-images.githubusercontent.com/20669553/158840132-ed2adb9d-a919-47be-89ec-8561c0e8e32f.png">

Looking back at the main requirement, it says "From and To airports", which might mean plural for both departure and landing, however, in the first iteration of automation POC, I will work with **assumption, that only 1 valid unique "from" airport and only 1 valid unique "to" airport described by their unique 3 letter code.** This means, that searching for Paris, or New York is not allowed, as they behave as containers for multiple airports in the area (choosing NYC will return JFK, Newark, La Guradia and Port Authority in the results).

##### Date pickers

<img width="687" alt="Date picker" src="https://user-images.githubusercontent.com/20669553/158849574-5c964c22-2519-49d2-b467-2fd4f555f611.png">

Unfortunatelly, the date picker does not allow a keyboard entry of desired flight dates. I will need to fiddle around with the picker UI.
I will **assume, that I am only interested in flights on exact dates, specified by the test.** Opening the page with no cookies (incognito) by default chooses a departure date a month from today. I need to be ready to move around the calendar in both directions - previous months, future months. The calendar items show a nice use of accessibility metadata for example `role="button" aria-label="15. Juni 2022"`. I will most likely use this to identify the right date. But I will need to invest some time in making the date / format exactly right, plus it is in German.


## Phase 3 - automation POC

The project is a web UI test which requires a gradle file to run.
Even though articles exists on how to use Gradle with Python scripts, or Javascript tools such as Jasmine or Karma, I decided to stick to the stack I am most familiar with and create a POC using Java + Cucumber + Selenium.

