package org.mplayer.structures;

/**
 * The MCI_OPEN_PARMS structure contains information for the MCI_OPEN command.
 *
 * @author <a href="mailto:acsf.dev@gmail.com">Kay Schröer</a>
 * @see <a href="https://docs.microsoft.com/de-de/windows/desktop/Multimedia/mci-open-parms">https://docs.microsoft.com/de-de/windows/desktop/Multimedia/mci-open-parms</a>
 */
public class MCI_Open_Parms extends MCI_Generic_Parms {
  public int wDeviceID;
  public String lpstrDeviceType;
  public String lpstrElementName;
  public String lpstrAlias;

  public MCI_Open_Parms() {
    super();

    wDeviceID = 0;
    lpstrDeviceType = "";
    lpstrElementName = "";
    lpstrAlias = "";
  }
}
