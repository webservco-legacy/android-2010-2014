package co.webserv.android.radu;

import java.io.File;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class GeneralAdapter {

	private static final int REQUEST_CODE = 10;
	protected static final int RESULT_CLOSE_ALL = 1;

	static Locale locale;
	static final String language = "en";

	private Activity mActivity;

	public boolean mCheckConnection = true;

	public GeneralAdapter(Activity a) {
		mActivity = a;
	}

	public void onCreate() {
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(mActivity);
		Configuration config = mActivity.getBaseContext().getResources()
				.getConfiguration();

		String lang = settings.getString("language", language);

		locale = new Locale(lang);
		Locale.setDefault(locale);
		config.locale = locale;
		mActivity
				.getBaseContext()
				.getResources()
				.updateConfiguration(
						config,
						mActivity.getBaseContext().getResources()
								.getDisplayMetrics());

		mActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);

		String wvcDb = Environment.getDataDirectory().toString() + "/data/"
				+ mActivity.getPackageName() + "/databases/webviewCache.db";

		File f_wvcDb = new File(wvcDb);

		if (f_wvcDb.exists()) {

			if (f_wvcDb.length() > 12288) {
				mCheckConnection = false;
			}

		}

		if (mCheckConnection && noConnection()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
			builder.setIcon(R.drawable.ic_launcher)
					.setTitle(mActivity.getString(R.string.conerr_title))
					.setMessage(mActivity.getString(R.string.conerr_message))
					.setCancelable(false)
					.setPositiveButton(
							mActivity.getString(R.string.conerr_retry),
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
									mActivity.setResult(RESULT_CLOSE_ALL);
									mActivity.finish();

									Intent i = new Intent(mActivity, mActivity
											.getClass());
									mActivity.startActivityForResult(i,
											RESULT_CLOSE_ALL);
								}
							})
					.setNegativeButton(
							mActivity.getString(R.string.conerr_cancel),
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
									mActivity.setResult(RESULT_CLOSE_ALL);
									mActivity.finish();
								}
							}).create().show();
		}

	}

	public void onConfigurationChanged(Configuration newConfig) {

		/**
		 * Deprecated, use in activity!
		 * 
		 * mActivity.getParent().onConfigurationChanged(newConfig);
		 */

		newConfig.locale = locale;
		Locale.setDefault(locale);
		mActivity
				.getBaseContext()
				.getResources()
				.updateConfiguration(
						newConfig,
						mActivity.getBaseContext().getResources()
								.getDisplayMetrics());

	}

	public boolean noConnection() {

		ConnectivityManager connectivityManager = (ConnectivityManager) mActivity
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

		return (networkInfo != null && networkInfo.isConnectedOrConnecting()) ? false
				: true;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_CLOSE_ALL:
			mActivity.setResult(RESULT_CLOSE_ALL);
			mActivity.finish();
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		default:
			return mActivity.getParent().onOptionsItemSelected(item);
		}
	}

	public void onBackPressed() {

		Bundle extras = mActivity.getIntent().getExtras();
		if (extras != null) {

			boolean main = extras.getBoolean("main");

			if (main == true) {
				mActivity.finish();
				mActivity.moveTaskToBack(true);
			} else {
				mActivity.finish();
				Intent i = new Intent(mActivity, BrowserActivity.class);
				i.putExtra("main", true);
				mActivity.startActivityForResult(i, REQUEST_CODE);
			}
		} else {
			mActivity.finish();
			Intent i = new Intent(mActivity, BrowserActivity.class);
			i.putExtra("main", true);
			mActivity.startActivityForResult(i, REQUEST_CODE);
		}

		// mActivity.getParent().onBackPressed();
	}
	
	public void exit() {
		mActivity.finish();
		mActivity.moveTaskToBack(true);
	}
}
/* $Id$ */