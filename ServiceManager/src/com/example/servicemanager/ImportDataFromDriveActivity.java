package com.example.servicemanager;

import static com.example.servicemanager.Constants.DB_NAME;
import java.io.InputStream;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.DriveApi.DriveContentsResult;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

public class ImportDataFromDriveActivity extends SyncWithDrive {

  InputStream inputStream;
  @Override
  public void onConnected(Bundle connectionHint) {
    if (checkIfFolderExistOnDrive()) {
      if(checkIfDriveFileExist()){

        //TODO this means we have file, then we need to import that to local db
        driveFile.open(mGoogleApiClient, DriveFile.MODE_READ_ONLY, null).setResultCallback(contentsOpenedCallback);
        
        this.deleteDatabase(DB_NAME);
        DataAdapter mDbHelper = new DataAdapter(this, inputStream);
        mDbHelper.createDatabase();
        mDbHelper.open();
        mDbHelper.close();

        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Reset Services").setMessage("Data restored.").setPositiveButton("ok", null)
            .show();
      }
      
    } else {
      createNewFolderOnDrive();
      // createFileInDriveFolder();
      
      //Open the Register User activity, as we don't have file to import from drive
      registerUser();
    }
  }
  
  final private ResultCallback<DriveContentsResult> contentsOpenedCallback =
      new ResultCallback<DriveContentsResult>() {
  @Override
  public void onResult(DriveContentsResult result) {
      if (!result.getStatus().isSuccess()) {
          // display an error saying file can't be opened
          return;
      }
      // DriveContents object contains pointers
      // to the actual byte stream
      DriveContents contents = result.getDriveContents();
      inputStream = contents.getInputStream();
  }
};

  public void createNewFolderOnDrive() {
    Intent intent1 =
        new Intent(getApplicationContext(), com.example.servicemanager.CreateFolderActivity.class);
    startActivity(intent1);
  }

  public void createFileInDriveFolder() {
    Intent intent2 = new Intent(getApplicationContext(),
        com.example.servicemanager.CreateFileInFolderActivity.class);
    startActivity(intent2);
  }

  public void registerUser() {
    Intent intent3 =
        new Intent(getApplicationContext(), com.example.servicemanager.CreateUser.class);
    startActivity(intent3);
  }
  
}