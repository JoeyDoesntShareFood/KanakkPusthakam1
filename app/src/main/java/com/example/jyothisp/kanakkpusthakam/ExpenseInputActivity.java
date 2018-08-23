package com.example.jyothisp.kanakkpusthakam;

import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jyothisp.kanakkpusthakam.data.Expense;
import com.example.jyothisp.kanakkpusthakam.data.ExpenseInputAdapter;
import com.example.jyothisp.kanakkpusthakam.data.TripContract;

import java.util.ArrayList;

public class ExpenseInputActivity extends AppCompatActivity {

    double[] mCashRolled;
    Uri mTripUri;
    int mNumberOfMembers;
    int mMemberPosition;
    long[] mIDs;
    String[] mMembers;
    ArrayList<Expense> expenses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_input);
        setTitle(getResources().getString(R.string.add_member));

        mTripUri = getIntent().getData();

        mNumberOfMembers = getNumberOfMembers();

        mMemberPosition = -1;


        setupSpinner();

        expenses = new ArrayList<>();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCashRolled = new double[mNumberOfMembers];

        final ListView listView = (ListView) findViewById(R.id.expense_input_list_view);
        listView.setItemsCanFocus(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                deleteExpense(listView, position);
            }
        });

        Button addButton = (Button) findViewById(R.id.input_add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExpense();
            }
        });

    }


    private void deleteExpense(final ListView listView, final int position){
        showDeleteDialog(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Expense expense = (Expense) listView.getItemAtPosition(position);
                String name = expense.getmName();
                int index = getIndexByName(name);
                expenses.remove(index);
                for (index = 0; index<mNumberOfMembers; index++){
                    if (name.equals(mMembers[index]))
                        break;
                }
                mCashRolled[index] = 0;
                updateUI();
            }
        });
    }

    private void showDeleteDialog(DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete);
        builder.setMessage(R.string.delete_dialog_message);
        builder.setPositiveButton(R.string.delete, onClickListener);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null)
                    dialog.dismiss();
            }
        });

        builder.create().show();
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
                updateUI();
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


    private void updateUI() {
        ListView listView = (ListView) findViewById(R.id.expense_input_list_view);
        ExpenseInputAdapter adapter = new ExpenseInputAdapter(this, expenses);
        listView.setAdapter(adapter);
    }

    private void setupSpinner() {

        //TODO: Refactor code to get the position using ID instead of name.

        String[] members = new String[mNumberOfMembers];
        mIDs = new long[mNumberOfMembers];

        String selection = TripContract.MembersEntry.TRIP_ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(mTripUri))};
        Cursor cursor = getContentResolver().query(TripContract.MembersEntry.CONTENT_URI, null, selection, selectionArgs, null);
        int nameColumnIndex = cursor.getColumnIndexOrThrow(TripContract.MembersEntry.COLUMN_NAME);
        int idColumnIndex = cursor.getColumnIndexOrThrow(TripContract.MembersEntry._ID);

        int i = 0;
        while (cursor.moveToNext()) {
            members[i] = cursor.getString(nameColumnIndex);
            mIDs[i] = cursor.getLong(idColumnIndex);
            i++;
        }

        mMembers = members;

        cursor.close();


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, members);

        Spinner spinner = (Spinner) findViewById(R.id.members_spinner);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mMemberPosition = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mMemberPosition = -1;
            }
        });

    }

    private int getNumberOfMembers() {
        String selection = TripContract.MembersEntry.TRIP_ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(mTripUri))};
        Cursor cursor = getContentResolver().query(TripContract.MembersEntry.CONTENT_URI, null, selection, selectionArgs, null);

        int members = cursor.getCount();

        cursor.close();

        return members;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_member_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_menu_item:
                long id = ContentUris.parseId(mTripUri);
                String expenseTitle = getIntent().getStringExtra(TripContract.ExpenseEntry.COLUMN_ITEM);
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
}

