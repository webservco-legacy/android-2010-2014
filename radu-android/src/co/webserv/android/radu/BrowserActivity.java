package co.webserv.android.radu;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.Menu;

public class BrowserActivity extends Activity {

	private GeneralAdapter mGeneralAdapter;

	private HTML5WebView mWebView;

	private String mUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		mGeneralAdapter = new GeneralAdapter(this);
		mGeneralAdapter.onCreate();

		mWebView = new HTML5WebView(this);

		mUrl = getString(R.string.site_url);

		if (savedInstanceState != null) {
			mWebView.restoreState(savedInstanceState);
		} else {
			mWebView.loadUrl(mUrl);
		}

		setContentView(mWebView.getLayout());
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mWebView.saveState(outState);
	}

	@Override
	public void onStop() {
		super.onStop();
		mWebView.stopLoading();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mWebView.inCustomView()) {
				mWebView.hideCustomView();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// return mGeneralAdapter.onCreateOptionsMenu(menu);
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return mGeneralAdapter.onOptionsItemSelected(item);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);

		mGeneralAdapter.onConfigurationChanged(newConfig);
	}

	@Override
	public void onBackPressed() {

		if (mWebView.getOriginalUrl().equals(mUrl)) {
			mGeneralAdapter.exit();
		} else {
			if (mWebView.canGoBack()) {
				mWebView.goBack();
			} else {
				mGeneralAdapter.exit();
			}
		}
	}
}