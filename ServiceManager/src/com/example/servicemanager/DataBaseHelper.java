package com.example.servicemanager;

import static com.example.servicemanager.Constants.*;
import static com.example.servicemanager.Utils.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {
  private static String TAG = "DataBaseHelper"; // Tag just for the LogCat
                                                // window
  // destination path (location) of our database on device
  protected static String DB_PATH = getExternalFolderPath();
  private SQLiteDatabase mDataBase;
  private InputStream inputStream;

  public DataBaseHelper(Context context, InputStream inputStream) {
    super(context, DB_NAME, null, 1);// 1? Its database Version
    DB_PATH = Utils.getDatabasePath(context.getApplicationInfo().dataDir, context.getPackageName());
    this.inputStream = inputStream;
  }

  public void createDataBase() throws IOException {
    // If the database does not exist, copy it from the assets.

    this.getReadableDatabase();
    this.close();
    try {
      // Copy the database from assests
      copyDataBase();
      Log.e(TAG, "createDatabase database created");
    } catch (IOException mIOException) {
      throw new Error("ErrorCopyingDataBase");
    }
  }

  // Copy the database from assets
  private void copyDataBase() throws IOException {
    //File file = Drive.files().get(fileId).execute();
    InputStream mInput = new FileInputStream(getExternalFolderPath() + "/" + DB_NAME);
    String outFileName = DB_PATH + DB_NAME_WITH_EXTENTION;
    OutputStream mOutput = new FileOutputStream(outFileName);
    byte[] mBuffer = new byte[1024];
    int mLength;
    while ((mLength = mInput.read(mBuffer)) > 0) {
      mOutput.write(mBuffer, 0, mLength);
    }
    mOutput.flush();
    mOutput.close();
    mInput.close();
  }

  // Open the database, so we can query it
  public boolean openDataBase() throws SQLException {
    String mPath = DB_PATH + DB_NAME;
    // Log.v("mPath", mPath);
    mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
    // mDataBase = SQLiteDatabase.openDatabase(mPath, null,
    // SQLiteDatabase.NO_LOCALIZED_COLLATORS);
    return mDataBase != null;
  }

  @Override
  public synchronized void close() {
    if (mDataBase != null)
      mDataBase.close();
    super.close();
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // TODO Auto-generated method stub

  }
}
