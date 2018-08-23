package com.example.jyothisp.kanakkpusthakam.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.CursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
    private int mStrength;
    private long[] mIDs;
    private ContentValues values;

    public ExpenseInputAdapter(Context context, Cursor c, long[] ids) {
        super(context, c, 0);
        mStrength = ids.length;
        mIDs = ids;
        values = new ContentValues();
        for (int i = 0; i < mStrength; i++) {
            values.put("" + mIDs[i], (double) 0);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.input_list_item, parent, false);

        final EditText editText = (EditText) view.findViewById(R.id.input_cash_edit_text);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                long id = (long) editText.getTag();
                String cashString = editText.getText().toString().trim();
                double cash;
                if (cashString.equals(""))
                    cash = 0;
                else
                    cash = Double.valueOf(cashString);
                if (values.containsKey("" + id)) {
                    values.remove("" + id);
                    values.put("" + id, cash);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = (TextView) view.findViewById(R.id.input_name_text_view);
        EditText cashEditText = (EditText) view.findViewById(R.id.input_cash_edit_text);

        int nameColumnIndex = cursor.getColumnIndexOrThrow(TripContract.MembersEntry.COLUMN_NAME);
        int IDColumnIndex = cursor.getColumnIndexOrThrow(TripContract.MembersEntry._ID);

        long id = cursor.getLong(IDColumnIndex);
        String name = cursor.getString(nameColumnIndex);
        nameTextView.setText(name);

        cashEditText.setTag(id);
        double cash = values.getAsDouble("" + id);
        String cashString = String.valueOf(cash);
        if (cash != 0)
            cashEditText.setText(cashString);
        else
            cashEditText.setText("");
    }

    public double[] getCashRolled(){
        double[] cash;
        cash = new double[mStrength];

        for (int i =0; i<mStrength; i++){
            cash[i] = values.getAsDouble("" + mIDs[i]);
            Log.v("ExpenseInputAdapter", "getCashRolled: " + cash[i]);

        }


        return cash;
    }

}
