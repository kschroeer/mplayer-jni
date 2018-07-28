package com.mplayer.structures;

/**
 * The MCI_GENERIC_PARMS structure is used for MCI command messages that have
 * empty parameter lists.
 *
 * @author <a href="mailto:acsf.dev@gmail.com">Kay Schröer</a>
 * @see <a href="https://docs.microsoft.com/de-de/windows/desktop/Multimedia/mci-generic-parms">https://docs.microsoft.com/de-de/windows/desktop/Multimedia/mci-generic-parms</a>
 */
public class MCI_Generic_Parms {
  public long dwCallback;

  public MCI_Generic_Parms() {
    dwCallback = 0;
  }
}
