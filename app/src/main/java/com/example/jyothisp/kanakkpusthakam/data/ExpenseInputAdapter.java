package com.example.jyothisp.kanakkpusthakam.data;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jyothisp.kanakkpusthakam.R;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class ExpenseInputAdapter extends CursorAdapter {

    private Cursor mCursor;

    public ExpenseInputAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.input_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = (TextView) view.findViewById(R.id.input_name_text_view);
        EditText cashEditText = (EditText) view.findViewById(R.id.input_cash_edit_text);

        int nameColumnIndex = cursor.getColumnIndexOrThrow(TripContract.MembersEntry.COLUMN_NAME);
        String name = cursor.getString(nameColumnIndex);

        nameTextView.setText(name);
    }

}
