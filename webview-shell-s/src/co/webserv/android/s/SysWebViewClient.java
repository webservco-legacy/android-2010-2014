package co.webserv.android.s;

import java.lang.ref.WeakReference;
import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.webkit.CookieSyncManager;
import android.webkit.HttpAuthHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SysWebViewClient extends WebViewClient {

	private Activity mActivity;

	private SysWebBrowser mSysWebBrowser;

	public ProgressDialog mProgressDialog;

	public static CustomHandler mHandler;

	public static class CustomHandler extends Handler {

		private final WeakReference<ProgressDialog> mProgressDialog;

		CustomHandler(ProgressDialog pd) {

			mProgressDialog = new WeakReference<ProgressDialog>(pd);
		}

		@Override
		public void handleMessage(Message msg) {

			ProgressDialog pd = mProgressDialog.get();

			if (pd != null) {
				pd.dismiss();
			}
		}

	}

	public SysWebViewClient(SysWebBrowser wb) {

		mSysWebBrowser = wb;

		mActivity = mSysWebBrowser.getActivity();
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {

		UtilsAndroid.logDebug(mActivity, "SysWebViewClient: shouldOverrideUrlLoading: url: " + url);

		if (url.endsWith(".mp4")) {

			UtilsAndroid.logDebug(mActivity, "SysWebViewClient: shouldOverrideUrlLoading: mp4 intent");

			Uri uri = Uri.parse(url);

			Intent i = new Intent(Intent.ACTION_VIEW);

			i.setDataAndType(uri, "video/*");

			mActivity.startActivity(i);

			return true;

		} else if (url.endsWith(".mp3")) {

			UtilsAndroid.logDebug(mActivity, "SysWebViewClient: shouldOverrideUrlLoading: mp3 intent");

			Uri uri = Uri.parse(url);

			Intent i = new Intent(Intent.ACTION_VIEW);

			i.setDataAndType(uri, "audio/*");

			mActivity.startActivity(i);

			return true;

		} else if (url.startsWith(mActivity.getString(R.string.app_url))
				|| (mActivity.getString(R.string.set_use_facebook_api_web).length() > 0
						&& url.contains("facebook.com"))) {

			UtilsAndroid.logDebug(mActivity, "SysWebViewClient: shouldOverrideUrlLoading: in-app url");

			return false;

		} else {

			UtilsAndroid.logDebug(mActivity, "SysWebViewClient: shouldOverrideUrlLoading: external url");

			Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

			mActivity.startActivity(i);

			return true;
		}
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {

		UtilsAndroid.logDebug(mActivity, "SysWebViewClient: onPageStarted: url: " + url);

		if (mActivity.getString(R.string.set_use_facebook_api_web).length() > 0
				&& url.equals("https://m.facebook.com/dialog/feed")) {

			UtilsAndroid.logDebug(mActivity, "SysWebViewClient: onPageStarted: facebook url handling");

			SysWebBrowser.loadLastAppUrl(view, mActivity, "");

			return;
		}

		if (mProgressDialog == null) {

			// pd is null. show

			mProgressDialog = ProgressDialog.show((Context) mActivity, mActivity.getString(R.string.progress_title),
					mActivity.getString(R.string.progress_message), true);

		} else {

			if (mProgressDialog.isShowing()) {

			} else {

				// pd not null, but not showing. show

				mProgressDialog = ProgressDialog.show((Context) mActivity, mActivity.getString(R.string.progress_title),
						mActivity.getString(R.string.progress_message), true);
			}
		}

		super.onLoadResource(view, url);
	}

	@Override
	public void onPageFinished(WebView view, String url) {

		UtilsAndroid.logDebug(mActivity, "SysWebViewClient: onPageFinished: url: " + url);

		if (mProgressDialog != null) {

			mHandler = new CustomHandler(mProgressDialog);

			mHandler.sendEmptyMessage(0);
		}

		UtilsAndroid.logDebug(mActivity, "SysWebViewClient: onPageFinished: CookieSyncManager manual syc");

		CookieSyncManager.getInstance().sync();

		if (mActivity.getString(R.string.set_use_gcm).length() > 0) {

			if (url.startsWith("http")) {

				UtilsApp.updateGcmRegistrationId(mActivity);
			}
		}

		super.onPageFinished(view, url);
	}

	@Override
	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

		UtilsAndroid.logDebug(mActivity, "SysWebViewClient: onReceivedError: description: " + description);
		UtilsAndroid.logDebug(mActivity, "SysWebViewClient: onReceivedError: failingUrl: " + failingUrl);

		String asset_path = "file:///android_asset/";

		String language = Locale.getDefault().getLanguage();

		UtilsAndroid.logDebug(mActivity, "SysWebViewClient: onReceivedError: language: " + language);

		String asset_name = "webview_error_" + language + ".html";

		if (UtilsAndroid.assetExists(mActivity, asset_name)) {

			asset_path = asset_path.concat(asset_name);

		} else {

			asset_path = asset_path.concat("webview_error_en.html");
		}

		UtilsAndroid.logDebug(mActivity, "SysWebViewClient: onReceivedError: asset_path: " + asset_path);

		view.loadUrl(asset_path);
	}

	@Override
	public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {

		String httpd_auth_user = mActivity.getString(R.string.httpd_auth_user);

		if (httpd_auth_user.length() > 0) {

			handler.proceed(httpd_auth_user, mActivity.getString(R.string.httpd_auth_password));
		} else {

			super.onReceivedHttpAuthRequest(view, handler, host, realm);
		}

	}
}