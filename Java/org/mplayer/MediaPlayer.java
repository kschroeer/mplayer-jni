package org.mplayer;

import org.mplayer.enumerations.MPDeviceType;
import org.mplayer.enumerations.MPMode;
import org.mplayer.enumerations.MPTimeFormat;
import org.mplayer.exceptions.MCIDeviceError;
import org.mplayer.structures.MCI_Generic_Parms;
import org.mplayer.structures.MCI_GetDevCaps_Parms;
import org.mplayer.structures.MCI_Open_Parms;
import org.mplayer.structures.MCI_Play_Parms;
import org.mplayer.structures.MCI_Record_Parms;
import org.mplayer.structures.MCI_Save_Parms;
import org.mplayer.structures.MCI_Seek_Parms;
import org.mplayer.structures.MCI_Set_Parms;
import org.mplayer.structures.MCI_Status_Parms;
import org.mplayer.utils.StringPointer;

/**
 * Media player wrapper.
 *
 * @author <a href="mailto:acsf.dev@gmail.com">Kay Schröer</a>
 */
public class MediaPlayer {
  private boolean autoRewind;
  private boolean canEject;
  private boolean canPlay;
  private boolean canRecord;
  private int deviceID;
  private MPDeviceType deviceType;
  private long error;
  private String fileName;
  private long flags;
  private long from;
  private boolean mciOpened;
  private boolean shareable;
  private long to;
  private boolean useFrom;
  private boolean useTo;
  private boolean useWait;
  private boolean wait;

  private final String[] DEVICE_NAME = {
    "", "AVIVideo", "CDAudio", "DAT",
    "DigitalVideo", "MMMovie", "Other", "Overlay", "Scanner", "Sequencer",
    "VCR", "Videodisc", "WaveAudio"
  };

  // String resource number bases (internal use)
  private final int MCI_STRING_OFFSET = 512;

  // MCI command message identifiers
  private final int MCI_OPEN = 0x00000803;
  private final int MCI_CLOSE = 0x00000804;
  private final int MCI_PLAY = 0x00000806;
  private final int MCI_SEEK = 0x00000807;
  private final int MCI_STOP = 0x00000808;
  private final int MCI_PAUSE = 0x00000809;
  private final int MCI_GETDEVCAPS = 0x0000080B;
  private final int MCI_SET = 0x0000080D;
  private final int MCI_RECORD = 0x0000080F;
  private final int MCI_SAVE = 0x00000813;
  private final int MCI_STATUS = 0x00000814;
  private final int MCI_RESUME = 0x00000855;

  // constants for predefined MCI device types
  private final int MCI_DEVTYPE_VCR = MCI_STRING_OFFSET + 1;
  private final int MCI_DEVTYPE_VIDEODISC = MCI_STRING_OFFSET + 2;
  private final int MCI_DEVTYPE_OVERLAY = MCI_STRING_OFFSET + 3;
  private final int MCI_DEVTYPE_CD_AUDIO = MCI_STRING_OFFSET + 4;
  private final int MCI_DEVTYPE_DAT = MCI_STRING_OFFSET + 5;
  private final int MCI_DEVTYPE_SCANNER = MCI_STRING_OFFSET + 6;
  private final int MCI_DEVTYPE_ANIMATION = MCI_STRING_OFFSET + 7;
  private final int MCI_DEVTYPE_DIGITAL_VIDEO = MCI_STRING_OFFSET + 8;
  private final int MCI_DEVTYPE_OTHER = MCI_STRING_OFFSET + 9;

  // common flags for dwFlags parameter of MCI command messages
  private final long MCI_WAIT = 0x00000002;
  private final long MCI_FROM = 0x00000004;
  private final long MCI_TO = 0x00000008;
  private final long MCI_TRACK = 0x00000010;

  // flags for dwFlags parameter of MCI_OPEN command message
  private final long MCI_OPEN_SHAREABLE = 0x00000100;
  private final long MCI_OPEN_ELEMENT = 0x00000200;
  private final long MCI_OPEN_TYPE = 0x00002000;

  // flags for dwFlags parameter of MCI_SEEK command message
  private final long MCI_SEEK_TO_START = 0x00000100;
  private final long MCI_SEEK_TO_END = 0x00000200;

  // flags for dwFlags parameter of MCI_STATUS command message
  private final long MCI_STATUS_ITEM = 0x00000100;

