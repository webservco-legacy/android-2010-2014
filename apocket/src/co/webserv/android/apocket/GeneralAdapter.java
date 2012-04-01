package co.webserv.android.apocket;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;

public class GeneralAdapter {

	protected static final int RESULT_CLOSE_ALL = 1;

	protected static final String TAG = "COWEBSERVANDROID";

	public static final String msKeyUserId = "user_id";

	public long mUserId;

	private Activity mActivity;

	SharedPreferences mSharedPreferences;

	public GeneralAdapter(Activity a) {
		mActivity = a;
	}

	public void onCreate() {

		mActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);

		mSharedPreferences = mActivity.getSharedPreferences(
				mActivity.getPackageName(), Activity.MODE_PRIVATE);

		int saved_version = mSharedPreferences.getInt("version", 0);

		if (saved_version == 0) {

			int version = GeneralAdapter.getVersionCode(mActivity);
			mSharedPreferences.edit().putInt("version", version).commit();

			Intent iu = new Intent(mActivity, UserAddActivity.class);
			mActivity.startActivityForResult(iu, RESULT_CLOSE_ALL);
			exit();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_CLOSE_ALL:
			mActivity.setResult(RESULT_CLOSE_ALL);
			mActivity.finish();
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = mActivity.getMenuInflater();
		inflater.inflate(R.menu.options, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.pocket_list:
			mActivity.startActivityForResult(new Intent(mActivity,
					PocketListActivity.class), RESULT_CLOSE_ALL);
			return true;

		case R.id.pocket_add:
			mActivity.startActivityForResult(new Intent(mActivity,
					PocketDetailsActivity.class), RESULT_CLOSE_ALL);
			return true;

		case R.id.operation_list:
			mActivity.startActivityForResult(new Intent(mActivity,
					OperationListActivity.class), RESULT_CLOSE_ALL);
			return true;

		case R.id.operation_add:
			mActivity.startActivityForResult(new Intent(mActivity,
					OperationDetailsActivity.class), RESULT_CLOSE_ALL);
			return true;

		case R.id.about:
			mActivity.startActivityForResult(new Intent(mActivity,
					AboutActivity.class), RESULT_CLOSE_ALL);
			return true;

		case R.id.exit:
			mActivity.setResult(RESULT_CLOSE_ALL);
			mActivity.finish();
			return true;
		default:
			return mActivity.getParent().onOptionsItemSelected(item);
		}
	}

	public void copyFile(File source, File destination) throws IOException {

		InputStream input = new FileInputStream(source);
		OutputStream output = new FileOutputStream(destination);

		byte[] buffer = new byte[1024];

		int length;

		while ((length = input.read(buffer)) > 0) {
			output.write(buffer, 0, length);
		}
		input.close();
		output.close();

	}

	public void onBackPressed() {

		if (mActivity instanceof PocketListActivity) {
			exit();
		} else {
			exit();
			mActivity.startActivityForResult(new Intent(mActivity,
					PocketListActivity.class), RESULT_CLOSE_ALL);
		}
	}

	public void exit() {
		mActivity.setResult(RESULT_CLOSE_ALL);
		mActivity.finish();
	}

	/**
	 * http://stackoverflow.com/questions/3934331/android-how-to-encrypt-a-
	 * string
	 * 
	 * @param s
	 * @return
	 */
	public static String md5(String s) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
			digest.update(s.getBytes(), 0, s.length());
			String hash = new BigInteger(1, digest.digest()).toString(16);
			return hash;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static int getVersionCode(Context context) {
		try {
			// XXX
			// ComponentName comp = new ComponentName(context, cls);
			// PackageInfo pinfo =
			// context.getPackageManager().getPackageInfo(comp.getPackageName(),
			// 0);
			// return pinfo.versionName;
			return context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;

		} catch (android.content.pm.PackageManager.NameNotFoundException e) {
			return 0;
		}
	}
}
/* $Id$ */