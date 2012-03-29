package co.webserv.android.radu;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
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
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);

		mGeneralAdapter.onConfigurationChanged(newConfig);
	}

	@Override
	public void onBackPressed() {
		
		Log.e("COWEBSERVANDROID", "back event");
		Log.e("COWEBSERVANDROID", "original detected: " + mWebView.getOriginalUrl());
		Log.e("COWEBSERVANDROID", "original string: " + mUrl);
		
		if (mWebView.inCustomView()) {
			mWebView.hideCustomView();
		} else if (mWebView.getOriginalUrl().equals(mUrl)) {
			mGeneralAdapter.onBackPressed();
		} else {
			if (mWebView.canGoBack()) {
				mWebView.clearHistory();
				mWebView.loadUrl(mUrl);
			} else {
				mGeneralAdapter.onBackPressed();
			}
		}
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		Log.d("COWEBSERVANDROID", "keydown!");
		
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			
			Log.d("COWEBSERVANDROID", "back event");
			Log.d("COWEBSERVANDROID", "original detected: " + mWebView.getOriginalUrl());
			Log.d("COWEBSERVANDROID", "original string: " + mUrl);
			
			if (mWebView.inCustomView()) {
				mWebView.hideCustomView();
				return true;
			} else if (mWebView.getOriginalUrl().equals(mUrl)) {
				mGeneralAdapter.onBackPressed();
				return true;
			} else {
				if (mWebView.canGoBack()) {
					mWebView.clearHistory();
					mWebView.loadUrl(mUrl);
					return true;
				} else {
					mGeneralAdapter.onBackPressed();
					return true;
				}
			}
			
		}
		return super.onKeyDown(keyCode, event);
	}
	
}