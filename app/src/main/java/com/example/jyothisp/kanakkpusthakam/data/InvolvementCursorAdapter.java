package com.example.jyothisp.kanakkpusthakam.data;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.jyothisp.kanakkpusthakam.R;

import java.util.ArrayList;
import java.util.Arrays;

public class InvolvementCursorAdapter extends CursorAdapter {

    ArrayList<Long> selectedItemsID;
    long[] mIDs;
    private Cursor mCurrentCursor;

    public InvolvementCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        selectedItemsID = new ArrayList<>();
    }

    @Override
    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor != null){
            mIDs = new long[newCursor.getCount()];
            int idColumnIndex = newCursor.getColumnIndex(TripContract.MembersEntry._ID);
            int i=0;
            newCursor.moveToPosition(-1);
            while (newCursor.moveToNext()){
                mIDs[i]=newCursor.getLong(idColumnIndex);
                Log.v("Just Checking", "Adapter: " + mIDs[i]);
                i++;
            }
            mCurrentCursor = newCursor;
        }
        return super.swapCursor(newCursor);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.involvement_list_item, parent, false);

        final CheckBox box = view.findViewById(R.id.involvement_check_box);
        box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object o = box.getTag();
                long position = (long) o;
                Log.v("Adapter", "YO: " + position);
                if (box.isChecked()){

                    if (!selectedItemsID.contains(position))
                        selectedItemsID.add(position);
                } else {
                    selectedItemsID.remove(position);
                }
            }
        });

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView nameTextView = (TextView) view.findViewById(R.id.involvement_name_text_view);
        CheckBox box = view.findViewById(R.id.involvement_check_box);

        int nameColumnIndex = cursor.getColumnIndex(TripContract.MembersEntry.COLUMN_NAME);
        int idColumnIndex = cursor.getColumnIndex(TripContract.MembersEntry._ID);

        long id = cursor.getLong(idColumnIndex);
        if (selectedItemsID.contains(id))
            box.setChecked(true);
        else
            box.setChecked(false);

        box.setTag(id);

        String name = cursor.getString(nameColumnIndex);

        nameTextView.setText(name);

    }

    public void selectAll(){
        if (isAllSelected()){
            selectedItemsID.clear();
            swapCursor(null);
            swapCursor(mCurrentCursor);
        } else {
            selectedItemsID.clear();
            for (int i=0;i<mIDs.length;i++){
                selectedItemsID.add(mIDs[i]);
            }
            swapCursor(null);
            swapCursor(mCurrentCursor);
        }
    }

    private boolean isAllSelected(){
        for (int i=0; i<mIDs.length; i++){
            if (!selectedItemsID.contains(mIDs[i]))
                return false;
        }
        return true;
    }

    public ArrayList<Long> getSelectedItemsID() {
        return selectedItemsID;
    }
}
