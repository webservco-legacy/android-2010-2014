package co.webserv.android.s;

import com.facebook.FacebookSdk;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import bolts.AppLinks;

public class AppBrowser extends Activity implements SysAsyncResponse {

	private SysActivity mSysActivity;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		mSysActivity.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		mSysActivity = new SysActivity(AppBrowser.this);

		mSysActivity.OnCreate(savedInstanceState);

		/**
		 * Set splash layout.
		 */
		setContentView(R.layout.browser);

		mSysActivity.webBrowserInit();

		String home_url = getString(R.string.app_url) + getString(R.string.app_home_page);

		if (getString(R.string.set_use_tutorial).length() > 0) {

			Boolean show_tutorial = UtilsSharedPreferences.getBoolean(getApplicationContext(), "show_tutorial", true);

			UtilsAndroid.logDebug(this, "AppBrowser: onCreate: show_tutorial: " + show_tutorial);

			if (show_tutorial) {

				home_url = getString(R.string.app_url) + "tutorial";

				UtilsSharedPreferences.putBoolean(getApplicationContext(), "show_tutorial", false);
			}
		}

		UtilsAndroid.logDebug(this, "AppBrowser: onCreate: home_url: " + home_url);

		if (getString(R.string.set_use_applinks).length() > 0) {

			UtilsAndroid.logDebug(this,
					"AppBrowser: onCreate: AppLinks enabled in configuration, initializing Facebook SDK.");

			FacebookSdk.sdkInitialize(this.getApplicationContext());

			Uri applinks_url = AppLinks.getTargetUrlFromInboundIntent(this, getIntent());

			if (applinks_url != null) {

				UtilsAndroid.logDebug(this, "AppBrowser: onCreate: AppLinks url: " + applinks_url);

				home_url = applinks_url.toString();

				if (getString(R.string.set_applinks_process_url).length() > 0) {

					home_url = home_url.replace("Preview", "");

					UtilsAndroid.logDebug(this, "AppBrowser: onCreate: AppLinks processed url: " + home_url);
				}

				UtilsAndroid.logDebug(this, "AppBrowser: onCreate: loading AppLinks url instead of homepage");
			}
		}

		mSysActivity.webBrowserLoadUrl(home_url);
	}

	@Override
	protected void onDestroy() {

		mSysActivity.OnDestroy();

		super.onDestroy();
	}

	@Override
	protected void onPause() {

		mSysActivity.onPause();

		super.onPause();
	}

	@Override
	protected void onResume() {

		super.onResume();

		mSysActivity.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);

		mSysActivity.onSaveInstanceState(outState);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		UtilsAndroid.logDebug(this, "AppBrowser: onKeyDown: " + keyCode);

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			mSysActivity.onBackPressed();

			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void asyncTaskFinishFileUpload(String uploadResultCode) {

		UtilsAndroid.logDebug(this, "AppBrowser: asyncTaskFinishFileUpload: result: " + uploadResultCode);

		mSysActivity.handleFileUploadResult(uploadResultCode);

	}
}