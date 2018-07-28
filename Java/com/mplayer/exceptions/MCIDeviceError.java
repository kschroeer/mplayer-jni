package com.mplayer.exceptions;

/**
 * This exception is thrown if the interface recognizes an error (f.e. if the
 * play method is called without open the device first).
 *
 * @author <a href="mailto:acsf.dev@gmail.com">Kay Schröer</a>
 */
public class MCIDeviceError extends Exception {
  public MCIDeviceError() {
    super();
  }

  public MCIDeviceError(String message) {
    super(message);
  }
}
