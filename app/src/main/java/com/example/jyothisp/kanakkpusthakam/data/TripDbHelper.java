package com.example.jyothisp.kanakkpusthakam.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.jyothisp.kanakkpusthakam.data.TripContract.TripsEntry;
import com.example.jyothisp.kanakkpusthakam.data.TripContract.MembersEntry;
import com.example.jyothisp.kanakkpusthakam.data.TripContract.ExpenseEntry;

public class TripDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "trips.db";

    private static final int DATABASE_VERSION = 1;

    public TripDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        /**
         * Creating the trips table.
         */
        String SQL_CREATE_TABLE = "CREATE TABLE " + TripsEntry.TABLE_NAME + " ("
                + TripsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TripsEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                + TripsEntry.COLUMN_DATE + " TEXT NOT NULL, "
                + TripsEntry.COLUMN_CASH_PER_PERSON + " INTEGER DEFAULT 0, "
                + TripsEntry.COLUMN_IS_SETTLED + " INTEGER DEFAULT 0, "
                + TripsEntry.COLUMN_IS_COMPLETED + " INTEGER DEFAULT 0);";

        db.execSQL(SQL_CREATE_TABLE);

        /**
         * Creating the members table.
         */
        SQL_CREATE_TABLE = "CREATE TABLE " + MembersEntry.TABLE_NAME + " ("
                + MembersEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MembersEntry.TRIP_ID + " INTEGER NOT NULL, "
                + MembersEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + MembersEntry.COLUMN_CASH_SPENT + " INTEGER DEFAULT 0, "
                + MembersEntry.COLUMN_EXPENSE + " INTEGER DEFAULT 0, "
                + MembersEntry.COLUMN_BALANCE + " INTEGER DEFAULT 0, "
                + "FOREIGN KEY(" + MembersEntry.TRIP_ID + ") REFERENCES " + TripsEntry.TABLE_NAME + "(" + TripsEntry._ID + ")"
                + " );";


        db.execSQL(SQL_CREATE_TABLE);

        /**
         * Creating the expenses table.
         */
        SQL_CREATE_TABLE = "CREATE TABLE " + ExpenseEntry.TABLE_NAME + " ("
                + ExpenseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ExpenseEntry.TRIP_ID + " INTEGER NOT NULL, "
                + ExpenseEntry.COLUMN_ITEM + " TEXT NOT NULL DEFAULT \"Random Expense\", "
                + ExpenseEntry.COLUMN_CASH_ROLLED + " TEXT, "
                + ExpenseEntry.COLUMN_EXPENSE + " INTEGER DEFAULT 0, "
                + ExpenseEntry.COLUMN_TIME + " TEXT, "
                + ExpenseEntry.COLUMN_IS_INVLOVED + " TEXT, "
                + "FOREIGN KEY(" + ExpenseEntry.TRIP_ID + ") REFERENCES " + TripsEntry.TABLE_NAME + "(" + TripsEntry._ID + ")"
                + ");";

        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
