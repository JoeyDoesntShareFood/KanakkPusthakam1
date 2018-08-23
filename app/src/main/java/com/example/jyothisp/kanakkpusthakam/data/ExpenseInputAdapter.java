package com.example.jyothisp.kanakkpusthakam.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jyothisp.kanakkpusthakam.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class ExpenseInputAdapter extends ArrayAdapter<Expense> {

    public ExpenseInputAdapter(@NonNull Context context, ArrayList<Expense> expenses) {
        super(context, 0, expenses);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.input_list_item, parent, false);
        }

        Expense currentExpense = getItem(position);

        TextView nameTextView = (TextView) view.findViewById(R.id.input_name_text_view);
        TextView cashTextView = (TextView) view.findViewById(R.id.input_cash_text_view);

        String cash = String.format("%.0f", currentExpense.getmCash());
        nameTextView.setText(currentExpense.getmName());
        cashTextView.setText(cash);

        return view;
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
