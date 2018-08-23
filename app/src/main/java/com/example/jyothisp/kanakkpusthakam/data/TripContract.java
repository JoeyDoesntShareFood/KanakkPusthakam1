package com.example.jyothisp.kanakkpusthakam.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class TripContract {

    private TripContract(){}

    /**
     * Content authority for the content provider.
     */
    public static final String CONTENT_AUTHORITY = "com.example.jyothisp.kanakkpusthakam";

    /**
     * Uri from the above content authority.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible paths.
     */
    public static final String PATH_TRIPS = "trips";
    public static final String PATH_MENMBERS = "members";
    public static final String PATH_EXPENSES = "expenses";

    public static final class TripsEntry implements BaseColumns{

        /**
         * Uri for this whole table.
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_TRIPS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of trips.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRIPS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single trip.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRIPS;

        /**
         * String with the name of the database table.
         */
        public static final String TABLE_NAME = "trips";


         // Columns in the table.


        /** Type : INTEGER */
        public static final String _ID = BaseColumns._ID;

        /** Type : TEXT */
        public static final String COLUMN_TITLE = "title";

        /** Type : TEXT */
        public static final String COLUMN_DATE = "date";

        /** Type : INTEGER */
        public static final String COLUMN_CASH_PER_PERSON = "cash";

        /** Type : INTEGER
         * possible values : 0,1  */
        public static final String COLUMN_IS_SETTLED = "settled";

        /** Type : INTEGER
         * possible values : 0,1 */
        public static final String COLUMN_IS_COMPLETED = "completed";


    }

    public static final class MembersEntry implements BaseColumns{

        /**
         * Uri for this whole table.
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MENMBERS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of members.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MENMBERS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single member.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MENMBERS;

        /**
         * String with the name of the database table.
         */
        public static final String TABLE_NAME = "members";

        //Columns

        /**
         * Unique ID of the member.
         * Type : INTEGER
         */
        public static final String _ID = BaseColumns._ID;

        /**
         * ID of the trip of the member (foreign key)
         * Type : INTEGER
         */
        public static final String TRIP_ID = "tripid";

        /**
         * Name of the member.
         * Type : TEXT
         */
        public static final String COLUMN_NAME = "name";

        /**
         * Cash spent by the member.
         * Type : INTEGER
         */
        public static final String COLUMN_CASH_SPENT = "cashspent";

        /**
         * Actual expense of the member.
         * Type : INTEGER
         */
        public static final String COLUMN_EXPENSE = "expense";

        /**
         * Balance to be settled by the member.
         * Type : INTEGER
         */
        public static final String COLUMN_BALANCE = "balance";

    }

    public static final class ExpenseEntry implements  BaseColumns{

        /**
         * Uri for this whole table.
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_EXPENSES);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of expenses.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EXPENSES;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single expense.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EXPENSES;

        /**
         * String with the name of the database table.
         */
        public static final String TABLE_NAME = "expenses";

        //Columns

        /**
         * Unique ID of the member.
         * Type : INTEGER
         */
        public static final String _ID = BaseColumns._ID;

        /**
         * ID of the trip of the member (foreign key)
         * Type : INTEGER
         */
        public static final String TRIP_ID = "tripid";

        /**
         * The type of expense.
         * Type : TEXT
         */
        public static final String COLUMN_ITEM = "item";

        /**
         * Integer array with the cash spent by each member for the expense.
         * Type : TEXT
         */
        public static final String
                COLUMN_CASH_ROLLED = "cashrolled";

        /**
         * Integer array with the expense of each member.
         * Type : INTEGER
         */
        public static final String COLUMN_EXPENSE = "expense";

        /**
         * Boolean array with the involvement of each member in the expense.
         * Type : TEXT
         */
        public static final String COLUMN_IS_INVLOVED = "involvement";

        /**
         * String with the time of the expense.
         * Type : TEXT
         */
        public static final String COLUMN_TIME = "time";

    }


}
