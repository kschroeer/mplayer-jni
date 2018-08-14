package org.mplayer.structures;

/**
 * The MCI_GETDEVCAPS_PARMS structure contains device-capability information
 * for the MCI_GETDEVCAPS command.
 *
 * @author <a href="mailto:acsf.dev@gmail.com">Kay Schröer</a>
 * @see <a href="https://docs.microsoft.com/de-de/windows/desktop/Multimedia/mci-getdevcaps-parms">https://docs.microsoft.com/de-de/windows/desktop/Multimedia/mci-getdevcaps-parms</a>
 */
public class MCI_GetDevCaps_Parms extends MCI_Generic_Parms {
  public long dwReturn;
  public long dwItem;

  public MCI_GetDevCaps_Parms() {
    super();

    dwReturn = 0;
    dwItem = 0;
  }
}
