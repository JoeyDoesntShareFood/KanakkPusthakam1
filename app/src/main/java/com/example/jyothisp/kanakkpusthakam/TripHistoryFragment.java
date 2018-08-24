package com.example.jyothisp.kanakkpusthakam;


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jyothisp.kanakkpusthakam.data.TripContract;
import com.example.jyothisp.kanakkpusthakam.data.TripsCursorAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class TripHistoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    public TripHistoryFragment() {
        // Required empty public constructor
    }

    TripsCursorAdapter mAdapter;

    TextInputEditText mEditText;

    private View mEmptyView;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_trip_history, container, false);

        getActivity().getSupportLoaderManager().initLoader(0, null, this);

        FloatingActionButton floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.trip_history_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showNewTripDialog(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String tripTitle = mEditText.getText().toString();
                        Uri uri = insertTrip(tripTitle);


                        Intent intent = new Intent(getActivity(), AddMemberActivity.class);
                        intent.setData(uri);
                        startActivity(intent);

                    }
                });

            }
        });

        mEmptyView = rootView.findViewById(R.id.fragment_empty_view);
        ListView listView = (ListView) rootView.findViewById(R.id.fragment_list_view);
        listView.setEmptyView(mEmptyView);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {
                showDeleteDialog(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = ContentUris.withAppendedId(TripContract.TripsEntry.CONTENT_URI, id);
                        getActivity().getContentResolver().delete(uri, null, null);
                    }
                });
                return true;
            }
        });

        mAdapter = new TripsCursorAdapter(getContext(), null);

        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uri = ContentUris.withAppendedId(TripContract.TripsEntry.CONTENT_URI, id);

                Intent intent = new Intent(getActivity(), SummaryActivity.class);
                intent.setData(uri);
                startActivity(intent);
            }
        });

        return rootView;
    }


    private void showDeleteDialog(DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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


    private Uri insertTrip(String title){

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
        String dateText = format.format(date);

        ContentValues values = new ContentValues();
        values.put(TripContract.TripsEntry.COLUMN_TITLE, title);
        values.put(TripContract.TripsEntry.COLUMN_DATE, dateText);

        Uri uri = getActivity().getContentResolver().insert(TripContract.TripsEntry.CONTENT_URI, values);

        long newRowID = ContentUris.parseId(uri);

        Toast.makeText(getContext(), "New row id " + newRowID , Toast.LENGTH_SHORT).show();

        return uri;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.simple_done_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_member_menu_item:
                Intent intent = new Intent(getActivity(), AddMemberActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void showNewTripDialog(DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.add_trip_dialog_title);

        final LinearLayout layout = new LinearLayout(getContext());

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(48, 32, 48, 32);

        final TextInputLayout inputLayout = new TextInputLayout(getContext());
        inputLayout.setHint(getResources().getString(R.string.add_trip_dialog_hint));
        mEditText = new TextInputEditText(getContext());
        inputLayout.addView(mEditText);

        layout.addView(inputLayout, layoutParams);

        builder.setView(layout);


        builder.setPositiveButton(R.string.start, onClickListener);

        builder.create().show();
    }

    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(getContext(), TripContract.TripsEntry.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        View view = mEmptyView.findViewById(R.id.container_empty_default);
        view.setVisibility(View.VISIBLE);
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
        mAdapter.swapCursor(null);
    }


}
