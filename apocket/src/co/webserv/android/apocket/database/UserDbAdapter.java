package co.webserv.android.apocket.database;

import co.webserv.android.apocket.GeneralAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class UserDbAdapter {

	public static final String msKeyId = "_id";
	public static final String msKeyUsername = "username";
	public static final String msKeyPassword = "password";
	public static final String msKeyName = "name";
	public static final String msKeyDescription = "description";

	public static final String msDbTable = "users";

	private Context mContext;
	private SQLiteDatabase mDb;
	private DbHelper mHelper;

	public UserDbAdapter(Context c) {

		mContext = c;
	}

	public UserDbAdapter open() throws SQLException {
		mHelper = new DbHelper(mContext);
		mDb = mHelper.getWritableDatabase();
		return this;
	}

	public void close() {

		mHelper.close();
	}

	public long createUser(String username, String password, String name,
			String description) {

		String password_md5 = GeneralAdapter.md5(password);

		ContentValues values = createContentValues(username, password_md5,
				name, description);

		return mDb.insert(msDbTable, null, values);
	}

	public boolean updateUser(long id, String username, String password,
			String name, String description) {

		String password_md5 = GeneralAdapter.md5(password);

		ContentValues values = createContentValues(username, password_md5,
				name, description);

		return mDb.update(msDbTable, values, msKeyId + "=" + id, null) > 0;
	}

	public boolean deleteUser(long id) {
		return mDb.delete(msDbTable, msKeyId + "=" + id, null) > 0;
	}

	public Cursor getUser(long id) throws SQLException {
		Cursor cursor = mDb.query(true, msDbTable, new String[] {}, msKeyId
				+ "=" + id, null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}

	public String getUserName(long id) throws SQLException {
		Cursor cursor = mDb.rawQuery("SELECT name" + " FROM users"
				+ " WHERE _id = '" + id + "' LIMIT 1", null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				String name = cursor.getString(cursor
						.getColumnIndexOrThrow(msKeyName));
				cursor.close();
				return name;
			}
		}

		return null;
	}

	public long getUserId(String username, String password) throws SQLException {

		String password_md5 = GeneralAdapter.md5(password);

		Cursor cursor = mDb.rawQuery("SELECT users._id" + " FROM users"
				+ " WHERE username = '" + username + "' AND password = '"
				+ password_md5 + "' LIMIT 1", null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				long id = cursor.getLong(cursor.getColumnIndexOrThrow(msKeyId));
				cursor.close();
				return id;
			}
		}

		return 0;
	}

	private ContentValues createContentValues(String username, String password,
			String name, String description) {

		ContentValues values = new ContentValues();
		values.put(msKeyUsername, username);
		values.put(msKeyPassword, password);
		values.put(msKeyName, name);
		values.put(msKeyDescription, description);
		return values;
	}

}
/* $Id$ */