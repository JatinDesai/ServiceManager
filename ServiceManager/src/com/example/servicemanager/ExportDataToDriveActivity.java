package com.example.servicemanager;

import android.content.Intent;
import android.os.Bundle;

public class ExportDataToDriveActivity extends SyncWithDrive {

  @Override
  public void onConnected(Bundle connectionHint) {
    if (checkIfFolderExistOnDrive()) {
      createFileInDriveFolder();
    } else {
      createNewFolderOnDrive();
      createFileInDriveFolder();
    }
    
    // Just export file and store the drive file id to database
  }

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
}
