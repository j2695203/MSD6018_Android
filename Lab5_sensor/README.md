# Lab 5: Fun with sensors
## Due Tuesday, Oct 3

In this lab we'll make an app that simulates a "marble" rolling around on the screen.  I'll include some recommendations for approaching this, but feel free to make modifications if you'd like (check with me though)

### Suggestions/Notes:

* Use the gravity sensor to detect the phone's orientation (there are other ways, [like this](https://developer.android.com/guide/topics/sensors/sensors_position#sensors-pos-orient) if you're interested)
* Use the `offset` modifier to move an element around its parent.  It's probably good to make the "ball's" parent be a `BoxWithConstraints` so you can access its `maxWidth` and `maxHeight` in its children
* Either use a ViewModel's MutableLiveData or a LaunchedEffect to update the offset over time
* Beware the aliasing issue we discussed in class for `event.values`
* The sensor's `y` axis points up the screen, but the offset's coordinate system has `y` pointing down!
* The order of modifiers matters.  You probably want to apply offset first and then change the size of your element!
* Hit the ... button above the emulator to get the "extended controls" window, in virtual sensors, pick the "device pose" tab to move the emulated phone around
* You might want to ask for sensor events at `SENSOR_DELAY_GAME` frequency to get a smoother roll

### Submission:

Push your code to your git repo, be sure to include a screenshot, and ideally a screen recording



## Screen Recording of Submission
![This is an image](https://github.com/j2695203/MSD6018Repo/blob/5bee94c2b80221252a5a2ba67b3fab51bcddb9ba/Lab5_sensor/Screen%20Recording%20Lab5.mov)