  // flags for dwItem field of the MCI_STATUS_PARMS parameter block
  private final long MCI_STATUS_LENGTH = 0x00000001;
  private final long MCI_STATUS_POSITION = 0x00000002;
  private final long MCI_STATUS_NUMBER_OF_TRACKS = 0x00000003;
  private final long MCI_STATUS_MODE = 0x00000004;
  private final long MCI_STATUS_TIME_FORMAT = 0x00000006;

  // flags for dwFlags parameter of MCI_GETDEVCAPS command message
  private final long MCI_GETDEVCAPS_ITEM = 0x00000100;

  // flags for dwItem field of the MCI_GETDEVCAPS_PARMS parameter block
  private final long MCI_GETDEVCAPS_CAN_RECORD = 0x00000001;
  private final long MCI_GETDEVCAPS_CAN_EJECT = 0x00000007;
  private final long MCI_GETDEVCAPS_CAN_PLAY = 0x00000008;

  // flags for dwFlags parameter of MCI_SET command message
  private final long MCI_SET_DOOR_OPEN = 0x00000100;
  private final long MCI_SET_TIME_FORMAT = 0x00000400;

  // flags for dwFlags parameter of MCI_SAVE command message
  private final long MCI_SAVE_FILE = 0x00000100;

  public MediaPlayer() {
    autoRewind = true;
    deviceType = MPDeviceType.dtAutoSelect;
  }

  public boolean canEject() {
    return canEject;
  }

  public boolean canPlay() {
    return canPlay;
  }

  public boolean canRecord() {
    return canRecord;
  }

  public int getDeviceID() {
    return deviceID;
  }

  public MPDeviceType getDeviceType() {
    return deviceType;
  }

  public void setDeviceType(MPDeviceType value) {
    if (value != deviceType) {
      deviceType = value;
    }
  }

  public long getEndPos() {
    return to;
  }

  public void setEndPos(long value) {
    if (value != to) {
      to = value;
    }
    useTo = true;
  }

  public long getError() {
    return error;
  }

  public String getErrorMessage() {
    StringPointer errMsg = new StringPointer();
    if (!MediaPlayerJNI.mciGetErrorString(error, errMsg, 4096)) {
      return "SMCIUnknownError";
    } else {
      return errMsg.pointer;
    }
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String value) {
    fileName = value;
  }

  public long getLength() throws MCIDeviceError {
    checkIfOpen();

    flags = MCI_WAIT | MCI_STATUS_ITEM;

    MCI_Status_Parms statusParm = new MCI_Status_Parms();
    statusParm.dwItem = MCI_STATUS_LENGTH;

    error = MediaPlayerJNI.mciSendCommand(deviceID, MCI_STATUS, flags, statusParm);

    return statusParm.dwReturn;
  }

  public MPMode getMode() {
    flags = MCI_WAIT | MCI_STATUS_ITEM;

    MCI_Status_Parms statusParm = new MCI_Status_Parms();
    statusParm.dwItem = MCI_STATUS_MODE;

    error = MediaPlayerJNI.mciSendCommand(deviceID, MCI_STATUS, flags, statusParm);

    int ordinal = (int)(statusParm.dwReturn - 524);
    return MPMode.values()[ordinal];
  }

  public long getPosition() {
    flags = MCI_WAIT | MCI_STATUS_ITEM;

    MCI_Status_Parms statusParm = new MCI_Status_Parms();
    statusParm.dwItem = MCI_STATUS_POSITION;

    error = MediaPlayerJNI.mciSendCommand(deviceID, MCI_STATUS, flags, statusParm);

    return statusParm.dwReturn;
  }

  public void setPosition(long value) throws MCIDeviceError {
    checkIfOpen();

    flags = 0;

    if (useWait) {
      if (wait) {
        flags = MCI_WAIT;
      }
      useWait = false;
    } else {
      flags = MCI_WAIT;
    }

    flags |= MCI_TO;

    MCI_Seek_Parms seekParm = new MCI_Seek_Parms();
    seekParm.dwTo = value;

    error = MediaPlayerJNI.mciSendCommand(deviceID, MCI_SEEK, flags, seekParm);
  }

  public long getStartPos() {
    return from;
  }

  public void setStartPos(long value) {
    if (value != from) {
      from = value;
    }
    useFrom = true;
  }

  public MPTimeFormat getTimeFormat() throws MCIDeviceError {
    checkIfOpen();

    flags = MCI_WAIT | MCI_STATUS_ITEM;

    MCI_Status_Parms statusParm = new MCI_Status_Parms();
    statusParm.dwItem = MCI_STATUS_TIME_FORMAT;

    error = MediaPlayerJNI.mciSendCommand(deviceID, MCI_STATUS, flags, statusParm);

    int ordinal = (int)statusParm.dwReturn;
    return MPTimeFormat.values()[ordinal];
  }

