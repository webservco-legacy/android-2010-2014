package co.webserv.android.apocket.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static final String msDbName = "apocket";

    private static final int msDbVersion = 1;

    private static final String msTableCreateUsers = "create table users "
            + "(_id integer primary key autoincrement, username text not null, "
            + "password text not null, name text not null, description text not null);";

    private static final String msTableCreatePockets = "create table pockets "
            + "(_id integer primary key autoincrement, user_id integer not null, "
            + "title text not null, type text not null, currency text not null, "
            + "description text not null);";

    private static final String msTableCreateOperations = "create table operations "
            + "(_id integer primary key autoincrement, pocket integer not null, "
            + "date text not null, amount real not null, notes text not null);";

    public DbHelper(Context context) {

        super(context, msDbName, null, msDbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(msTableCreateUsers);

        db.execSQL(msTableCreatePockets);

        db.execSQL(msTableCreateOperations);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS users");

        db.execSQL("DROP TABLE IF EXISTS pockets");

        db.execSQL("DROP TABLE IF EXISTS operations");

        onCreate(db);
    }
}
/* $Id$ */