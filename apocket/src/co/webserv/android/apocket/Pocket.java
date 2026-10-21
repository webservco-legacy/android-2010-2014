package co.webserv.android.apocket;

import java.util.HashMap;

public class Pocket extends HashMap<String, String> {

    private static final long serialVersionUID = -5450453557104685660L;

    public Long mId;
    public Long mUserId;
    public String mTitle;
    public String mType;
    public String mCurrency;
    public String mDescription;
    public String mTotal;

    public static final String msKeyUserId = "user_id";
    public static final String msKeyTitle = "title";
    public static final String msKeyType = "type";
    public static final String msKeyCurrency = "currency";
    public static final String msKeyDescription = "description";
    public static final String msKeyTotal = "total";

    public Pocket(Long _id, Long user_id, String title, String type,
            String currency, String description, String total) {
        mId = _id;
        mUserId = user_id;
        mTitle = title;
        mType = type;
        mCurrency = currency;
        mDescription = description;
        mTotal = total;
    }

    @Override
    public String get(Object k) {
        String key = (String) k;

        if (msKeyUserId.equals(key)) {
            return mUserId.toString(); // XXX ???
        } else if (msKeyTitle.equals(key)) {
            return mTitle;
        } else if (msKeyType.equals(key)) {
            return mType;
        } else if (msKeyCurrency.equals(key)) {
            return mCurrency;
        } else if (msKeyDescription.equals(key)) {
            return mDescription;
        } else if (msKeyTotal.equals(key)) {
            return mTotal;
        }
        return null;
    }
}
/* $Id$ */