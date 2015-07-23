package co.webserv.android.s;

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;

public class SysUncaughtExceptionHandler implements UncaughtExceptionHandler {

	private UncaughtExceptionHandler mUncaughtExceptionHandler;

	private Context mContext;

	public SysUncaughtExceptionHandler(Context c) {

		mContext = c;

		mUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {

		UtilsAndroid.logDebug(mContext,
				"SysUncaughtExceptionHandler: uncaughtException");

		UtilsSharedPreferences.putBoolean(mContext, "app_is_active", false);

		mUncaughtExceptionHandler.uncaughtException(thread, ex);
	}

}