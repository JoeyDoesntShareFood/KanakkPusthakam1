package com.example.jyothisp.kanakkpusthakam;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jyothisp.kanakkpusthakam.data.Expense;
import com.example.jyothisp.kanakkpusthakam.data.ExpenseInputAdapter;
import com.example.jyothisp.kanakkpusthakam.data.TripContract;

import java.util.ArrayList;

public class ExpenseInputActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    double[] mCashRolled;
    Uri mTripUri;
    int mNumberOfMembers;
    int mMemberPosition;
    long[] mIDs;
    String[] mMembers;
    ArrayList<Expense> expenses;

    private ExpenseInputAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_input);
        setTitle(getResources().getString(R.string.add_expense_dialog_title));

        mTripUri = getIntent().getData();

        mNumberOfMembers = getNumberOfMembers();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCashRolled = new double[mNumberOfMembers];

        mAdapter = new ExpenseInputAdapter(this, null, mIDs);
        getSupportLoaderManager().initLoader(0, null, this);
        final ListView listView = (ListView) findViewById(R.id.expense_input_list_view);
        listView.setAdapter(mAdapter);


    }





    private void addExpense(){
        EditText cashEditText = (EditText) findViewById(R.id.input_cash_edit_text);
        String cashText = cashEditText.getText().toString().trim();


        if (mMemberPosition == -1) {
            Toast.makeText(this, R.string.no_member_name_toast, Toast.LENGTH_SHORT).show();
        } else {

            if (cashText.equals(""))
                Toast.makeText(this, R.string.no_cash_toast, Toast.LENGTH_SHORT).show();
            else {
                mCashRolled[mMemberPosition] += Integer.parseInt(cashText);
                int index = getIndexByName(mMembers[mMemberPosition]);
                if (index == -1) {
                    expenses.add(new Expense(mMembers[mMemberPosition], mCashRolled[mMemberPosition]));
                } else {
                    expenses.remove(index);
                    expenses.add(new Expense(mMembers[mMemberPosition], mCashRolled[mMemberPosition]));
                }
                cashEditText.setText("");
            }

        }
    }

    private int getIndexByName(String name) {
        for (int i = 0; i < expenses.size(); i++) {
            if (expenses.get(i).getmName().equals(name)) {
                return i;
            }
        }
        return -1;
    }



    private int getNumberOfMembers() {
        String selection = TripContract.MembersEntry.TRIP_ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(mTripUri))};
        Cursor cursor = getContentResolver().query(TripContract.MembersEntry.CONTENT_URI, null, selection, selectionArgs, null);

        int members = cursor.getCount();
        mIDs = new long[members];
        int IDcolumnIndex = cursor.getColumnIndex(TripContract.MembersEntry._ID);
        int i =0;
        while (cursor.moveToNext()){
            mIDs[i] = cursor.getLong(IDcolumnIndex);
            i++;
        }



        cursor.close();

        return members;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.simple_done_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_menu_item:
                long id = ContentUris.parseId(mTripUri);
                String expenseTitle = getIntent().getStringExtra(TripContract.ExpenseEntry.COLUMN_ITEM);
                mCashRolled = mAdapter.getCashRolled();
                int f =0;
                for (int i=0; i<mCashRolled.length; i++){
                    if (mCashRolled[i] > 0)
                        f=1;
                }
                if (f == 0){
                    Toast.makeText(this, R.string.no_money, Toast.LENGTH_SHORT).show();
                    return false;
                }
                Intent intent = new Intent(this, InvolvementActivity.class);
                intent.putExtra(TripContract.TripsEntry._ID, id);
                intent.putExtra(TripContract.MembersEntry.COLUMN_NAME, mMembers);
                intent.putExtra(TripContract.ExpenseEntry.COLUMN_CASH_ROLLED, mCashRolled);
                intent.putExtra(TripContract.ExpenseEntry.COLUMN_ITEM, expenseTitle);
                intent.putExtra("ids", mIDs);
                intent.setData(null);
                startActivity(intent);
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, SummaryActivity.class);
        intent.setData(mTripUri);
        startActivity(intent);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        long tripID = ContentUris.parseId(mTripUri);
        String selection = TripContract.MembersEntry.TRIP_ID + "=?";
        String[] selectionArgs = new String[]{("" + tripID)};
        return new CursorLoader(this, TripContract.MembersEntry.CONTENT_URI, null, selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}