  public void setTimeFormat(MPTimeFormat value) {
    flags = MCI_SET_TIME_FORMAT;

    MCI_Set_Parms setParm = new MCI_Set_Parms();
    setParm.dwTimeFormat = value.ordinal();

    error = MediaPlayerJNI.mciSendCommand(deviceID, MCI_SET, flags, setParm);
  }

  public long getTrackLength(int trackNum) throws MCIDeviceError {
    checkIfOpen();

    flags = MCI_WAIT | MCI_STATUS_ITEM | MCI_TRACK;

    MCI_Status_Parms statusParm = new MCI_Status_Parms();
    statusParm.dwItem = MCI_STATUS_LENGTH;
    statusParm.dwTrack = trackNum;

    MediaPlayerJNI.mciSendCommand(deviceID, MCI_STATUS, flags, statusParm);

    return statusParm.dwReturn;
  }

  public long getTrackPosition(int trackNum) {
    flags = MCI_WAIT | MCI_STATUS_ITEM | MCI_TRACK;

    MCI_Status_Parms statusParm = new MCI_Status_Parms();
    statusParm.dwItem = MCI_STATUS_POSITION;
    statusParm.dwTrack = trackNum;

    MediaPlayerJNI.mciSendCommand(deviceID, MCI_STATUS, flags, statusParm);

    return statusParm.dwReturn;
  }

  public int getTracks() throws MCIDeviceError {
    checkIfOpen();

    flags = MCI_WAIT | MCI_STATUS_ITEM;

    MCI_Status_Parms statusParm = new MCI_Status_Parms();
    statusParm.dwItem = MCI_STATUS_NUMBER_OF_TRACKS;

    error = MediaPlayerJNI.mciSendCommand(deviceID, MCI_STATUS, flags, statusParm);

    return (int)statusParm.dwReturn;
  }

  public boolean getWait() {
    return wait;
  }

  public void setWait(boolean value) {
    if (value != wait) {
      wait = value;
    }
    useWait = true;
  }

  public void close() {
    if (deviceID != 0) {
      flags = 0;

      if (useWait) {
        if (wait) {
          flags = MCI_WAIT;
        }
        useWait = false;
      } else {
        flags = MCI_WAIT;
      }

      MCI_Generic_Parms genParm = new MCI_Generic_Parms();
      genParm.dwCallback = 0;

      error = MediaPlayerJNI.mciSendCommand(deviceID, MCI_CLOSE, flags, genParm);

      if (error == 0) {
        mciOpened = false;
        deviceID = 0;
      }
    }
  }

  public void eject() throws MCIDeviceError {
    checkIfOpen();

    if (canEject) {
      flags = 0;

      if (useWait) {
        if (wait) {
          flags = MCI_WAIT;
        }
        useWait = false;
      } else {
        flags = MCI_WAIT;
      }

      flags |= MCI_SET_DOOR_OPEN;

      MCI_Set_Parms setParm = new MCI_Set_Parms();

      error = MediaPlayerJNI.mciSendCommand(deviceID, MCI_SET, flags, setParm);
    }
  }

  public void next() throws MCIDeviceError {
    checkIfOpen();

    flags = 0;

    if (useWait) {
      if (wait) {
        flags = MCI_WAIT;
      }
      useWait = false;
    } else {
      flags = MCI_WAIT;
    }

    long tempFlags = flags;
    MCI_Seek_Parms seekParm = new MCI_Seek_Parms();

    if (getTimeFormat() == MPTimeFormat.tfTMSF) {
      if (getMode() == MPMode.mpPlaying) {
        if (mciTMSFTrack(getPosition()) == getTracks()) {
          setStartPos(getTrackPosition(getTracks()));
        } else {
          setStartPos(getTrackPosition((mciTMSFTrack(getPosition())) + 1));
        }
        play();
        return;
      } else {
        if (mciTMSFTrack(getPosition()) == getTracks()) {
          seekParm.dwTo = getTrackPosition(getTracks());
        } else {
          seekParm.dwTo = getTrackPosition((mciTMSFTrack(getPosition())) + 1);
        }
        flags = tempFlags | MCI_TO;
      }
    } else {
      flags = tempFlags | MCI_SEEK_TO_END;
    }

    error = MediaPlayerJNI.mciSendCommand(deviceID, MCI_SEEK, flags, seekParm);
  }

