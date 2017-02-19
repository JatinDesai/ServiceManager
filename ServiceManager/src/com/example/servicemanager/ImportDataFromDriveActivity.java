package com.example.servicemanager;

import android.content.Intent;
import android.os.Bundle;

public class ImportDataFromDriveActivity extends SyncWithDrive {

  @Override
  public void onConnected(Bundle connectionHint) {
    if (checkIfFolderExistOnDrive()) {
      if(checkIfDriveFileExist()){
        //TODO this means we have file, then we need to import that to local db
      }
      
    } else {
      createNewFolderOnDrive();
      // createFileInDriveFolder();
      
      //Open the Register User activity, as we don't have file to import from drive
      registerUser();
    }
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

  public void registerUser() {
    Intent intent3 =
        new Intent(getApplicationContext(), com.example.servicemanager.CreateUser.class);
    startActivity(intent3);
  }
  
}
