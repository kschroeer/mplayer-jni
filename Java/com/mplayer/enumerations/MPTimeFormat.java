package com.mplayer.enumerations;

/**
 * This enumeration is used to set the format of the value which is returned by
 * getting the track length or the current playback position.
 *
 * @author <a href="mailto:acsf.dev@gmail.com">Kay Schröer</a>
 */
public enum MPTimeFormat {
  tfMilliseconds, tfHMS, tfMSF, tfFrames, tfSMPTE24, tfSMPTE25,
  tfSMPTE30, tfSMPTE30Drop, tfBytes, tfSamples, tfTMSF;
}
