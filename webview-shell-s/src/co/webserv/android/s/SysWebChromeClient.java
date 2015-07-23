package co.webserv.android.s;

import android.app.Activity;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.WebChromeClient;

public class SysWebChromeClient extends WebChromeClient {

	private Activity mActivity;

	public SysWebChromeClient(Activity a) {

		mActivity = a;
	}

	@Override
	public void onGeolocationPermissionsShowPrompt(String origin,
			Callback callback) {

		UtilsAndroid.logDebug(mActivity,
				"SysWebChromeClient: onGeolocationPermissionsShowPrompt");

		callback.invoke(origin, true, false);
	}

	@Override
	public boolean onConsoleMessage(ConsoleMessage consoleMessage) {

		String message = consoleMessage.sourceId() + ":"
				+ consoleMessage.lineNumber() + ":" + consoleMessage.message();

		switch (consoleMessage.messageLevel()) {

		case ERROR:
			Log.e(UtilsAndroid.LOG_TAG, message);
			break;
		case WARNING:
			Log.w(UtilsAndroid.LOG_TAG, message);
			break;
		case TIP:
			Log.i(UtilsAndroid.LOG_TAG, message);
			break;
		case DEBUG:
		case LOG:
			Log.d(UtilsAndroid.LOG_TAG, message);
		default:
			break;
		}

		return true;
	}
}