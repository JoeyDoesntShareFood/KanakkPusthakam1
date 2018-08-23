package com.example.jyothisp.kanakkpusthakam.data;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jyothisp.kanakkpusthakam.R;

public class TripsCursorAdapter extends CursorAdapter {

    public TripsCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.trip_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView titleTextView = (TextView) view.findViewById(R.id.trip_title_text_view);
        TextView costTextView = (TextView) view.findViewById(R.id.trip_cost_text_view);
        TextView dateTextView = (TextView) view.findViewById(R.id.trip_date_text_view);
        ImageView settlementAlertImageView = (ImageView) view.findViewById(R.id.trip_is_settled_icon_image_view);

        int titleColumnIndex = cursor.getColumnIndex(TripContract.TripsEntry.COLUMN_TITLE);
        int cashColumnIndex = cursor.getColumnIndex(TripContract.TripsEntry.COLUMN_CASH_PER_PERSON);
        int dateColumnIndex = cursor.getColumnIndex(TripContract.TripsEntry.COLUMN_DATE);
        int settlementColumnIndex = cursor.getColumnIndex(TripContract.TripsEntry.COLUMN_IS_SETTLED);

        String title = cursor.getString(titleColumnIndex);
        int cash = cursor.getInt(cashColumnIndex);
        String cashString = "₹ " + cash + " per person";
        String date = cursor.getString(dateColumnIndex);
        int isSettled = cursor.getInt(settlementColumnIndex);

        titleTextView.setText(title);
        costTextView.setText(cashString);
        dateTextView.setText(date);

        if (isSettled == 0)
            settlementAlertImageView.setVisibility(View.VISIBLE);
        else
            settlementAlertImageView.setVisibility(View.INVISIBLE);


    }
}
