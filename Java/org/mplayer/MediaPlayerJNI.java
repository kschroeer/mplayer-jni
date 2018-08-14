package org.mplayer;

import org.mplayer.structures.MCI_Generic_Parms;
import org.mplayer.utils.StringPointer;

/**
 * This class loads the dll library and provides the native methods.
 * From this class the C header will be created.
 *
 * @author <a href="mailto:acsf.dev@gmail.com">Kay Schröer</a>
 */
public class MediaPlayerJNI {
  public static native boolean mciGetErrorString(long mciErr, StringPointer text, int length);
  public static native long mciSendCommand(int mciId, int message, long param1, MCI_Generic_Parms param2);

  static {
    System.loadLibrary("WinmmNative");
  }
}
