package co.webserv.android.radu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebView;
import android.webkit.WebViewClient;

class CustomWebViewClient extends WebViewClient {

	Activity mActivity;

	ProgressDialog pd;

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			pd.dismiss();
		}
	};

	public CustomWebViewClient(Activity activity) {
		mActivity = activity;
	}

	@Override
	public boolean shouldOverrideUrlLoading(final WebView view, final String url) {

		if (url.startsWith("mailto:")) {

			Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

			mActivity.startActivity(i);

			return true;
		} else if (url.startsWith("tel:")) {

			Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

			mActivity.startActivity(i);

			return true;
		} else if (url.contains("/share.html")) {

			Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);

			shareIntent.setType("text/plain");
			shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
					mActivity.getString(R.string.share_title));
			shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
					mActivity.getString(R.string.share_text));

			mActivity.startActivity(Intent.createChooser(shareIntent,
					mActivity.getString(R.string.share_title)));

			return true;

		} else if (url.contains("/facebook.html")
				|| url.contains("/twitter.html")) {

			view.loadUrl(url);

			return true;
		}

		pd = ProgressDialog.show((Context) mActivity,
				mActivity.getString(R.string.progress_title),
				mActivity.getString(R.string.progress_message), true);

		Thread thread = new Thread() {
			@Override
			public void run() {
				view.loadUrl(url);
			}
		};

		thread.start();

		return true;

		// return super.shouldOverrideUrlLoading(view, url);
	}

	@Override
	public void onLoadResource(WebView view, String url) {

		if (url.startsWith("http://www.youtube.com")) {

			if (url.contains("event=playbackerror") && url.contains("videoId")) {

				view.reload();

				Uri u = Uri.parse(url);

				String video_id = u.getQueryParameter("videoId");

				String new_url = "http://www.youtube.com/embed/" + video_id;

				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(new_url));

				mActivity.startActivity(i);
			}

		} else if (url.contains("facebook.com") || url.contains("twitter.com")) {

			Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

			mActivity.startActivity(i);

		} else {

			if (pd == null) {
				pd = ProgressDialog.show((Context) mActivity,
						mActivity.getString(R.string.progress_title),
						mActivity.getString(R.string.progress_message), true);
			}

			super.onLoadResource(view, url);
		}

	}

	@Override
	public void onPageFinished(WebView view, String url) {

		if (pd != null) {
			handler.sendEmptyMessage(0);
		}

		super.onPageFinished(view, url);
	}

}
/* $Id$ */