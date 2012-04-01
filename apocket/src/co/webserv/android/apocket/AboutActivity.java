package co.webserv.android.apocket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class AboutActivity extends Activity {

	private GeneralAdapter mGeneralAdapter;

	private TextView mContent;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		mGeneralAdapter = new GeneralAdapter(this);
		mGeneralAdapter.onCreate();

		setContentView(R.layout.main);

		mContent = (TextView) findViewById(R.id.content);

		mContent.setText(Html.fromHtml(getString(R.string.content_about)));
		mContent.setMovementMethod(LinkMovementMethod.getInstance());
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