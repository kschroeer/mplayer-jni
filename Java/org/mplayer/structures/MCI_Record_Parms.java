package org.mplayer.structures;

/**
 * The MCI_RECORD_PARMS structure contains positioning information for the
 * MCI_RECORD command.
 *
 * @author <a href="mailto:acsf.dev@gmail.com">Kay Schröer</a>
 * @see <a href="https://docs.microsoft.com/de-de/windows/desktop/Multimedia/mci-record-parms">https://docs.microsoft.com/de-de/windows/desktop/Multimedia/mci-record-parms</a>
 */
public class MCI_Record_Parms extends MCI_Generic_Parms {
  public long dwFrom;
  public long dwTo;

  public MCI_Record_Parms() {
    super();

    dwFrom = 0;
    dwTo = 0;
  }
}
