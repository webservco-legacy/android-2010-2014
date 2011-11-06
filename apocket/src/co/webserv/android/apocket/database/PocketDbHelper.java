package co.webserv.android.apocket.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PocketDbHelper extends SQLiteOpenHelper {

	private static final String msDbName = "apocket";

	private static final int msDbVersion = 1;

	private static final String msTableCreate = "create table pockets (_id integer primary key autoincrement, title text not null,"
			+ "type text not null, currency text not null, description text not null);";

	public PocketDbHelper(Context context) {

		super(context, msDbName, null, msDbVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(msTableCreate);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("DROP TABLE IF EXISTS pockets");
		onCreate(db);
	}
}
/* $Id$ */