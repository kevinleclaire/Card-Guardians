package com.v41.tp3mobile.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ApplicationDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 30;

    public ApplicationDatabaseHelper(Context context, String databaseFileName) {
        super(context, databaseFileName, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Faire ceci pour chaque table que vous avez
        sqLiteDatabase.execSQL(CardTable.CREATE_TABLE_SQL);
        sqLiteDatabase.execSQL(DeckTable.CREATE_TABLE_SQL);
        sqLiteDatabase.execSQL(DeckHasCardTable.CREATE_TABLE_SQL);
        sqLiteDatabase.execSQL(PlayerInfoTable.CREATE_TABLE_SQL);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //Faire ceci pour chaque table que vous avez
        sqLiteDatabase.execSQL(CardTable.DROP_TABLE_SQL);
        sqLiteDatabase.execSQL(DeckTable.DROP_TABLE_SQL);
        sqLiteDatabase.execSQL(DeckHasCardTable.DROP_TABLE_SQL);
        sqLiteDatabase.execSQL(PlayerInfoTable.DROP_TABLE_SQL);
        onCreate(sqLiteDatabase);
    }
}
