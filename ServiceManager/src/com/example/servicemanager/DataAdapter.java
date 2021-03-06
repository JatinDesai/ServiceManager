package com.example.servicemanager;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataAdapter {
  protected static final String TAG = "DataAdapter";

  private final Context mContext;
  private SQLiteDatabase mDb;
  private DataBaseHelper mDbHelper;
  private InputStream inputStream;

  public DataAdapter(Context context, InputStream inputStream) {
    this.mContext = context;
    mDbHelper = new DataBaseHelper(mContext, inputStream);
    this.inputStream = inputStream;
  }

  public DataAdapter createDatabase() throws SQLException {
    try {
      mDbHelper.createDataBase();
    } catch (IOException mIOException) {
      Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
      throw new Error("UnableToCreateDatabase");
    }
    return this;
  }

  public DataAdapter open() throws SQLException {
    try {
      mDbHelper.openDataBase();
      mDbHelper.close();
      mDb = mDbHelper.getReadableDatabase();
    } catch (SQLException mSQLException) {
      Log.e(TAG, "open >>" + mSQLException.toString());
      throw mSQLException;
    }
    return this;
  }

  public void close() {
    mDbHelper.close();
  }

  public Cursor getTestData() {
    try {
      String sql = "SELECT * FROM CUSTOMERS";

      Cursor mCur = mDb.rawQuery(sql, null);
      if (mCur != null) {
        mCur.moveToNext();
      }
      return mCur;
    } catch (SQLException mSQLException) {
      Log.e(TAG, "getTestData >>" + mSQLException.toString());
      // throw mSQLException;
      return null;
    }
  }
}
