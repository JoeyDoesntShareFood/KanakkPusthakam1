package com.example.jyothisp.kanakkpusthakam;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jyothisp.kanakkpusthakam.data.TripContract;

public class AddMemberActivity extends AppCompatActivity {

    EditText mEditText;
    Uri mTripUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        setTitle(getResources().getString(R.string.add_member));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTripUri = getIntent().getData();
        mEditText = (EditText) findViewById(R.id.add_member_edit_text);
        mEditText.setFocusableInTouchMode(true);
        mEditText.requestFocus();
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains("\n")){
                    addMember();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void add(View view) {
        addMember();
    }

    private void addMember() {
        String memberName = mEditText.getText().toString().trim();
        if (memberName.equals("")) {
            Toast.makeText(this, R.string.name_absent, Toast.LENGTH_SHORT).show();
            mEditText.setText("");
            return;
        }
        int id = (int) ContentUris.parseId(getIntent().getData());
        ContentValues values = new ContentValues();
        values.put(TripContract.MembersEntry.COLUMN_NAME, memberName);
        values.put(TripContract.MembersEntry.TRIP_ID, id);
        Uri memberUri = getContentResolver().insert(TripContract.MembersEntry.CONTENT_URI, values);
        long newRowID = ContentUris.parseId(memberUri);
        mEditText.setText("");

        Toast.makeText(this, memberName + " added to trip.", Toast.LENGTH_SHORT).show();
    }

    public void clear(View view) {
        mEditText.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.simple_done_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_menu_item:
                Intent intent = new Intent(AddMemberActivity.this, SummaryActivity.class);
                intent.setData(mTripUri);
                startActivity(intent);
                return true;
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
