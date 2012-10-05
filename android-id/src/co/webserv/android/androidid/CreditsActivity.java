package co.webserv.android.androidid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

public class CreditsActivity extends Activity {

	private GeneralAdapter mGeneralAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		/**
		 * General Adapter
		 */
		mGeneralAdapter = new GeneralAdapter(this);

		/**
		 * General functionality.
		 */
		mGeneralAdapter.onCreate();

		/**
		 * Set browser layout.
		 */
		setContentView(R.layout.main);

		mGeneralAdapter.initActivity(3);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		mGeneralAdapter.onActivityResult(requestCode, resultCode, data);

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onDestroy() {

		mGeneralAdapter.onDestroy();

		super.onDestroy();
	}

	@Override
	protected void onPause() {

		mGeneralAdapter.onPause();

		super.onPause();
	}

	@Override
	protected void onResume() {

		super.onResume();

		mGeneralAdapter.onResume();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			mGeneralAdapter.onBackPressed();

			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return mGeneralAdapter.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		return mGeneralAdapter.onOptionsItemSelected(item);
	}
}