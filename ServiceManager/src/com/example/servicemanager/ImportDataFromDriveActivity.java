package com.example.servicemanager;

import static com.example.servicemanager.Constants.DB_NAME;
import static com.example.servicemanager.Constants.FOLDER_NAME;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveApi.DriveContentsResult;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

public class ImportDataFromDriveActivity extends SyncWithDrive {

  InputStream inputStream;

  @Override
  public void onConnected(Bundle connectionHint) {
    if (checkIfFolderExistOnDrive()) {
      if (checkIfDriveFileExist()) {
        // TODO we have to import at very begining else it will not have
        /*String contents = null;
        // TODO this means we have file, then we need to import that to local db
        driveFile.open(mGoogleApiClient, DriveFile.MODE_READ_ONLY, null)
            .setResultCallback(contentsOpenedCallback);
        try {
          Thread.sleep(15000);
        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }*/


        ////
        /*
         * DriveContentsResult driveContentsResult = driveFile.open(getGoogleApiClient(),
         * DriveFile.MODE_READ_ONLY, null).setResultCallback(contentsOpenedCallback); if
         * (!driveContentsResult.getStatus().isSuccess()) { //return null; } DriveContents
         * driveContents = driveContentsResult.getDriveContents(); BufferedReader reader = new
         * BufferedReader( new InputStreamReader(driveContents.getInputStream())); StringBuilder
         * builder = new StringBuilder(); String line; try { while ((line = reader.readLine()) !=
         * null) { builder.append(line); } contents = builder.toString(); } catch (IOException e) {
         * Log.e(TAG, "IOException while reading from the stream", e); }
         * 
         * driveContents.discard(getGoogleApiClient());
         */
        ////


        this.downloadFromDrive(driveFile);



      } else {
        // TODO create new file, just folder exist
        createFileInDriveFolder();
      }

    } else {
      // createNewFolderOnDrive();
      // createFileInDriveFolder();

      // Open the Register User activity, as we don't have file to import from drive
      // registerUser();
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

  private void downloadFromDrive(DriveFile file) {
    file.open(mGoogleApiClient, DriveFile.MODE_READ_ONLY, null)
        .setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
          @Override
          public void onResult(DriveApi.DriveContentsResult result) {
            if (!result.getStatus().isSuccess()) {
              // showErrorDialog();
              return;
            }

            // DriveContents object contains pointers
            // to the actual byte stream
            DriveContents contents = result.getDriveContents();
            InputStream input = contents.getInputStream();

            try {
              
              String outFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + FOLDER_NAME + "/" + "SMDEV";
              File file = new File(outFileName);
              OutputStream output = new FileOutputStream(file);
              try {
                try {
                  byte[] buffer = new byte[4 * 1024]; // or other buffer size
                  int read;

                  while ((read = input.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                  }
                  output.flush();
                } finally {
                  output.close();
                }
              } catch (Exception e) {
                e.printStackTrace();
              }
            } catch (FileNotFoundException e) {
              e.printStackTrace();
            } finally {
              try {
                input.close();
                importNewFile();
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
          }
        });
  }

  public void importNewFile() {
    this.deleteDatabase(DB_NAME);
    DataAdapter mDbHelper = new DataAdapter(this, inputStream);
    mDbHelper.createDatabase();
    mDbHelper.open();
    mDbHelper.close();

    new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle("Reset Services").setMessage("Data restored.").setPositiveButton("ok", null)
        .show();
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
