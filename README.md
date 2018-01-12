Synopsis
-

This repository is for a pet NHL Pick 'Em Android app. The purpose of this app is ultimately for me to learn Android development and have a little fun. At some point I will release the app to the app store when I believe I have met a minimum viable product.

Technology used
-

- Android Studio (Currently 3.0)
- Local builds to personal Pixel phone
- Local builds to various emulated phones 
- NHL Stats API (example call: https://statsapi.web.nhl.com/api/v1/schedule?date=2017-10-26)

A bit about the NHL API
-

I happened across the NHL API while peeking at the network traffic of the newly redesigned NHL.com site when it first launched in 2015. I noticed that the entire frontend was being powered by various calls to various endpoints of the same API. Once I started messing around with the different API calls, I realized that the API was completely open, free to be hit from any origin space and/or any device. That fact inspired me to take a shot at using this simple but powerful API as the foundation for my first Android app.

Current version and features
-
v1.03
Main Features

Main Page
-
- User can see all games occuring on any particular day
- User can see the start times of games in their local time
- Once a game has started, user can see the live score of games
- Once a game has started, user can see the live data of the time remaining in games
- Once a game has started, user can easily see which team is winning as their score will be bolded in comparison to the losing team
- Once a game has ended, user will see that the game score is FINAL
- Once a game has ended, if user has made a pick the game in the list will turn green for a correct pick or red for an incorrect pick
- User can change to the next or previous day from the current one via right and left chevron arrows respectively
- User can change to any selected date via a date picker

Game Details Page
-
- Shown on click of individual game in list of Main Page
- If game has not started, user can make their pick for which team they believe will win the game
- On open, user will see their existing pick for which team they believe will win if they have made a pick for the game previously
- Once a game has started, user can no longer make a pick or update their existing pick
- User can see all game data they could on the Main Page (game start time, score, team records, time remaining in game)

Stats Page
-
- Shows user how many picks they have made total
- Shows user how many correct picks they have made total
- Shows user their percentage of correct picks compared to total picks

Standings Page
-
- Shows user the current NHL standings with four different rankings views: Wild Card, Division, Conference, League

About Page
-
- A little about the app and me as a developer as well as options to give feedback

Settings Page
-
- Gives user option to turn on or off daily reminder notification to make their picks for the day

---------------
Future and Desired Features
-
- Swipe left and right from game to game within Game Details Page
- Improved resolution support
- Multiplayer aspect, user can see other user's pick scores for the season and individual games
- Other stuff to be added here as it's thought of