  public void open() throws MCIDeviceError {
    if (mciOpened) {
      close();
    }

    MCI_Open_Parms openParm = new MCI_Open_Parms();
    openParm.dwCallback = 0;
    openParm.lpstrDeviceType = DEVICE_NAME[deviceType.ordinal()];
    openParm.lpstrElementName = fileName;

    flags = 0;

    if (useWait) {
      if (wait) {
        flags = MCI_WAIT;
      }
      useWait = false;
    } else {
      flags = MCI_WAIT;
    }

    if (deviceType != MPDeviceType.dtAutoSelect) {
      flags |= MCI_OPEN_TYPE;
    } else {
      flags |= MCI_OPEN_ELEMENT;
    }

    if (shareable) {
      flags |= MCI_OPEN_SHAREABLE;
    }

    error = MediaPlayerJNI.mciSendCommand(0, MCI_OPEN, flags, openParm);

    if (error != 0) {
      throw new MCIDeviceError();
    } else {
      mciOpened = true;
      deviceID = openParm.wDeviceID;
      getDeviceCaps();
      if (deviceType == MPDeviceType.dtCDAudio || deviceType == MPDeviceType.dtVideodisc) {
        setTimeFormat(MPTimeFormat.tfTMSF);
      }
    }
  }

  public void pause() throws MCIDeviceError {
    if (!mciOpened) {
      throw new MCIDeviceError("sNotOpenErr");
    }

    if (getMode() == MPMode.mpPlaying) {
      pauseOnly();
    } else {
      if (getMode() == MPMode.mpPaused) {
        resume();
      }
    }
  }

  public void pauseOnly() throws MCIDeviceError {
    checkIfOpen();

    flags = 0;

    if (useWait) {
      if (wait) {
        flags = MCI_WAIT;
      }
      useWait = false;
    } else {
      flags = MCI_WAIT;
    }

    MCI_Generic_Parms genParm = new MCI_Generic_Parms();
    genParm.dwCallback = 0;

    error = MediaPlayerJNI.mciSendCommand(deviceID, MCI_PAUSE, flags, genParm);
  }

  public void play() throws MCIDeviceError {
    checkIfOpen();

    if (autoRewind && getPosition() == getLength()) {
      if (!useFrom && !useTo) {
        rewind();
      }
    }

    MCI_Play_Parms playParm = new MCI_Play_Parms();
    playParm.dwCallback = 0;

    flags = 0;

    if (useWait) {
      if (wait) {
        flags |= MCI_WAIT;
      }
      useWait = false;
    }

    if (useFrom) {
      flags |= MCI_FROM;
      playParm.dwFrom = from;
      useFrom = false;
    }

    if (useTo) {
      flags |= MCI_TO;
      playParm.dwTo = to;
      useTo = false;
    }

    error = MediaPlayerJNI.mciSendCommand(deviceID, MCI_PLAY, flags, playParm);
  }

  public void previous() throws MCIDeviceError {
    checkIfOpen();

    flags = 0;

    if (useWait) {
      if (wait) {
        flags = MCI_WAIT;
      }
      useWait = false;
    } else {
      flags = MCI_WAIT;
    }

    long tempFlags = flags;
    MCI_Seek_Parms seekParm = new MCI_Seek_Parms();

    if (getTimeFormat() == MPTimeFormat.tfTMSF) {
      long cpos = getPosition();
      long tpos = getTrackPosition(mciTMSFTrack(getPosition()));

      if (getMode() == MPMode.mpPlaying) {
        if (mciTMSFTrack(cpos) != 1
          && mciTMSFMinute(cpos) == mciTMSFMinute(tpos)
          && mciTMSFSecond(cpos) == mciTMSFSecond(tpos)) {
          setStartPos(getTrackPosition(mciTMSFTrack(getPosition()) - 1));
        } else {
          setStartPos(tpos);
        }
        play();
        return;
      } else {
        if (mciTMSFTrack(cpos) != 1
          && mciTMSFMinute(cpos) == mciTMSFMinute(tpos)
          && mciTMSFSecond(cpos) == mciTMSFSecond(tpos)) {
          seekParm.dwTo = getTrackPosition(mciTMSFTrack(getPosition()) - 1);
        } else {
          seekParm.dwTo = tpos;
        }
        flags = tempFlags | MCI_TO;
      }
    } else {
      flags = tempFlags | MCI_SEEK_TO_START;
    }

    error = MediaPlayerJNI.mciSendCommand(deviceID, MCI_SEEK, flags, seekParm);
  }

