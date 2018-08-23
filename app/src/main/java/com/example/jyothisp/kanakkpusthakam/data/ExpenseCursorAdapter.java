package com.example.jyothisp.kanakkpusthakam.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jyothisp.kanakkpusthakam.R;

public class ExpenseCursorAdapter extends CursorAdapter {

    public ExpenseCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.expense_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView titleTextView = (TextView) view.findViewById(R.id.expense_title_text_view);
        TextView costTextView = (TextView) view.findViewById(R.id.expense_cost_text_view);
        TextView timeTextView = (TextView) view.findViewById(R.id.expense_time_text_view);

        int titleColumnIndex = cursor.getColumnIndex(TripContract.ExpenseEntry.COLUMN_ITEM);
        int cashColumnIndex = cursor.getColumnIndex(TripContract.ExpenseEntry.COLUMN_EXPENSE);
        int timeColumnIndex = cursor.getColumnIndex(TripContract.ExpenseEntry.COLUMN_TIME);

        String time = cursor.getString(timeColumnIndex);
        String title = cursor.getString(titleColumnIndex);
        if (title.equals(""))
            title = "Random Expense";
        int cash = cursor.getInt(cashColumnIndex);
        String cashString = "₹ " + cash + " per person";

        titleTextView.setText(title);
        costTextView.setText(cashString);
        timeTextView.setText(time);


    }
}
