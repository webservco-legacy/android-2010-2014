package co.webserv.android.apocket;

import java.util.HashMap;

public class Operation extends HashMap<String, String> {

    private static final long serialVersionUID = 385783739432470865L;

    public Long mId;
    public String mPocket;
    public String mDate;
    public String mAmount;
    public String mCurrency;
    public String mNotes;

    public static final String msKeyPocket = "pocket";
    public static final String msKeyDate = "date";
    public static final String msKeyAmount = "amount";
    public static final String msKeyCurrency = "currency";
    public static final String msKeyNotes = "notes";

    public Operation(Long _id, String pocket, String date, String amount, String currency,
            String notes) {
        mId = _id;
        mPocket = pocket;
        mDate = date;
        mAmount = amount;
        mCurrency = currency;
        mNotes = notes;
    }

    @Override
    public String get(Object k) {
        String key = (String) k;
        if (msKeyPocket.equals(key)) {
            return mPocket;
        } else if (msKeyDate.equals(key)) {
            return mDate;
        } else if (msKeyAmount.equals(key)) {
            return mAmount;
        } else if (msKeyCurrency.equals(key)) {
            return mCurrency;
        } else if (msKeyNotes.equals(key)) {
            return mNotes;
        }
        return null;
    }
}
/* $Id$ */