  public void resume() throws MCIDeviceError {
    checkIfOpen();

    flags = 0;

    if (useWait) {
      if (wait) {
        flags |= MCI_WAIT;
      }
    }

    MCI_Generic_Parms genParm = new MCI_Generic_Parms();
    genParm.dwCallback = 0;

    error = MediaPlayerJNI.mciSendCommand(deviceID, MCI_RESUME, flags, genParm);

    if (error != 0) {
      play();
    }
  }

  public void rewind() throws MCIDeviceError {
    checkIfOpen();

    long rFlags = MCI_WAIT | MCI_SEEK_TO_START;
    MCI_Seek_Parms seekParm = new MCI_Seek_Parms();

    MediaPlayerJNI.mciSendCommand(deviceID, MCI_SEEK, rFlags, seekParm);
  }

  public void save() throws MCIDeviceError {
    checkIfOpen();

    if (!fileName.equals("")) {
      MCI_Save_Parms saveParm = new MCI_Save_Parms();
      saveParm.lpfilename = fileName;

      flags = 0;

      if (useWait) {
        if (wait) {
          flags = MCI_WAIT;
        }
        useWait = false;
      } else {
        flags = MCI_WAIT;
      }

      flags |= MCI_SAVE_FILE;

      error = MediaPlayerJNI.mciSendCommand(deviceID, MCI_SAVE, flags, saveParm);
    }
  }

  public void startRecording() throws MCIDeviceError {
    checkIfOpen();

    flags = 0;

    if (useWait) {
      if (wait) {
        flags |= MCI_WAIT;
      }
      useWait = false;
    }

    MCI_Record_Parms recordParm = new MCI_Record_Parms();

    if (useFrom) {
      flags |= MCI_FROM;
      recordParm.dwFrom = from;
      useFrom = false;
    }

    if (useTo) {
      flags |= MCI_TO;
      recordParm.dwTo = to;
      useTo = false;
    }

    error = MediaPlayerJNI.mciSendCommand(deviceID, MCI_RECORD, flags, recordParm);
  }

  public void stop() throws MCIDeviceError {
    checkIfOpen();

    flags = 0;

    if (useWait) {
      if (wait) {
        flags = MCI_WAIT;
      }
      useWait = false;
    } else {
      flags = MCI_WAIT;
    }

    MCI_Generic_Parms genParm = new MCI_Generic_Parms();
    genParm.dwCallback = 0;

    error = MediaPlayerJNI.mciSendCommand(deviceID, MCI_STOP, flags, genParm);
  }

  private void checkIfOpen() throws MCIDeviceError {
    if (!mciOpened) {
      throw new MCIDeviceError("sNotOpenErr");
    }
  }

  private void getDeviceCaps() {
    flags = MCI_WAIT | MCI_GETDEVCAPS_ITEM;
    MCI_GetDevCaps_Parms devCapParm = new MCI_GetDevCaps_Parms();

    devCapParm.dwItem = MCI_GETDEVCAPS_CAN_PLAY;
    MediaPlayerJNI.mciSendCommand(deviceID, MCI_GETDEVCAPS, flags, devCapParm);
    canPlay = devCapParm.dwReturn == 0;

    devCapParm.dwItem = MCI_GETDEVCAPS_CAN_RECORD;
    MediaPlayerJNI.mciSendCommand(deviceID, MCI_GETDEVCAPS, flags, devCapParm);
    canRecord = devCapParm.dwReturn == 0;

    devCapParm.dwItem = MCI_GETDEVCAPS_CAN_EJECT;
    MediaPlayerJNI.mciSendCommand(deviceID, MCI_GETDEVCAPS, flags, devCapParm);
    canEject = devCapParm.dwReturn == 0;
  }

  private byte mciTMSFMinute(long tmsf) {
    return HIBYTE(LOWORD(tmsf));
  }

  private byte mciTMSFSecond(long tmsf) {
    return LOBYTE(HIWORD(tmsf));
  }

  private byte mciTMSFTrack(long tmsf) {
    return LOBYTE(LOWORD(tmsf));
  }

  private short LOWORD(long data) {
    return (short)(data & 0xffff);
  }

  private short HIWORD(long data) {
    return (short)(data >> 16);
  }

  private byte LOBYTE(short data) {
    return (byte)(data & 0xff);
  }

  private byte HIBYTE(short data) {
    return (byte)(data >> 8);
  }
}
