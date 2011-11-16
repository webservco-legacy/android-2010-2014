package co.webserv.android.apocket.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PocketDbAdapter {

	public static final String msKeyId = "_id";
	public static final String msKeyTitle = "title";
	public static final String msKeyType = "type";
	public static final String msKeyCurrency = "currency";
	public static final String msKeyDescription = "description";

	public static final String msDbTable = "pockets";

	private Context mContext;
	private SQLiteDatabase mDb;
	private DbHelper mHelper;

	public PocketDbAdapter(Context c) {

		mContext = c;
	}

	public PocketDbAdapter open() throws SQLException {

		mHelper = new DbHelper(mContext);
		mDb = mHelper.getWritableDatabase();
		return this;
	}

	public void close() {

		mHelper.close();
	}

	public long createPocket(String title, String type, String currency,
			String description) {

		ContentValues values = createContentValues(title, type, currency,
				description);
		return mDb.insert(msDbTable, null, values);
	}

	public boolean updatePocket(long id, String title, String type,
			String currency, String description) {

		ContentValues values = createContentValues(title, type, currency,
				description);
		return mDb.update(msDbTable, values, msKeyId + "=" + id, null) > 0;
	}

	public boolean deletePocket(long id) {

		return mDb.delete(msDbTable, msKeyId + "=" + id, null) > 0;
	}

	public Cursor getPocket(long id) throws SQLException {

		Cursor cursor = mDb.query(true, msDbTable, new String[] {}, msKeyId
				+ "=" + id, null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}

	public Cursor getPockets() {

		return mDb.query(msDbTable, new String[] { msKeyId, msKeyTitle,
				msKeyType, msKeyCurrency, msKeyDescription }, null, null, null,
				null, null);
	}

	// XXX
	public Cursor getPocketList() {
		return mDb
				.rawQuery(
						"SELECT pockets._id, title, type, currency, description,"
								+ " COALESCE(SUM(amount),0) AS total"
								+ " FROM pockets"
								+ " LEFT OUTER JOIN operations ON pockets._id = operations.pocket"
								+ " GROUP BY pockets._id", null);
	}

	private ContentValues createContentValues(String title, String type,
			String currency, String description) {

		ContentValues values = new ContentValues();
		values.put(msKeyTitle, title);
		values.put(msKeyType, type);
		values.put(msKeyCurrency, currency);
		values.put(msKeyDescription, description);
		return values;
	}
}
/* $Id$ */