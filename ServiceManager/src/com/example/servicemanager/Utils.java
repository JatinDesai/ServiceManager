package com.example.servicemanager;

import static com.example.servicemanager.Constants.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.os.Environment;

public class Utils {

  public static String getDatabasePath(String dataDir, String packageName) {
    String dbPath = "";
    if (android.os.Build.VERSION.SDK_INT >= 17) {
      dbPath = dataDir + "/databases/";
    } else {
      dbPath = "/data/data/" + packageName + "/databases/";
    }
    return dbPath;
  }

  public static String getExternalFolderPath() {
    return Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + FOLDER_NAME;
  }

  public static int getInterval() {
    int seconds = 60;
    int minutes = 1;
    int milliseconds = 1000;
    int repeatMS = minutes * seconds * 2 * milliseconds;
    return repeatMS;
  }

  public static Calendar getDate(String date) {
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    try {
      cal.setTime(sdf.parse(date));
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return cal;
  }

  public static String getDateString(Calendar cal) {
    StringBuilder date = new StringBuilder().append(cal.get(Calendar.DATE)).append("/")
        .append(cal.get(Calendar.MONTH) + 1).append("/").append(cal.get(Calendar.YEAR));
    return date.toString();
  }

  public static String composeDate(int day, int month, int year) {
    StringBuilder date =
        new StringBuilder().append(day).append("/").append(month + 1).append("/").append(year);
    return date.toString();
  }

  public static Calendar getFormatedDate(Calendar date) {
    date.set(Calendar.HOUR_OF_DAY, 0);
    date.set(Calendar.MINUTE, 0);
    date.set(Calendar.SECOND, 0);
    date.set(Calendar.MILLISECOND, 0);
    return date;
  }

}
