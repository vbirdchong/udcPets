package com.example.android.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by cccho on 2017/10/21.
 */

public class PetDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = PetDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "shelter.db";
    private static final int DATABASE_VERSION = 1;

    public PetDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // 这里需要注意 SQL 语句的之间的空格
        String SQL_CREATE_PETS_TABLE =
                "CREATE TABLE " + PetContract.PetEntry.TABLE_NAME + " ("
                + PetContract.PetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PetContract.PetEntry.COLUMN_PET_NAME + " TEXT NOT NULL, "
                + PetContract.PetEntry.COLUMN_PET_BREED + " TEXT, "
                + PetContract.PetEntry.COLUMN_PET_GENDER + " INTEGER NOT NULL, "
                + PetContract.PetEntry.COLUMN_PET_WEIGHT + " INTEGER NOT NULL DEFAULT 0);";

        sqLiteDatabase.execSQL(SQL_CREATE_PETS_TABLE);
        Log.i(LOG_TAG, SQL_CREATE_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // no need to do at present
    }
}
