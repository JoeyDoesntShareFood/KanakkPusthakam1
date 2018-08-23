package com.example.jyothisp.kanakkpusthakam;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jyothisp.kanakkpusthakam.data.InvolvementCursorAdapter;
import com.example.jyothisp.kanakkpusthakam.data.TripContract;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class InvolvementActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private InvolvementCursorAdapter mAdapter;
    private double[] mCashRolled;
    private int[] mInvolvement;
    private String[] mMembers;
    private long[] mIDs;
    private long tripID;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_involvement);

        ListView listView = (ListView) findViewById(R.id.involvement_list_view);

        mAdapter = new InvolvementCursorAdapter(this, null);

        tripID = getIntent().getLongExtra(TripContract.TripsEntry._ID, 0);
        getSupportLoaderManager().initLoader(0, null, this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mCashRolled = getIntent().getDoubleArrayExtra(TripContract.ExpenseEntry.COLUMN_CASH_ROLLED);
        mMembers = getIntent().getStringArrayExtra(TripContract.MembersEntry.COLUMN_NAME);
        mIDs = getIntent().getLongArrayExtra("ids");
        mInvolvement = new int[getNumberOfMembers()];
        listView.setAdapter(mAdapter);
        mListView = listView;
    }

    private int getNumberOfMembers() {
        String selection = TripContract.MembersEntry.TRIP_ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(tripID)};
        Cursor cursor = getContentResolver().query(TripContract.MembersEntry.CONTENT_URI, null, selection, selectionArgs, null);

        int members = cursor.getCount();

        cursor.close();

        return members;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.involvement_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_menu_item:
                if (mAdapter.getSelectedItemsID().size() == 0)
                    Toast.makeText(this, R.string.involvement_help, Toast.LENGTH_SHORT).show();
                else {
                    addExpense();
                    updateMembers();
                    String cash = String.format("%.0f", calcExpense());
                    Toast.makeText(this, "New expense added, Rs." + cash + " per person", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, SummaryActivity.class);
                    intent.setData(ContentUris.withAppendedId(TripContract.TripsEntry.CONTENT_URI, tripID));
                    startActivity(intent);
                }
                return true;
            case R.id.select_all_item:
                mAdapter.selectAll();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addExpense() {
        String expenseName = getIntent().getStringExtra(TripContract.ExpenseEntry.COLUMN_ITEM);
        String cashSpent = intArrayToString(mCashRolled);
        getInvolvement();
        String involvement = intArrayToString(mInvolvement);
        int expense = (int) calcExpense();
        Log.v("Involvement fragment", "" + expense);
        Log.v("Involvement fragment", expenseName);
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("hh:mm aaa");
        String timeText = format.format(date);
        ContentValues values = new ContentValues();
        values.put(TripContract.ExpenseEntry.COLUMN_ITEM, expenseName);
        values.put(TripContract.ExpenseEntry.TRIP_ID, tripID);
        values.put(TripContract.ExpenseEntry.COLUMN_CASH_ROLLED, cashSpent);
        values.put(TripContract.ExpenseEntry.COLUMN_IS_INVLOVED, involvement);
        values.put(TripContract.ExpenseEntry.COLUMN_EXPENSE, expense);
        values.put(TripContract.ExpenseEntry.COLUMN_TIME, timeText);

        getContentResolver().insert(TripContract.ExpenseEntry.CONTENT_URI, values);

    }

    private void updateMembers() {
        for (int i = 0; i < mCashRolled.length; i++) {
            if (mCashRolled[i] != 0) {
                Uri uri = ContentUris.withAppendedId(TripContract.MembersEntry.CONTENT_URI, mIDs[i]);
                ContentValues values = new ContentValues();
                values.put(TripContract.MembersEntry.COLUMN_CASH_SPENT, mCashRolled[i]);
                getContentResolver().update(uri, values, null, null);
            }
            if (mInvolvement[i] == 1) {
                Uri uri = ContentUris.withAppendedId(TripContract.MembersEntry.CONTENT_URI, mIDs[i]);
                ContentValues values = new ContentValues();
                int expense = (int) calcExpense();
                values.put(TripContract.MembersEntry.COLUMN_EXPENSE, expense);
                getContentResolver().update(uri, values, null, null);
            }
        }

        calcCashPerPerson();
    }

    private void calcCashPerPerson(){
        String selection = TripContract.MembersEntry.TRIP_ID + "=?";
        String[] selectionArgs = new String[]{("" + tripID)};
        String[] projection = new String[] {
                TripContract.MembersEntry.COLUMN_EXPENSE
        };
        Cursor cursor = getContentResolver().query(TripContract.MembersEntry.CONTENT_URI, projection, selection, selectionArgs, null);
        int sum = 0;
        int expenseColumnIndex = cursor.getColumnIndex(TripContract.MembersEntry.COLUMN_EXPENSE);
        while (cursor.moveToNext()){
            sum += cursor.getInt(expenseColumnIndex);
        }
        int cashPerPerson = sum/cursor.getCount();
        cursor.close();

        ContentValues values = new ContentValues();
        values.put(TripContract.TripsEntry.COLUMN_CASH_PER_PERSON, cashPerPerson);
        selection = TripContract.TripsEntry._ID + "=?";
        int rows = getContentResolver().update(TripContract.TripsEntry.CONTENT_URI, values, selection, selectionArgs);
    }

    //TODO: deal with no expense input;

    private double calcExpense() {
        double membersInExpense = mAdapter.getSelectedItemsID().size();
        double totalCash = 0;
        for (int i = 0; i < mCashRolled.length; i++)
            totalCash += mCashRolled[i];
        try {
            double expense = totalCash / membersInExpense;
            return expense;
        } catch (ArithmeticException e) {
            Log.e("InvolvementActivity", "calcExpense: /0", e);
        }
        return 0;
    }

    private void getInvolvement() {
        ArrayList<Long> ids = mAdapter.getSelectedItemsID();
        for (int i = 0; i < mIDs.length; i++) {
            if (ids.contains(mIDs[i]))
                mInvolvement[i] = 1;
            else
                mInvolvement[i] = 0;
        }
    }


    private String intArrayToString(double[] intArray) {
        String string = "";
        int i;
        for (i = 0; i < (intArray.length - 1); i++) {
            string += intArray[i];
            string += ",";
        }
        string += intArray[i];
        Log.v("Involvement fragment1", string);

        return string;
    }

    private String intArrayToString(int[] intArray) {
        String string = "";
        int i;
        for (i = 0; i < (intArray.length - 1); i++) {
            string += intArray[i];
            string += ",";
        }
        string += intArray[i];
        Log.v("Involvement fragment", string);
        return string;
    }


    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        String selection = TripContract.MembersEntry.TRIP_ID + "=?";
        String[] selectionArgs = new String[]{("" + tripID)};
        return new CursorLoader(this, TripContract.MembersEntry.CONTENT_URI, null, selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, ExpenseInputActivity.class);
        intent.setData(ContentUris.withAppendedId(TripContract.TripsEntry.CONTENT_URI, tripID));
        intent.putExtra(TripContract.ExpenseEntry.COLUMN_ITEM, getIntent().getStringExtra(TripContract.ExpenseEntry.COLUMN_ITEM));
        startActivity(intent);
    }


}
