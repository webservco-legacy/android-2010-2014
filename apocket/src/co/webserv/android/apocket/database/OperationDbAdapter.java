package co.webserv.android.apocket.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class OperationDbAdapter {

    public static final String msKeyId = "_id";
    public static final String msKeyPocket = "pocket";
    public static final String msKeyDate = "date";
    public static final String msKeyAmount = "amount";
    public static final String msKeyNotes = "notes";

    public static final String msDbTable = "operations";

    private Context mContext;
    private SQLiteDatabase mDb;
    private DbHelper mHelper;

    public OperationDbAdapter(Context c) {
        mContext = c;
    }

    public OperationDbAdapter open() throws SQLException {
        mHelper = new DbHelper(mContext);
        mDb = mHelper.getWritableDatabase();
        return this;
    }

    public void close() {

        mHelper.close();
    }

    public long addOperation(long pocket, String date, double amount,
            String notes) {
        ContentValues values = createContentValues(pocket, date, amount, notes);
        return mDb.insert(msDbTable, null, values);
    }

    public boolean updateOperation(long id, long pocket, String date,
            double amount, String notes) {
        ContentValues values = createContentValues(pocket, date, amount, notes);
        return mDb.update(msDbTable, values, msKeyId + "=" + id, null) > 0;
    }

    public boolean deleteOperation(long id) {
        return mDb.delete(msDbTable, msKeyId + "=" + id, null) > 0;
    }

    public Cursor getOperation(long id) throws SQLException {
        Cursor cursor = mDb.query(true, msDbTable, new String[] {}, msKeyId
                + "=" + id, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor getPocketOperations(long pocket) {
        return mDb.query(msDbTable, new String[] { msKeyId, msKeyPocket,
                msKeyDate, msKeyAmount, msKeyNotes }, msKeyPocket + "="
                + pocket, null, null, null, msKeyDate + " DESC, " + msKeyPocket
                + " DESC, " + msKeyId + " DESC", null);
    }

    public Cursor getOperations() {
        return mDb.query(msDbTable, new String[] { msKeyId, msKeyPocket,
                msKeyDate, msKeyAmount, msKeyNotes }, null, null, null, null,
                msKeyDate + " DESC, " + msKeyPocket + " DESC, " + msKeyId
                        + " DESC", null);
    }

    private ContentValues createContentValues(long pocket, String date,
            double amount, String notes) {
        ContentValues values = new ContentValues();
        values.put(msKeyPocket, pocket);
        values.put(msKeyDate, date);
        values.put(msKeyAmount, amount);
        values.put(msKeyNotes, notes);
        return values;
    }
}
/* $Id$ */