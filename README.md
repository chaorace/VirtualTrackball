# VirtualTrackball

Virtual Trackball is a utility that emulates a trackball using mouse inputs. This tool is intended to be used with laptop trackpads to help ease the user experience without resorting to mouse acceleration. It should work on Windows, OSX, and Linux, but has only been tested on Windows.

# Use

Virtual Trackball has two operating modes: GUI and command line. The GUI mode is accessed by simply running the VirtualTrackball.jar file. The command line mode is accessed by running VirtualTrackball.jar with at least one flag (flags listed below). If you'd like to use the command line mode without changing the default settings, any fake flag will do the trick. Currently, the only way to end the command line version of the Virtual Trackball is by killing the Java environment from a task manager. This can be worked around by wrapping the command line version in a shell script and force-killing the shell script when you'd like to kill Virtual Trackball

# Flags

-p / --PollingRate [number]: 

The rate at which mouse movement is measured. Lower values are smoother, Higher values are less intensive. Changing this option directly affects how all other options are evaluated (don't change this unless you know what you're doing!)
Default value (10)

-t / --Tolerance [number]:

The lowest amount of movement that can start trackball spinning.
Default value (5)

-s / --Sensitivity [number]:

The lowest amount of movement that can interrupt trackball spinning.
Default value (1)

-f / --Friction [number]

The rate at which the trackball slows down. 1 eliminates all slowdown. >1 values cause the trackball to speed up.
Default value (.95)

# Downloads
[Github](https://github.com/chaorace/VirtualTrackball/releases)

# More Info

VirtualTrackball was made by Christopher Lathan Crockett in 2015 and is Licensed under the MIT License. It depends upon the following libraries:

[ScalaFX](https://github.com/scalafx/scalafx)
[JNativeHook](https://github.com/kwhat/jnativehook)
[JavaGeom](https://github.com/dlegland/javaGeom)
[JCommander](https://github.com/cbeust/jcommander)
