package co.webserv.android.apocket.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PocketDbAdapter {

    public static final String msKeyId = "_id";
    public static final String msKeyUserId = "user_id";
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

    public long createPocket(long user_id, String title, String type,
            String currency, String description) throws SQLException {

        ContentValues values = createContentValues(user_id, title, type,
                currency, description);
        return mDb.insert(msDbTable, null, values);
    }

    public boolean updatePocket(long id, long user_id, String title,
            String type, String currency, String description)
            throws SQLException {

        ContentValues values = createContentValues(user_id, title, type,
                currency, description);
        return mDb.update(msDbTable, values, msKeyId + "=" + id, null) > 0;
    }

    public boolean deletePocket(long id) throws SQLException {

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

    public Cursor getPockets() throws SQLException {

        return mDb.query(msDbTable, new String[] { msKeyId, msKeyUserId,
                msKeyTitle, msKeyType, msKeyCurrency, msKeyDescription }, null,
                null, null, null, null);
    }

    public Cursor getUserPockets(long user_id) throws SQLException {
        return mDb.rawQuery("SELECT _id, title, type, currency, description"
                + " FROM pockets WHERE user_id = '" + user_id + "'", null);
    }

    public Cursor getPocketList(long user_id) throws SQLException {
        return mDb
                .rawQuery(
                        "SELECT pockets._id, user_id, title, type, currency, description,"
                                + " COALESCE(ROUND(SUM(amount),2), 0) AS total"
                                + " FROM pockets"
                                + " LEFT OUTER JOIN operations ON pockets._id = operations.pocket"
                                + " WHERE pockets.user_id = '" + user_id + "'"
                                + " GROUP BY pockets._id", null);
    }

    public String getPocketName(long id) throws SQLException {
        Cursor cursor = mDb.rawQuery("SELECT title" + " FROM pockets"
                + " WHERE _id = '" + id + "' LIMIT 1", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String title = cursor.getString(cursor
                        .getColumnIndexOrThrow(msKeyTitle));
                cursor.close();
                return title;
            }
        }

        return null;
    }

    private ContentValues createContentValues(long user_id, String title,
            String type, String currency, String description) {

        ContentValues values = new ContentValues();
        values.put(msKeyUserId, user_id);
        values.put(msKeyTitle, title);
        values.put(msKeyType, type);
        values.put(msKeyCurrency, currency);
        values.put(msKeyDescription, description);
        return values;
    }
}
/* $Id$ */