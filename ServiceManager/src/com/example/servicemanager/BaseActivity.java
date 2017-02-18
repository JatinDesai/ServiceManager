package com.example.servicemanager;

import static com.example.servicemanager.Constants.*;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BaseActivity extends SyncWithDrive {
  private DBHelper smDevDb;
  //private GoogleApiClient mGoogleApiClient;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    //super.onCreate(savedInstanceState);
    //setContentView(R.layout.login_activity);

    smDevDb = new DBHelper(this);

    String user = smDevDb.getGlobalParam(USER_NAME);    
    if (user.isEmpty()) {
      //createUser();
      
      super.onCreate(savedInstanceState); // this will ask for gmail id from parent class
      checkDriveToImportDb();
    } else {
      loginUser();
    }
  }

  public void checkDriveToImportDb() {
    Intent in = new Intent(getApplicationContext(), com.example.servicemanager.ImportDataFromDriveActivity.class);
    startActivity(in);
  }

  public void loginUser() {
    Intent in = new Intent(getApplicationContext(), com.example.servicemanager.LoginActivity.class);
    startActivity(in);
  }

  public void createUser() {
    Intent in = new Intent(getApplicationContext(), com.example.servicemanager.CreateUser.class);
    startActivity(in);
  }
}
