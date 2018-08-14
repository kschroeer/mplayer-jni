package org.mplayer.structures;

/**
 * The MCI_PLAY_PARMS structure contains positioning information for the
 * MCI_PLAY command.
 *
 * @author <a href="mailto:acsf.dev@gmail.com">Kay Schröer</a>
 * @see <a href="https://docs.microsoft.com/de-de/windows/desktop/Multimedia/mci-play-parms">https://docs.microsoft.com/de-de/windows/desktop/Multimedia/mci-play-parms</a>
 */
public class MCI_Play_Parms extends MCI_Generic_Parms {
  public long dwFrom;
  public long dwTo;

  public MCI_Play_Parms() {
    super();

    dwFrom = 0;
    dwTo = 0;
  }
}
