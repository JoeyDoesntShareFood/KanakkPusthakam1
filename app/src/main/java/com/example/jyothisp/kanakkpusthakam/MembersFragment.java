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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jyothisp.kanakkpusthakam.data.SummaryCursorAdapter;
import com.example.jyothisp.kanakkpusthakam.data.TripContract;


/**
 * A simple {@link Fragment} subclass.
 */
public class MembersFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    SummaryCursorAdapter mAdapter;
    EditText mEditText;
    Uri mTripUri;
    View mEmptyView;

    public MembersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_members, container, false);

        mTripUri = getActivity().getIntent().getData();

        FloatingActionButton floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.members_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewExpenseDialog(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String expenseTitle = mEditText.getText().toString().trim();

                        Intent intent = new Intent(getActivity(), ExpenseInputActivity.class);
                        intent.setData(mTripUri);
                        intent.putExtra(TripContract.ExpenseEntry.COLUMN_ITEM, expenseTitle);
                        startActivity(intent);
                    }
                });
            }
        });

        getActivity().getSupportLoaderManager().initLoader(0, null, this).forceLoad();
        ListView listView = (ListView) rootView.findViewById(R.id.member_list_view);
        mEmptyView = rootView.findViewById(R.id.member_empty_view);
        mEmptyView.setVisibility(View.INVISIBLE);
        listView.setEmptyView(mEmptyView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
                showDeleteDialog(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMember(id);
                    }
                });
            }
        });
        mAdapter = new SummaryCursorAdapter(getContext(), null);
        listView.setAdapter(mAdapter);

        return rootView;
    }



    private void showNewExpenseDialog(DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.add_expense_dialog_title);

        final LinearLayout layout = new LinearLayout(getContext());

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(48, 32, 48, 32);

        final TextInputLayout inputLayout = new TextInputLayout(getContext());
        inputLayout.setHint(getResources().getString(R.string.add_expense_dialog_hint));
        mEditText = new TextInputEditText(getContext());
        inputLayout.addView(mEditText);

        layout.addView(inputLayout, layoutParams);

        builder.setView(layout);


        builder.setPositiveButton(R.string.add, onClickListener);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null)
                    dialog.dismiss();
            }
        });

        builder.create().show();
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


    private void deleteMember(long id){
        Uri uri = ContentUris.withAppendedId(TripContract.MembersEntry.CONTENT_URI, id);
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null, null);
        int balanceColumnIndex = cursor.getColumnIndex(TripContract.MembersEntry.COLUMN_BALANCE);
        cursor.moveToFirst();
        int balance = cursor.getInt(balanceColumnIndex);
        cursor.close();
        if (balance == 0){
            getActivity().getContentResolver().delete(uri, null, null);
        } else{
            Toast.makeText(getContext(), R.string.member_not_deleted, Toast.LENGTH_SHORT).show();

        }

    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String selection = TripContract.MembersEntry.TRIP_ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(mTripUri))};
        return new CursorLoader(getContext(), TripContract.MembersEntry.CONTENT_URI, null, selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        View container = mEmptyView.findViewById(R.id.container_empty);
        container.setVisibility(View.VISIBLE);
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

}
