package org.mplayer.enumerations;

/**
 * This enumeration represents the current playback mode (f.e. that a file is
 * playing or stopped).
 *
 * @author <a href="mailto:acsf.dev@gmail.com">Kay Schröer</a>
 */
public enum MPMode {
  mpNotReady, mpStopped, mpPlaying, mpRecording, mpSeeking,
  mpPaused, mpOpen;
}
