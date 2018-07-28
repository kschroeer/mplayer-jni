@echo off

echo Compiling java classes ...
cd Java
javac com/mplayer/MediaPlayer.java
javac Test.java

echo Generating jni header ...
javah -o ../C/MediaPlayerJNI.h com.mplayer.MediaPlayerJNI
cd ..

echo Building dll library ...
cd C
gcc -Wall -shared WinmmNative.c "-IC:/Program Files/Java/jdk1.8.0_171/include" "-IC:/Program Files/Java/jdk1.8.0_171/include/win32" -o WinmmNative.dll -lwinmm
cd ..
