package co.webserv.android.apocket;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;

public class GeneralAdapter {

	protected static final int RESULT_CLOSE_ALL = 1;

	private Activity mActivity;

	public GeneralAdapter(Activity a) {
		mActivity = a;
	}

	public void onCreate() {
		mActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);
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

		case R.id.test:
			mActivity.startActivityForResult(new Intent(mActivity,
					OperationListActivity.class), RESULT_CLOSE_ALL);
			return true;
		case R.id.test2:
			mActivity.startActivityForResult(new Intent(mActivity,
					OperationDetailsActivity.class), RESULT_CLOSE_ALL);
			return true;

		case R.id.home:
			mActivity.startActivityForResult(new Intent(mActivity,
					HomeActivity.class), RESULT_CLOSE_ALL);
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
}
/* $Id$ */