package com.mplayer.structures;

/**
 * The MCI_SET_PARMS structure contains information for the MCI_SET command.
 *
 * @author <a href="mailto:acsf.dev@gmail.com">Kay Schröer</a>
 * @see <a href="https://docs.microsoft.com/de-de/windows/desktop/Multimedia/mci-set-parms">https://docs.microsoft.com/de-de/windows/desktop/Multimedia/mci-set-parms</a>
 */
public class MCI_Set_Parms extends MCI_Generic_Parms {
  public long dwTimeFormat;
  public long dwAudio;

  public MCI_Set_Parms() {
    super();

    dwTimeFormat = 0;
    dwAudio = 0;
  }
}
