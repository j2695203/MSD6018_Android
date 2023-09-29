# Lab 4: Compose + Room + Repository + Web requests

### Description

In this lab we'll put together a bunch of stuff from the past 2 weeks into a simple app.

Start by creating an "empty activity" app that uses Compose (that's the default on the most recent version of Android Studio)

The app will need a button which will trigger a request to https://api.chucknorris.io/jokes/random which returns a random Chuck Norris joke.

Begin by displaying the most recent joke once you get a response.

Next, display all previous joke requests using a lazy list layout composable. Note, you'll want to use/create a viewmodel to track keep track of the jokes

Finally, add a Room data base to store the jokes persistently so users don't lose old jokes when the close and reopen the app.

** Notes
Feel free to use whatever HTTP request library you want Since we're using compose, you can add the Room compiler plugin stuff using KSP which should reduce the number of bugs/issues you have

Be sure to include screenshots of your app in your repo

### Screen Recording of Submission
![This is an image](https://github.com/j2695203/MSD6018Repo/blob/8418b09b16421516c179efe25a5c045ebb6a259d/Lab4/Lab4_recording.gif)
