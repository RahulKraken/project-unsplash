package com.kraken.project_unsplash.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * database helper class
 */
public class DatabaseHelper extends SQLiteOpenHelper {

  private static final String TAG = "DatabaseHelper";

  // database name
  private static final String DATABASE_NAME = "data.db";

  /**
   * database version : needs to updated every time onUpgrade method is called or changes to
   * database structure is made
   */
  private static final int DATABASE_VERSION = 1;

  /**
   * constructor calling super with the DATABASE_NAME and DATABASE_VERSION
   * @param context : context
   */
  public DatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    Log.d(TAG, "DatabaseHelper: constructor called with super");
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    // create the favorites table
    Log.d(TAG, "onCreate: creating table");
    db.execSQL(DatabaseContract.FavoritesEntry.SQL_CREATE_TABLE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }
}
