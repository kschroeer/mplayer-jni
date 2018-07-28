package com.mplayer.structures;

/**
 * The MCI_SEEK_PARMS structure contains positioning information for the
 * MCI_SEEK command.
 *
 * @author <a href="mailto:acsf.dev@gmail.com">Kay Schröer</a>
 * @see <a href="https://docs.microsoft.com/de-de/windows/desktop/Multimedia/mci-seek-parms">https://docs.microsoft.com/de-de/windows/desktop/Multimedia/mci-seek-parms</a>
 */
public class MCI_Seek_Parms extends MCI_Generic_Parms {
  public long dwTo;

  public MCI_Seek_Parms() {
    super();

    dwTo = 0;
  }
}
