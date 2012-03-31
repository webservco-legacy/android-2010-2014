package co.webserv.android.apocket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;

public class AboutActivity extends Activity {

	private GeneralAdapter mGeneralAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		mGeneralAdapter = new GeneralAdapter(this);
		mGeneralAdapter.onCreate();

		setContentView(R.layout.main);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mGeneralAdapter.onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return mGeneralAdapter.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return mGeneralAdapter.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		mGeneralAdapter.onBackPressed();
	}
}
/* $Id$ */