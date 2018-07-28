#include <jni.h>
#include <malloc.h>
#include <windows.h>
#include "MediaPlayerJNI.h"

JNIEXPORT jboolean JNICALL Java_com_mplayer_MediaPlayerJNI_mciGetErrorString
  (JNIEnv *env, jclass clazz, jlong mciErr, jobject text, jint length)
{
  jfieldID pointerField = (*env)->GetFieldID(env, text, "pointer", "Ljava/lang/String;");

  if (pointerField != NULL) {
    MCIERROR wError = (MCIERROR) mciErr;
    UINT uLength = (UINT) length;
    char *lpszErrorText = (char *) malloc(sizeof(char) * uLength);

    BOOL bReturn = mciGetErrorString(wError, lpszErrorText, uLength);

    jstring jerrorText = (*env)->NewStringUTF(env, lpszErrorText);
    (*env)->SetObjectField(env, text, pointerField, jerrorText);
    free(lpszErrorText);

    return bReturn;
  } else {
    return FALSE;
  }
}

JNIEXPORT jlong JNICALL Java_com_mplayer_MediaPlayerJNI_mciSendCommand
  (JNIEnv *env, jclass clazz, jint mciId, jint message, jlong param1, jobject param2)
{
  MCIDEVICEID wDeviceID = (MCIDEVICEID) mciId;
  UINT uMessage = (UINT) message;
  DWORD dwFlags = (DWORD) param1;

  switch (uMessage) {
  case MCI_CLOSE:
    {
      jclass closeParams = (*env)->FindClass(env, "Lcom/mplayer/structures/MCI_Generic_Parms;");
      if (closeParams != NULL) {
        MCI_GENERIC_PARMS lpClose;
        MCIERROR dwReturn = mciSendCommand(wDeviceID, uMessage, dwFlags, (DWORD_PTR) &lpClose);
        return dwReturn;
      }
    }
    break;
  case MCI_GETDEVCAPS:
    {
      jclass getDevCapsParams = (*env)->FindClass(env, "Lcom/mplayer/structures/MCI_GetDevCaps_Parms;");
      if (getDevCapsParams != NULL) {
        jfieldID returnField = (*env)->GetFieldID(env, getDevCapsParams, "dwReturn", "J");
        jfieldID itemField = (*env)->GetFieldID(env, getDevCapsParams, "dwItem", "J");

        if (returnField != NULL && itemField != NULL) {
          MCI_GETDEVCAPS_PARMS lpGetDevCaps;
          lpGetDevCaps.dwReturn = (DWORD) (*env)->GetLongField(env, param2, returnField);
          lpGetDevCaps.dwItem = (DWORD) (*env)->GetLongField(env, param2, itemField);

          MCIERROR dwReturn = mciSendCommand(wDeviceID, uMessage, dwFlags, (DWORD_PTR) &lpGetDevCaps);
          (*env)->SetLongField(env, param2, returnField, lpGetDevCaps.dwReturn);
          (*env)->SetLongField(env, param2, itemField, lpGetDevCaps.dwItem);

          return dwReturn;
        }
      }
    }
    break;
  case MCI_OPEN:
    {
      jclass openParams = (*env)->FindClass(env, "Lcom/mplayer/structures/MCI_Open_Parms;");
      if (openParams != NULL) {
        jfieldID deviceIDField = (*env)->GetFieldID(env, openParams, "wDeviceID", "I");
        jfieldID deviceTypeField = (*env)->GetFieldID(env, openParams, "lpstrDeviceType", "Ljava/lang/String;");
        jfieldID elementNameField = (*env)->GetFieldID(env, openParams, "lpstrElementName", "Ljava/lang/String;");

        if (deviceIDField != NULL && deviceTypeField != NULL && elementNameField != NULL) {
          jstring jdevType = (*env)->GetObjectField(env, param2, deviceTypeField);
          const char *deviceType = (*env)->GetStringUTFChars(env, jdevType, NULL);
          jstring jelemName = (*env)->GetObjectField(env, param2, elementNameField);
          const char *elementName = (*env)->GetStringUTFChars(env, jelemName, NULL);

          MCI_OPEN_PARMS lpOpen;
          lpOpen.wDeviceID = 0;
          lpOpen.lpstrDeviceType = deviceType;
          lpOpen.lpstrElementName = elementName;

          MCIERROR dwReturn = mciSendCommand(wDeviceID, uMessage, dwFlags, (DWORD_PTR) &lpOpen);
          (*env)->SetIntField(env, param2, deviceIDField, lpOpen.wDeviceID);

          (*env)->ReleaseStringUTFChars(env, jdevType, deviceType);
          (*env)->ReleaseStringUTFChars(env, jelemName, elementName);

          return dwReturn;
        }
      }
    }
    break;
  case MCI_PAUSE:
    {
      jclass pauseParams = (*env)->FindClass(env, "Lcom/mplayer/structures/MCI_Generic_Parms;");
      if (pauseParams != NULL) {
        MCI_GENERIC_PARMS lpPause;
        MCIERROR dwReturn = mciSendCommand(wDeviceID, uMessage, dwFlags, (DWORD_PTR) &lpPause);
        return dwReturn;
      }
    }
    break;
  case MCI_PLAY:
    {
      jclass playParams = (*env)->FindClass(env, "Lcom/mplayer/structures/MCI_Play_Parms;");
      if (playParams != NULL) {
        jfieldID fromField = (*env)->GetFieldID(env, playParams, "dwFrom", "J");
        jfieldID toField = (*env)->GetFieldID(env, playParams, "dwTo", "J");

        if (fromField != NULL && toField != NULL) {
          MCI_PLAY_PARMS lpPlay;
          lpPlay.dwFrom = (DWORD) (*env)->GetLongField(env, param2, fromField);
          lpPlay.dwTo = (DWORD) (*env)->GetLongField(env, param2, toField);

          MCIERROR dwReturn = mciSendCommand(wDeviceID, uMessage, dwFlags, (DWORD_PTR) &lpPlay);
          return dwReturn;
        }
      }
    }
    break;
  case MCI_RECORD:
    {
      jclass recordParams = (*env)->FindClass(env, "Lcom/mplayer/structures/MCI_Record_Parms;");
      if (recordParams != NULL) {
        jfieldID fromField = (*env)->GetFieldID(env, recordParams, "dwFrom", "J");
        jfieldID toField = (*env)->GetFieldID(env, recordParams, "dwTo", "J");

        if (fromField != NULL && toField != NULL) {
          MCI_RECORD_PARMS lpRecord;
          lpRecord.dwFrom = (DWORD) (*env)->GetLongField(env, param2, fromField);
          lpRecord.dwTo = (DWORD) (*env)->GetLongField(env, param2, toField);

          MCIERROR dwReturn = mciSendCommand(wDeviceID, uMessage, dwFlags, (DWORD_PTR) &lpRecord);
          return dwReturn;
        }
      }
    }
    break;
  case MCI_RESUME:
    {
      jclass resumeParams = (*env)->FindClass(env, "Lcom/mplayer/structures/MCI_Generic_Parms;");
      if (resumeParams != NULL) {
        MCI_GENERIC_PARMS lpResume;
        MCIERROR dwReturn = mciSendCommand(wDeviceID, uMessage, dwFlags, (DWORD_PTR) &lpResume);
        return dwReturn;
      }
    }
    break;
  case MCI_SAVE:
    {
      jclass saveParams = (*env)->FindClass(env, "Lcom/mplayer/structures/MCI_Save_Parms;");
      if (saveParams != NULL) {
        jfieldID filenameField = (*env)->GetFieldID(env, saveParams, "lpfilename", "Ljava/lang/String;");

        if (filenameField != NULL) {
          jstring jfileName = (*env)->GetObjectField(env, param2, filenameField);
          const char *fileName = (*env)->GetStringUTFChars(env, jfileName, NULL);

          MCI_SAVE_PARMS lpSave;
          lpSave.lpfilename = fileName;

          MCIERROR dwReturn = mciSendCommand(wDeviceID, uMessage, dwFlags, (DWORD_PTR) &lpSave);
          (*env)->ReleaseStringUTFChars(env, jfileName, fileName);

          return dwReturn;
        }
      }
    }
    break;
  case MCI_SEEK:
    {
      jclass seekParams = (*env)->FindClass(env, "Lcom/mplayer/structures/MCI_Seek_Parms;");
      if (seekParams != NULL) {
        jfieldID toField = (*env)->GetFieldID(env, seekParams, "dwTo", "J");

        if (toField != NULL) {
          MCI_SEEK_PARMS lpSeek;
          lpSeek.dwTo = (DWORD) (*env)->GetLongField(env, param2, toField);

          MCIERROR dwReturn = mciSendCommand(wDeviceID, uMessage, dwFlags, (DWORD_PTR) &lpSeek);
          return dwReturn;
        }
      }
    }
    break;
  case MCI_SET:
    {
      jclass setParams = (*env)->FindClass(env, "Lcom/mplayer/structures/MCI_Set_Parms;");
      if (setParams != NULL) {
        jfieldID timeFormatField = (*env)->GetFieldID(env, setParams, "dwTimeFormat", "J");
        jfieldID audioField = (*env)->GetFieldID(env, setParams, "dwAudio", "J");

        if (timeFormatField != NULL && audioField != NULL) {
          MCI_SET_PARMS lpSet;
          lpSet.dwTimeFormat = (DWORD) (*env)->GetLongField(env, param2, timeFormatField);
          lpSet.dwAudio = (DWORD) (*env)->GetLongField(env, param2, audioField);

          MCIERROR dwReturn = mciSendCommand(wDeviceID, uMessage, dwFlags, (DWORD_PTR) &lpSet);
          return dwReturn;
        }
      }
    }
    break;
  case MCI_STATUS:
    {
      jclass statusParams = (*env)->FindClass(env, "Lcom/mplayer/structures/MCI_Status_Parms;");
      if (statusParams != NULL) {
        jfieldID returnField = (*env)->GetFieldID(env, statusParams, "dwReturn", "J");
        jfieldID itemField = (*env)->GetFieldID(env, statusParams, "dwItem", "J");
        jfieldID trackField = (*env)->GetFieldID(env, statusParams, "dwTrack", "J");

        if (returnField != NULL && itemField != NULL && trackField != NULL) {
          MCI_STATUS_PARMS lpStatus;
          lpStatus.dwReturn = (DWORD) (*env)->GetLongField(env, param2, returnField);
          lpStatus.dwItem = (DWORD) (*env)->GetLongField(env, param2, itemField);
          lpStatus.dwTrack = (DWORD) (*env)->GetLongField(env, param2, trackField);

          MCIERROR dwReturn = mciSendCommand(wDeviceID, uMessage, dwFlags, (DWORD_PTR) &lpStatus);
          (*env)->SetLongField(env, param2, returnField, lpStatus.dwReturn);
          (*env)->SetLongField(env, param2, itemField, lpStatus.dwItem);
          (*env)->SetLongField(env, param2, trackField, lpStatus.dwTrack);

          return dwReturn;
        }
      }
    }
    break;
  case MCI_STOP:
    {
      jclass stopParams = (*env)->FindClass(env, "Lcom/mplayer/structures/MCI_Generic_Parms;");
      if (stopParams != NULL) {
        MCI_GENERIC_PARMS lpStop;
        MCIERROR dwReturn = mciSendCommand(wDeviceID, uMessage, dwFlags, (DWORD_PTR) &lpStop);
        return dwReturn;
      }
    }
    break;
  }

  return 0;
}
