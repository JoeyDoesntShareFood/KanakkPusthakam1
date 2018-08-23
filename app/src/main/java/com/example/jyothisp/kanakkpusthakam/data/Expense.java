package com.example.jyothisp.kanakkpusthakam.data;

import android.app.Activity;

public class Expense {

    private String mName;
    private double mCash;
    private long mID;

    public Expense(String name, double cash) {
        mCash = cash;
        mName = name;
    }

    public Expense(String name, int cash, long id) {
        mCash = cash;
        mName = name;
        mID = id;
    }

    public double getmCash() {
        return mCash;
    }

    public String getmName() {
        return mName;
    }

    public long getmID() {
        return mID;
    }
}
