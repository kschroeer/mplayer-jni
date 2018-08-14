package org.mplayer.structures;

/**
 * The MCI_SAVE_PARMS structure contains the filename information for the
 * MCI_SAVE command.
 *
 * @author <a href="mailto:acsf.dev@gmail.com">Kay Schröer</a>
 * @see <a href="https://docs.microsoft.com/de-de/windows/desktop/Multimedia/mci-save-parms">https://docs.microsoft.com/de-de/windows/desktop/Multimedia/mci-save-parms</a>
 */
public class MCI_Save_Parms extends MCI_Generic_Parms {
  public String lpfilename;

  public MCI_Save_Parms() {
    super();

    lpfilename = "";
  }
}
