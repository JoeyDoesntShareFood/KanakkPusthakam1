package com.example.jyothisp.kanakkpusthakam;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.jyothisp.kanakkpusthakam.data.TripContract;

public class SummaryActivity extends AppCompatActivity {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);



        viewPager = (ViewPager) findViewById(R.id.view_pager);

        viewPager.setAdapter(new TripFragmentPagerAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);


        ActionBar actionBar = (ActionBar) getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);

    }

    private void resetExpenses() {
        long id = ContentUris.parseId(getIntent().getData());
        String selection = TripContract.ExpenseEntry.TRIP_ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(id)};
        int rows = getContentResolver().delete(TripContract.ExpenseEntry.CONTENT_URI, selection, selectionArgs);

        ContentValues values = new ContentValues();
        values.put(TripContract.MembersEntry.COLUMN_CASH_SPENT, 0);
        values.put(TripContract.MembersEntry.COLUMN_EXPENSE, 0);
        values.put(TripContract.MembersEntry.COLUMN_BALANCE, 0);
        rows = getContentResolver().update(TripContract.MembersEntry.CONTENT_URI, values, selection, selectionArgs);
        Toast.makeText(this, "All expenses deleted", Toast.LENGTH_SHORT).show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.summary_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.help_menu_item);
        if (viewPager.getCurrentItem() == 0)
            item.setVisible(true);
        else
            item.setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_member_menu_item:
                Intent intent = new Intent(this, AddMemberActivity.class);
                intent.setData(getIntent().getData());
                startActivity(intent);
                break;
            case R.id.delete_all_expenses:
                resetExpenses();
                break;
            case android.R.id.home:
                onBackPressed();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, TripHistoryActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}
