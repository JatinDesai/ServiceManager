package com.example.servicemanager;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.MetadataChangeSet;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.gms.drive.DriveFolder.DriveFolderResult;
import com.google.android.gms.drive.DriveId;

public class CreateFolderActivity extends SyncWithDrive {

  @Override
  public void onConnected(Bundle connectionHint) {

    /*
     * if (checkIfDriveFolderExist()) { return; }
     */
    MetadataChangeSet changeSet =
        new MetadataChangeSet.Builder().setTitle(EXISTING_FOLDER_ID).build();
    Drive.DriveApi.getRootFolder(getGoogleApiClient()).createFolder(getGoogleApiClient(), changeSet)
        .setResultCallback(folderCreatedCallback);
  };

  final ResultCallback<DriveFolderResult> folderCreatedCallback =
      new ResultCallback<DriveFolderResult>() {
        @Override
        public void onResult(DriveFolderResult result) {
          if (!result.getStatus().isSuccess()) {
            showMessage("Error while trying to create the folder");
            return;
          }

          // TODO this needs to be stored in DB, as a follows
          // Parameter Name: DRIVE_FOLDER_ID
          // Parameter Value like : DRIVE_FOLDER_ID = "DriveId:CAESABisDCCEq5Gr3lEoAQ=="
          DriveId folderId = result.getDriveFolder().getDriveId();
          showMessage("Created a folder: " + folderId);

          setDriveFolderId(folderId);
          storeDriveFolderIdToDb(folderId);
          
          //create file after folder creation
          Intent intent2 = new Intent(getApplicationContext(),
              com.example.servicemanager.CreateFileInFolderActivity.class);
          startActivity(intent2);
        }
      };
}
