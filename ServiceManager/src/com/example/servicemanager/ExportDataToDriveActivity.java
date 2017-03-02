package com.example.servicemanager;

import android.content.Intent;
import android.os.Bundle;

public class ExportDataToDriveActivity extends SyncWithDrive {

  @Override
  public void onConnected(Bundle connectionHint) {
    // TODO in case we need to compare both the version gdrive and local before exporting
    if (checkIfFolderExistOnDrive()) {
      if(checkIfDriveFileExist()) {
        //If file, exist on gdrive,
        // do we need to compare before export? what we can compare, date, base version? 
        // if exist we can set params to DB and those can be used while comparing
        // For now create in all cases
        createFileInDriveFolder();
        
      } else {
          //TODO create new file, just folder exist
          createFileInDriveFolder();
      }
    } else {
      createNewFolderOnDrive();
      //createFileInDriveFolder();
    }
    
    // Just export file and store the drive file id to database
    /*if (checkIfFolderExistOnDrive()) {
      //TODO create new file, just folder exist
      createFileInDriveFolder();      
    } else {
      createNewFolderOnDrive();
      createFileInDriveFolder();
    }*/
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
