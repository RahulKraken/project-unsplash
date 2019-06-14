package com.kraken.project_unsplash.Database;

import static android.provider.BaseColumns._ID;

/**
 * class contains the schema of all tables
 */
public class DatabaseContract {

    /**
     * Schema for favorites table
     */
    public static class FavoritesEntry {
        public static final String TABLE_NAME = "fav_table";
        public static final String COLUMN_PHOTO_ID = "photo_id";

        /*
        CREATE TABLE IF NOT EXISTS fav_table (_id INTEGER PRIMARY KEY, photo_id TEXT NOT NULL)
         */

        static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_PHOTO_ID + " TEXT NOT NULL)";
    }
}
