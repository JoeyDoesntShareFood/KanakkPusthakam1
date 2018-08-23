package com.example.jyothisp.kanakkpusthakam.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class TripProvider extends ContentProvider {


    /**
     * Constants for the Uri Matcher.
     */
    public static final int TRIPS = 100;
    public static final int TRIPS_ID = 101;
    public static final int MEMBERS = 200;
    public static final int MEMBERS_ID = 201;
    public static final int EXPENSES = 300;
    public static final int EXPENSES_ID = 301;

    /**
     * Creating the Uri Matcher.
     */
    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


    /**
     * Adding the Uris to the matcher.
     */
    static {

        /**
         * Uri for all the trips.
         */
        sUriMatcher.addURI(TripContract.CONTENT_AUTHORITY, TripContract.PATH_TRIPS, TRIPS);

        /**
         * Uri for a single trip.
         */
        sUriMatcher.addURI(TripContract.CONTENT_AUTHORITY, TripContract.PATH_TRIPS + "/#", TRIPS_ID);

        /**
         * Uri for all the members.
         */
        sUriMatcher.addURI(TripContract.CONTENT_AUTHORITY, TripContract.PATH_MENMBERS, MEMBERS);

        /**
         * Uri for a single member.
         */
        sUriMatcher.addURI(TripContract.CONTENT_AUTHORITY, TripContract.PATH_MENMBERS + "/#", MEMBERS_ID);

        /**
         * Uri for all the expenses.
         */
        sUriMatcher.addURI(TripContract.CONTENT_AUTHORITY, TripContract.PATH_EXPENSES, EXPENSES);

        /**
         * Uri for a single expense.
         */
        sUriMatcher.addURI(TripContract.CONTENT_AUTHORITY, TripContract.PATH_EXPENSES + "/#", EXPENSES_ID);
    }

    private TripDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new TripDbHelper(getContext());
        return true;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);

        switch (match) {

            case TRIPS:
                cursor = database.query(TripContract.TripsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case TRIPS_ID:
                selection = TripContract.TripsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(TripContract.TripsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case MEMBERS:
                cursor = database.query(TripContract.MembersEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case MEMBERS_ID:
                selection = TripContract.MembersEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(TripContract.MembersEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case EXPENSES:
                cursor = database.query(TripContract.ExpenseEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case EXPENSES_ID:
                selection = TripContract.ExpenseEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(TripContract.ExpenseEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Unknown Uri.");
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case TRIPS:
                return insertTrip(uri, values);
            case MEMBERS:
                return insertMember(uri, values);
            case EXPENSES:
                return insertExpense(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertTrip(Uri uri, ContentValues contentValues) {

        /**
         * Sanity Checks.
         */

        String date = contentValues.getAsString(TripContract.TripsEntry.COLUMN_DATE);
        if (date == null) {
            throw new IllegalArgumentException("Date is not specified.");
        }

        String title = contentValues.getAsString(TripContract.TripsEntry.COLUMN_TITLE);
        if (title == null) {
            title = "Unnamed trip";
            contentValues.remove(TripContract.TripsEntry.COLUMN_TITLE);
            contentValues.put(TripContract.TripsEntry.COLUMN_TITLE, title);
        }

        Integer cash = contentValues.getAsInteger(TripContract.TripsEntry.COLUMN_CASH_PER_PERSON);
        if (cash == null) {
            cash = 0;
            contentValues.remove(TripContract.TripsEntry.COLUMN_CASH_PER_PERSON);
            contentValues.put(TripContract.TripsEntry.COLUMN_CASH_PER_PERSON, cash);
        }


        /** Sanity Checks complete. */

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(TripContract.TripsEntry.TABLE_NAME, null, contentValues);

        if (id == -1) {
            Log.e("TripProvider", "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertMember(Uri uri, ContentValues values) {

        /**
         * Sanity Checks.
         */

        String name = values.getAsString(TripContract.MembersEntry.COLUMN_NAME);
        if (name == null) {
            name = "Unnamed member";
            values.remove(TripContract.MembersEntry.COLUMN_NAME);
            values.put(TripContract.MembersEntry.COLUMN_NAME, name);
        }

        Integer cash = values.getAsInteger(TripContract.MembersEntry.COLUMN_CASH_SPENT);
        if (cash == null) {
            cash = 0;
            values.remove(TripContract.MembersEntry.COLUMN_CASH_SPENT);
            values.put(TripContract.MembersEntry.COLUMN_CASH_SPENT, cash);
        }

        cash = values.getAsInteger(TripContract.MembersEntry.COLUMN_EXPENSE);
        if (cash == null) {
            cash = 0;
            values.remove(TripContract.MembersEntry.COLUMN_EXPENSE);
            values.put(TripContract.MembersEntry.COLUMN_EXPENSE, cash);
        }

        cash = values.getAsInteger(TripContract.MembersEntry.COLUMN_BALANCE);
        if (cash == null) {
            cash = 0;
            values.remove(TripContract.MembersEntry.COLUMN_BALANCE);
            values.put(TripContract.MembersEntry.COLUMN_BALANCE, cash);
        }

        /** Sanity Checks complete. */

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(TripContract.MembersEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e("TripProvider", "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertExpense(Uri uri, ContentValues values) {


        /**
         * Sanity Checks.
         */

        String name = values.getAsString(TripContract.ExpenseEntry.COLUMN_ITEM);
        if (name == null) {
            name = "Unnamed expense";
            values.remove(TripContract.ExpenseEntry.COLUMN_ITEM);
            values.put(TripContract.ExpenseEntry.COLUMN_ITEM, name);
        }

        String cash = values.getAsString(TripContract.ExpenseEntry.COLUMN_CASH_ROLLED);
        if (cash == null) {
            throw new IllegalArgumentException("Cash rolled is not specified.");
        }

        String involvement = values.getAsString(TripContract.ExpenseEntry.COLUMN_IS_INVLOVED);
        if (involvement == null) {
            throw new IllegalArgumentException("Involvement is not specified.");
        }

        /** Sanity Checks complete. */

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(TripContract.ExpenseEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e("TripProvider", "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        switch (match) {
            case TRIPS:
                return updateTrip(uri, values, selection, selectionArgs);
            case MEMBERS:
                return updateMember(uri, values, selection, selectionArgs);
            case MEMBERS_ID:
                return updateMembersInc(uri, values);
            case EXPENSES:
                return updateExpense(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }


    }

    private int updateTrip(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        /**
         * Sanity Checks.
         */
        if (values.containsKey(TripContract.TripsEntry.COLUMN_DATE)) {
            String date = values.getAsString(TripContract.TripsEntry.COLUMN_DATE);
            if (date == null) {
                throw new IllegalArgumentException("Date is not specified.");
            }
        }


        if (values.containsKey(TripContract.TripsEntry.COLUMN_TITLE)) {
            String title = values.getAsString(TripContract.TripsEntry.COLUMN_TITLE);
            if (title == null) {
                title = "Unnamed trip";
                values.remove(TripContract.TripsEntry.COLUMN_TITLE);
                values.put(TripContract.TripsEntry.COLUMN_TITLE, title);
            }
        }


        if (values.containsKey(TripContract.TripsEntry.COLUMN_CASH_PER_PERSON)) {
            Integer cash = values.getAsInteger(TripContract.TripsEntry.COLUMN_CASH_PER_PERSON);
            if (cash == null) {
                cash = 0;
                values.remove(TripContract.TripsEntry.COLUMN_CASH_PER_PERSON);
                values.put(TripContract.TripsEntry.COLUMN_CASH_PER_PERSON, cash);
            }
        }


        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }
        /** Sanity Checks complete. */

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Returns the number of database rows affected by the update statement
        int numberRowsChanged = database.update(TripContract.TripsEntry.TABLE_NAME, values, selection, selectionArgs);

        if (numberRowsChanged > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numberRowsChanged;
    }

    private int updateMember(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        /**
         * Sanity Checks.
         */

        if (values.containsKey(TripContract.MembersEntry.COLUMN_NAME)) {
            String name = values.getAsString(TripContract.MembersEntry.COLUMN_NAME);
            if (name == null) {
                name = "Unnamed member";
                values.remove(TripContract.MembersEntry.COLUMN_NAME);
                values.put(TripContract.MembersEntry.COLUMN_NAME, name);
            }
        }


        if (values.containsKey(TripContract.MembersEntry.COLUMN_CASH_SPENT)) {
            Integer cash = values.getAsInteger(TripContract.MembersEntry.COLUMN_CASH_SPENT);
            if (cash == null) {
                cash = 0;
                values.remove(TripContract.MembersEntry.COLUMN_CASH_SPENT);
                values.put(TripContract.MembersEntry.COLUMN_CASH_SPENT, cash);
            }
        }


        if (values.containsKey(TripContract.MembersEntry.COLUMN_EXPENSE)) {
            Integer cash = values.getAsInteger(TripContract.MembersEntry.COLUMN_EXPENSE);
            if (cash == null) {
                cash = 0;
                values.remove(TripContract.MembersEntry.COLUMN_EXPENSE);
                values.put(TripContract.MembersEntry.COLUMN_EXPENSE, cash);
            }
        }


        if (values.containsKey(TripContract.MembersEntry.COLUMN_BALANCE)) {
            Integer cash = values.getAsInteger(TripContract.MembersEntry.COLUMN_BALANCE);
            if (cash == null) {
                cash = 0;
                values.remove(TripContract.MembersEntry.COLUMN_BALANCE);
                values.put(TripContract.MembersEntry.COLUMN_BALANCE, cash);
            }
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }
        /** Sanity Checks complete. */

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Returns the number of database rows affected by the update statement
        int numberRowsChanged = database.update(TripContract.MembersEntry.TABLE_NAME, values, selection, selectionArgs);

        if (numberRowsChanged > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numberRowsChanged;
    }

    private int updateMembersInc(Uri uri, ContentValues values) {

        long id = ContentUris.parseId(uri);
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        if (values.containsKey(TripContract.MembersEntry.COLUMN_CASH_SPENT)) {

            int cash = values.getAsInteger(TripContract.MembersEntry.COLUMN_CASH_SPENT);

            String sqlString = "UPDATE " + TripContract.MembersEntry.TABLE_NAME
                    + " SET " + TripContract.MembersEntry.COLUMN_CASH_SPENT
                    + " = " + TripContract.MembersEntry.COLUMN_CASH_SPENT + " + " + cash
                    + " WHERE " + TripContract.MembersEntry._ID
                    + " = " + id + ";";

            Log.v("TripProvider", "updateMembersInc: " + sqlString);

            database.execSQL(sqlString);
        }

        if (values.containsKey(TripContract.MembersEntry.COLUMN_EXPENSE)) {

            int expense = values.getAsInteger(TripContract.MembersEntry.COLUMN_EXPENSE);

            String sqlString = "UPDATE " + TripContract.MembersEntry.TABLE_NAME
                    + " SET " + TripContract.MembersEntry.COLUMN_EXPENSE
                    + " = " + TripContract.MembersEntry.COLUMN_EXPENSE + " + " + expense
                    + " WHERE " + TripContract.MembersEntry._ID
                    + " = " + id + ";";

            Log.v("TripProvider", "updateMembersInc: " + sqlString);
            database.execSQL(sqlString);
        }


            String  sqlString = "UPDATE " + TripContract.MembersEntry.TABLE_NAME + " SET "
                    + TripContract.MembersEntry.COLUMN_BALANCE + " = "
                    + TripContract.MembersEntry.COLUMN_CASH_SPENT + " - "
                    + TripContract.MembersEntry.COLUMN_EXPENSE + ";";

            Log.v("TripProvider", "updateMembersInc: " + sqlString);
            database.execSQL(sqlString);


        getContext().getContentResolver().notifyChange(uri, null);


        return 1;

    }

    private int updateExpense(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        /**
         * Sanity Checks.
         */

        if (values.containsKey(TripContract.ExpenseEntry.COLUMN_ITEM)) {
            String name = values.getAsString(TripContract.ExpenseEntry.COLUMN_ITEM);
            if (name == null) {
                name = "Unnamed expense";
                values.remove(TripContract.ExpenseEntry.COLUMN_ITEM);
                values.put(TripContract.ExpenseEntry.COLUMN_ITEM, name);
            }
        }


        if (values.containsKey(TripContract.ExpenseEntry.COLUMN_CASH_ROLLED)) {
            String cash = values.getAsString(TripContract.ExpenseEntry.COLUMN_CASH_ROLLED);
            if (cash == null) {
                throw new IllegalArgumentException("Cash rolled is not specified.");
            }
        }


        if (values.containsKey(TripContract.ExpenseEntry.COLUMN_IS_INVLOVED)) {
            String involvement = values.getAsString(TripContract.ExpenseEntry.COLUMN_IS_INVLOVED);
            if (involvement == null) {
                throw new IllegalArgumentException("Involvement is not specified.");
            }
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }
        /** Sanity Checks complete. */

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Returns the number of database rows affected by the update statement
        int numberRowsChanged = database.update(TripContract.ExpenseEntry.TABLE_NAME, values, selection, selectionArgs);

        if (numberRowsChanged > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numberRowsChanged;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowsDeleted;

        switch (match) {
            case TRIPS:
                rowsDeleted = database.delete(TripContract.TripsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MEMBERS:
                rowsDeleted = database.delete(TripContract.MembersEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case EXPENSES:
                rowsDeleted = database.delete(TripContract.ExpenseEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TRIPS_ID:
                selection = TripContract.TripsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(TripContract.TripsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MEMBERS_ID:

                selection = TripContract.MembersEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(TripContract.MembersEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case EXPENSES_ID:
                selection = TripContract.MembersEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(TripContract.ExpenseEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case TRIPS:
                return TripContract.TripsEntry.CONTENT_LIST_TYPE;
            case TRIPS_ID:
                return TripContract.TripsEntry.CONTENT_ITEM_TYPE;
            case MEMBERS:
                return TripContract.MembersEntry.CONTENT_LIST_TYPE;
            case MEMBERS_ID:
                return TripContract.MembersEntry.CONTENT_ITEM_TYPE;
            case EXPENSES:
                return TripContract.ExpenseEntry.CONTENT_LIST_TYPE;
            case EXPENSES_ID:
                return TripContract.ExpenseEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }


}
