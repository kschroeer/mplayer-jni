package com.mplayer.structures;

/**
 * The MCI_STATUS_PARMS structure contains information for the MCI_STATUS
 * command.
 *
 * @author <a href="mailto:acsf.dev@gmail.com">Kay Schröer</a>
 * @see <a href="https://docs.microsoft.com/de-de/windows/desktop/Multimedia/mci-status-parms">https://docs.microsoft.com/de-de/windows/desktop/Multimedia/mci-status-parms</a>
 */
public class MCI_Status_Parms extends MCI_Generic_Parms {
  public long dwReturn;
  public long dwItem;
  public long dwTrack;

  public MCI_Status_Parms() {
    super();

    dwReturn = 0;
    dwItem = 0;
    dwTrack = 0;
  }
}
