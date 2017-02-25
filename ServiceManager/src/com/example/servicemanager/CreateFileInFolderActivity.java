package com.example.servicemanager;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveApi.DriveContentsResult;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveFolder.DriveFileResult;
import com.google.android.gms.drive.MetadataChangeSet;

import static com.example.servicemanager.Constants.DB_NAME;
import static com.example.servicemanager.Constants.FILE_NAME;
import static com.example.servicemanager.Utils.getDateString;
import static com.example.servicemanager.Utils.getExternalFolderPath;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.Override;
import java.util.Calendar;
import org.apache.commons.io.IOUtils;


public class CreateFileInFolderActivity extends SyncWithDrive {

  @Override
  public void onConnected(Bundle connectionHint) {
    Drive.DriveApi.newDriveContents(getGoogleApiClient()).setResultCallback(driveContentsCallback);
  }

  final private ResultCallback<DriveContentsResult> driveContentsCallback =
      new ResultCallback<DriveContentsResult>() {

        @Override
        public void onResult(DriveContentsResult result) {
          if (result.getStatus().isSuccess()) {
            try {
              createDBBackupOnGoogleDrive(result);
            } catch (IOException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
          }
        }
      };

  public void createDBBackupOnGoogleDrive(DriveContentsResult result) throws IOException {

    try {
      backupDatabase(getApplicationContext());
    } catch (IOException e) {
      Log.e(TAG, e.getMessage());
    }

    final DriveContents driveContents = result.getDriveContents();
    InputStream mInput = new FileInputStream(getExternalFolderPath() + "/" + DB_NAME);
    OutputStream outputStream = driveContents.getOutputStream();
    Writer writer = new OutputStreamWriter(outputStream);
    IOUtils.copy(mInput, outputStream);

    MetadataChangeSet changeSet =
        new MetadataChangeSet.Builder().setTitle(FILE_NAME + getDateString(Calendar.getInstance()))
            .setMimeType("text/plain").setStarred(true).build();

    DriveFolder driveFolder = Drive.DriveApi.getFolder(getGoogleApiClient(), driveFolderId);

    driveFolder.createFile(getGoogleApiClient(), changeSet, result.getDriveContents())
        .setResultCallback(createFileCallback);
  }

  final private ResultCallback<DriveFileResult> createFileCallback =
      new ResultCallback<DriveFileResult>() {
        @Override
        public void onResult(DriveFileResult result) {
          if (!result.getStatus().isSuccess()) {
            showMessage("Error while trying to create the file");
            return;
          }
          showMessage("Created a file: " + result.getDriveFile().getDriveId());
          setDriveFileId(result.getDriveFile().getDriveId());
          storeDriveFileIdToDb(result.getDriveFile().getDriveId());

          finish();
          moveTaskToBack(true);
          System.exit(1);
          return;
        }
      };
}
