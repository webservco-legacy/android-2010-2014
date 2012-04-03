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

import co.webserv.android.apocket.database.UserDbAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class GeneralAdapter {

	protected static final int RESULT_CLOSE_ALL = 1;

	protected static final boolean LOG = true;

	protected static final String TITLE_SEP = ": ";

	protected static final String TAG = "COWEBSERVANDROID";

	public static final String msKeyUserId = "user_id";

	public long mUserId;
	public String mUserName;

	protected TextView mActivityTitle;

	private Activity mActivity;

	private UserDbAdapter mUserDbAdapter;

	protected SharedPreferences mSharedPreferences;

	public GeneralAdapter(Activity a) {
		mActivity = a;
	}

	public boolean onCreate() {

		mActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);

		if (LOG) {
			Log.d(TAG, mActivity.getLocalClassName()
					+ ": loading shared preferences.");
		}

		mSharedPreferences = mActivity.getSharedPreferences(
				mActivity.getPackageName(), Activity.MODE_PRIVATE);

		if (LOG) {
			Log.d(TAG, mActivity.getLocalClassName()
					+ ": checking saved version number.");
		}

		int saved_version = mSharedPreferences.getInt("version", 0);
		int version = GeneralAdapter.getVersionCode(mActivity);

		if (saved_version == 0) {

			if (LOG) {
				Log.d(TAG, mActivity.getLocalClassName()
						+ ": saved version number is 0, must be first run.");
			}

			if (LOG) {
				Log.d(TAG, mActivity.getLocalClassName()
						+ ": detected version number " + version + ".");
			}

			mSharedPreferences.edit().putInt("version", version).commit();

			if (LOG) {
				Log.d(TAG, mActivity.getLocalClassName()
						+ ": saved version number to shared preferences.");
				Log.d(TAG,
						mActivity.getLocalClassName()
								+ ": being first run, we load the new account activity.");
			}

			Intent iu = new Intent(mActivity, UserAddActivity.class);
			mActivity.startActivityForResult(iu, RESULT_CLOSE_ALL);
			exit();

			return false;

		} else {

			if (LOG) {
				Log.d(TAG, mActivity.getLocalClassName() + ": not first run.");
				Log.d(TAG, mActivity.getLocalClassName()
						+ ": saved version number is " + saved_version + ".");
			}

			if (LOG) {
				Log.d(TAG, mActivity.getLocalClassName()
						+ ": checking version update.");
			}

			if (version > saved_version) {

				if (LOG) {
					Log.d(TAG, mActivity.getLocalClassName()
							+ ": current version (" + version
							+ ") is greater than saved version ("
							+ saved_version + ").");
					Log.d(TAG, mActivity.getLocalClassName()
							+ ": we have a fresh installed update!");
				}

				mSharedPreferences.edit().putInt("version", version).commit();

				if (LOG) {
					Log.d(TAG,
							mActivity.getLocalClassName()
									+ ": saved new version number to shared preferences.");
				}

				Toast.makeText(mActivity,
						mActivity.getString(R.string.update_message),
						Toast.LENGTH_LONG).show();
			} else {
				if (LOG) {
					Log.d(TAG, mActivity.getLocalClassName()
							+ ": no version update.");
				}
			}

		}

		if (LOG) {
			Log.d(TAG, mActivity.getLocalClassName()
					+ ": checking current activity.");
		}

		if (mActivity instanceof UserActivity) {

			if (LOG) {
				Log.d(TAG, mActivity.getLocalClassName()
						+ ": current activity is user login.");
			}

		} else if (mActivity instanceof UserAddActivity) {

			if (LOG) {
				Log.d(TAG, mActivity.getLocalClassName()
						+ ": current activity is new account.");
			}

		} else {

			if (LOG) {
				Log.d(TAG,
						mActivity.getLocalClassName()
								+ ": current activity is not user login or new account.");
				Log.d(TAG, mActivity.getLocalClassName()
						+ ": checking user login status.");
			}

			Bundle extras = mActivity.getIntent().getExtras();

			if (extras != null) {

				mUserId = extras.getLong(msKeyUserId, 0);

				if (LOG) {
					Log.d(TAG, mActivity.getLocalClassName()
							+ ": got bundle extras user id: " + mUserId + ".");
				}

			} else {

				if (LOG) {
					Log.d(TAG, mActivity.getLocalClassName()
							+ ": bundle extras is empty.");
				}
			}

			if (mUserId == 0) {

				if (LOG) {
					Log.d(TAG, mActivity.getLocalClassName()
							+ ": bundle extras user id empty or non existent.");
					Log.d(TAG, mActivity.getLocalClassName()
							+ ": starting user login activity.");
				}

				Intent iu = new Intent(mActivity, UserActivity.class);
				mActivity.startActivityForResult(iu, RESULT_CLOSE_ALL);
				exit();
				return false;

			} else {

				if (LOG) {
					Log.d(TAG, mActivity.getLocalClassName()
							+ ": checking bundle extras user id.");
				}

				mUserDbAdapter = new UserDbAdapter(mActivity);
				mUserDbAdapter.open();
				mUserName = mUserDbAdapter.getUserName(mUserId);
				mUserDbAdapter.close();

				if (mUserName == null) {

					if (LOG) {
						Log.d(TAG, mActivity.getLocalClassName()
								+ ": bundle extras user id check failed.");
						Log.d(TAG, mActivity.getLocalClassName()
								+ ": starting user login activity.");
					}

					Intent iu = new Intent(mActivity, UserActivity.class);
					mActivity.startActivityForResult(iu, RESULT_CLOSE_ALL);
					exit();
					return false;

				} else {

					if (LOG) {
						Log.d(TAG, mActivity.getLocalClassName()
								+ ": bundle extras user id check successfull.");
						Log.d(TAG, mActivity.getLocalClassName()
								+ ": setting user name: " + mUserName + ".");
					}

				}
			}
		}

		return true;

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_CLOSE_ALL:
			exit();
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
			Intent ip = new Intent(mActivity, PocketListActivity.class);
			ip.putExtra(msKeyUserId, mUserId);
			mActivity.startActivityForResult(ip, RESULT_CLOSE_ALL);
			return true;

		case R.id.pocket_add:
			Intent ipa = new Intent(mActivity, PocketDetailsActivity.class);
			ipa.putExtra(msKeyUserId, mUserId);
			mActivity.startActivityForResult(ipa, RESULT_CLOSE_ALL);
			return true;

		case R.id.operation_list:
			Intent io = new Intent(mActivity, OperationListActivity.class);
			io.putExtra(msKeyUserId, mUserId);
			mActivity.startActivityForResult(io, RESULT_CLOSE_ALL);
			return true;

		case R.id.operation_add:
			Intent iod = new Intent(mActivity, OperationDetailsActivity.class);
			iod.putExtra(msKeyUserId, mUserId);
			mActivity.startActivityForResult(iod, RESULT_CLOSE_ALL);
			return true;

		case R.id.about:
			Intent ia = new Intent(mActivity, AboutActivity.class);
			ia.putExtra(msKeyUserId, mUserId);
			mActivity.startActivityForResult(ia, RESULT_CLOSE_ALL);
			return true;

		case R.id.exit:
			exit();
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
			Intent ip = new Intent(mActivity, PocketListActivity.class);
			ip.putExtra(msKeyUserId, mUserId);
			mActivity.startActivityForResult(ip, RESULT_CLOSE_ALL);
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
			return context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;

		} catch (android.content.pm.PackageManager.NameNotFoundException e) {
			return 0;
		}
	}
}
/* $Id$ */