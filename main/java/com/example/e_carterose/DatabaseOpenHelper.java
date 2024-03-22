package com.example.e_carterose;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseOpenHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "bdd_ede_v15.db";
    private static final int DATABASE_VERSION = 1;
    private final Context context;

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = null;
    }

    public Context getContext() {
        return context;
    }

}
