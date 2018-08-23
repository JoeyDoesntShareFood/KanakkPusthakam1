package com.example.jyothisp.kanakkpusthakam.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.jyothisp.kanakkpusthakam.R;

public class SummaryCursorAdapter extends CursorAdapter {

    public SummaryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.summary_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView nameTextView = (TextView) view.findViewById(R.id.member_name_text_view);
        TextView cashSpentTextView = (TextView) view.findViewById(R.id.member_cash_spent_text_view);
        TextView expenseTextView = (TextView) view.findViewById(R.id.member_expense_text_view);
        TextView balanceTextView = (TextView) view.findViewById(R.id.member_balance_text_view);

        int nameColumnIndex = cursor.getColumnIndex(TripContract.MembersEntry.COLUMN_NAME);
        int cashSpentColumnIndex = cursor.getColumnIndex(TripContract.MembersEntry.COLUMN_CASH_SPENT);
        int expenseColumnIndex = cursor.getColumnIndex(TripContract.MembersEntry.COLUMN_EXPENSE);
        int balanceColumnIndex = cursor.getColumnIndex(TripContract.MembersEntry.COLUMN_BALANCE);

        String name = cursor.getString(nameColumnIndex);
        int cashSpent = cursor.getInt(cashSpentColumnIndex);
        int expense = cursor.getInt(expenseColumnIndex);
        int balance = cursor.getInt(balanceColumnIndex);

        nameTextView.setText(name);
        cashSpentTextView.setText(String.valueOf(cashSpent));
        expenseTextView.setText(String.valueOf(expense));
        balanceTextView.setText(String.valueOf(balance));

    }
}
