# MPlayer-JNI
@author Kay Schröer (acsf.dev@gmail.com)

This Java project provides support for the **winmm.dll** libary to play audio files and CDs.
It wraps some of the basic functions in a **MediaPlayer** class and should give an idea of how to use Java Native Interface (JNI) for system calls.

## Requirements

- Windows (any platform)
- Java Development Kit (JDK)
- Minimalist GNU for Windows (MinGW)

## Building

You can use the **Compile.bat** file to build the project. It works through the following steps:
- Compiling java classes
- Generating jni header
- Building dll library

## Demo

The Java folder contains a Test file which provides a console-based player demo. The audio file to play is passed as command-line parameter:
`java Test myfile.mp3`

And the player is controlled by pressing one of the characters the simple menu lists, f.e. 'p' for 'play' or 's' for 'stop'.

Please keep in mind that the WinmmNative.dll has to be stored in the same folder like the test application or in the java.library.path.
