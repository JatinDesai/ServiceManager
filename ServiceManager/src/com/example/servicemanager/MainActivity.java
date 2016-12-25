package com.example.servicemanager;

import static com.example.servicemanager.Constants.*;
import static com.example.servicemanager.Utils.getInterval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

@SuppressWarnings("deprecation")
public class MainActivity extends ActionBarActivity {
  private GridView gridView;
  private GridViewAdapter gridAdapter;
  public AlarmManager alarmManager;
  Intent alarmIntent;
  PendingIntent pendingIntent;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    gridView = (GridView) findViewById(R.id.gridView);
    gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, getData());
    gridView.setAdapter(gridAdapter);

    gridView.setOnItemClickListener(new OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        ImageItem item = (ImageItem) parent.getItemAtPosition(position);

        if (item.getTitle().equals(ADD_CUSTOMER))
          insertCustomer();
        else if (item.getTitle().equals(SEARCH_CUSTOMER))
          searchCustomer();
        else if (item.getTitle().equals(DUE_SERVICES))
          dueServices();
        else if (item.getTitle().equals(IMPORT))
          importData();
        else if (item.getTitle().equals(UPCOMING_SERVICES))
          upcomingServices();
        else if (item.getTitle().equals(SETTINGS))
          settings();
      }
    });

    setAlarm();
    // TODO inprogress
    // createFolderOnGoogleDriveForBkp();
  }

  /*
   * public void createFolderOnGoogleDriveForBkp() { Intent intent1 = new
   * Intent(getApplicationContext(), com.example.servicemanager.CreateFolderActivity.class);
   * startActivity(intent1); }
   */

  private void settings() {
    Intent intent = new Intent(getApplicationContext(), com.example.servicemanager.Settings.class);
    startActivity(intent);
  }

  public void setAlarm() {
    alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

    alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
    pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, 0);

    Calendar alarmStartTime = Calendar.getInstance();
    alarmStartTime.add(Calendar.MINUTE, 2);
    alarmManager.setRepeating(AlarmManager.RTC, alarmStartTime.getTimeInMillis(), getInterval(),
        pendingIntent);
  }

  /**
   * Prepare some dummy data for gridview
   */
  @SuppressLint("Recycle")
  private ArrayList<ImageItem> getData() {
    final ArrayList<ImageItem> imageItems = new ArrayList<ImageItem>();
    TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
    for (int i = 0; i < imgs.length(); i++) {
      Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
      if (i == 0)
        imageItems.add(new ImageItem(bitmap, ADD_CUSTOMER));
      else if (i == 1)
        imageItems.add(new ImageItem(bitmap, SEARCH_CUSTOMER));
      else if (i == 2)
        imageItems.add(new ImageItem(bitmap, DUE_SERVICES));
      else if (i == 3)
        imageItems.add(new ImageItem(bitmap, IMPORT));
      else if (i == 4)
        imageItems.add(new ImageItem(bitmap, UPCOMING_SERVICES));
      else if (i == 5)
        imageItems.add(new ImageItem(bitmap, SETTINGS));
    }
    return imageItems;
  }

  // @Override
  // public boolean onCreateOptionsMenu(Menu menu) {
  // getMenuInflater().inflate(R.menu.main, menu);
  // return true;
  // }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_settings) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  public void dueServices() {
    Intent intent =
        new Intent(getApplicationContext(), com.example.servicemanager.SearchCustomer.class);
    intent.putExtra(CALLED_FROM, DUE_SERVICES);
    startActivity(intent);
  }

  // TODO need to modify logic
  public void upcomingServices() {
    Intent intent =
        new Intent(getApplicationContext(), com.example.servicemanager.SearchCustomer.class);
    intent.putExtra(CALLED_FROM, UPCOMING_SERVICES);
    startActivity(intent);
  }

  public void searchCustomer() {
    Intent intent =
        new Intent(getApplicationContext(), com.example.servicemanager.SearchCustomer.class);
    intent.putExtra(CALLED_FROM, SEARCH_CUSTOMER);
    startActivity(intent);
  }

  public void insertCustomer() {
    if (IMEIs.contains(getIMEINumber())) {
      Intent intent =
          new Intent(getApplicationContext(), com.example.servicemanager.NewCustomer.class);
      startActivity(intent);
    } else {
      promptLicenseError();
    }
  }

  public void updateCustomer() {
    insertCustomer();
  }

  // This is to export data to google drive
  public void syncDataWithDrive() {
    Intent intent =
        new Intent(getApplicationContext(), com.example.servicemanager.SyncWithDrive.class);
    finish();
    startActivity(intent);
  }

  public void importData() {

    if (IMEIs.contains(getIMEINumber())) {
      this.deleteDatabase(DB_NAME);
      TestAdapter mDbHelper = new TestAdapter(this);
      mDbHelper.createDatabase();
      mDbHelper.open();
      mDbHelper.close();

      new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
          .setTitle("Reset Services").setMessage("Data restored.").setPositiveButton("ok", null)
          .show();
    } else {
      promptLicenseError();
    }
  }

  public String getIMEINumber() {
    return ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId()
        .toString();
  }

  public void promptLicenseError() {
    new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle("License Error")
        .setMessage("A license error has occurred. Contact on + 91 9898988744 for support")
        .setPositiveButton("ok", null).show();
  }

  @Override
  public void onBackPressed() {
    syncDataWithDrive();
  }
}
