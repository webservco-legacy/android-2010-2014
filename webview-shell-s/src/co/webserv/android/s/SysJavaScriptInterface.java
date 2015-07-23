package co.webserv.android.s;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.facebook.Session;

/**
 * JavaScript Interface.
 * 
 * Add here the native methods you want to access from the html5 app.
 * 
 * @author radu
 * 
 */
public class SysJavaScriptInterface {

	private Activity mActivity;

	private WebView mWebView;

	public SysJavaScriptInterface(Activity a, WebView wv) {

		mActivity = a;

		mWebView = wv;
	}

	@JavascriptInterface
	public void showToast(String message) {

		UtilsAndroid.logDebug(mActivity,
				"SysJavaScriptInterface: showToast: message: " + message);

		UtilsAndroid.showToast(mActivity, message);
	}

	@JavascriptInterface
	public void showNotification(String title, String message) {

		UtilsAndroid.logDebug(mActivity,
				"SysJavaScriptInterface: showNotification: title: " + title);
		UtilsAndroid
				.logDebug(mActivity,
						"SysJavaScriptInterface: showNotification: message: "
								+ message);

		UtilsAndroid.showNotification(mActivity, title, message);
	}

	@JavascriptInterface
	public void fileUploadInit(String taskType, String taskAsset) {

		UtilsAndroid.logDebug(mActivity,
				"SysJavaScriptInterface: FileUploadInit: task type: "
						+ taskType);
		UtilsAndroid.logDebug(mActivity,
				"SysJavaScriptInterface: FileUploadInit: task asset: "
						+ taskAsset);

		if (taskType != null) {

			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

			if (UtilsApp.shouldUploadImage(mActivity, taskType)) {

				UtilsAndroid
						.logDebug(mActivity,
								"SysJavaScriptInterface: FileUploadInit: ShouldUploadImage");

				intent.setType("image/*");
			}

			intent.addCategory(Intent.CATEGORY_OPENABLE);

			UtilsSharedPreferences.putString(mActivity, "taskType", taskType);
			UtilsSharedPreferences.putString(mActivity, "taskAsset", taskAsset);

			Intent chooser_intent = Intent.createChooser(intent,
					mActivity.getString(R.string.file_upload));

			if (mActivity.getPackageManager().hasSystemFeature(
					PackageManager.FEATURE_CAMERA)) {

				UtilsAndroid
						.logDebug(mActivity,
								"SysJavaScriptInterface: FileUploadInit: device has camera.");

				Uri camera_file_uri = UtilsAndroid
						.getAppCameraCaptureFileUri(mActivity);

				Intent intent_capture = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);

				UtilsAndroid.logDebug(mActivity,
						"SysJavaScriptInterface: FileUploadInit: setting camera capture file path: "
								+ camera_file_uri);

				intent_capture.putExtra(MediaStore.EXTRA_OUTPUT,
						camera_file_uri);

				chooser_intent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
						new Parcelable[] { intent_capture });
			}

			if (chooser_intent.resolveActivity(mActivity.getPackageManager()) != null) {

				UtilsAndroid
						.logDebug(mActivity,
								"SysJavaScriptInterface: FileUploadInit: starting intent");

				mActivity.startActivityForResult(chooser_intent,
						UtilsAndroid.REQUEST_CODE_FILEUPLOAD_INIT);
			} else {

				Log.e(UtilsAndroid.LOG_TAG,
						"SysJavaScriptInterface: FileUploadInit: no app to handle the intent, aborting.");
			}

		}
	}

	@JavascriptInterface
	public void facebookLogin() {

		UtilsAndroid.logDebug(mActivity,
				"SysJavaScriptInterface: facebookLogin");

		SysFacebook.openSession(mActivity, null);
	}

	@JavascriptInterface
	public void facebookHybridLogin() {

		UtilsAndroid.logDebug(mActivity,
				"SysJavaScriptInterface: facebookHybridLogin");

		Runnable runHybridLogin = new Runnable() {

			@Override
			public void run() {

				UtilsAndroid
						.logDebug(mActivity,
								"SysJavaScriptInterface: facebookHybridLogin: Runnable(): run()");

				SysFacebook.hybridLogin(mActivity, mWebView);
			}
		};

		SysFacebook.openSession(mActivity, runHybridLogin);
	}

	@JavascriptInterface
	public void facebookLogout() {

		UtilsAndroid.logDebug(mActivity,
				"SysJavaScriptInterface: facebookLogout");

		Session session = Session.getActiveSession();

		if (session == null || session.isClosed()) {

			UtilsAndroid.logDebug(mActivity,
					"SysJavaScriptInterface: facebookLogout: nothing to do");

		} else {

			UtilsAndroid.logDebug(mActivity,
					"SysJavaScriptInterface: facebookLogout: closing session");

			session.closeAndClearTokenInformation();
		}
	}

	@JavascriptInterface
	public void facebookShare(final String shareName,
			final String shareCaption, final String shareDescription,
			final String shareLink, final String shareImageUrl) {

		UtilsAndroid.logDebug(mActivity,
				"SysJavaScriptInterface: facebookShare");

		Runnable runShare = new Runnable() {

			@Override
			public void run() {

				mActivity.runOnUiThread(new Runnable() {

					@Override
					public void run() {

						UtilsAndroid
								.logDebug(mActivity,
										"SysJavaScriptInterface: facebookShare: Runnable(): run()");

						SysFacebook.share(mActivity, shareName, shareCaption,
								shareDescription, shareLink, shareImageUrl);

					}
				});
			}
		};

		Session session = Session.getActiveSession();

		if (session.isOpened()) {

			UtilsAndroid
					.logDebug(mActivity,
							"SysJavaScriptInterface: facebookShare: session already opened");

			runShare.run();

		} else {

			UtilsAndroid.logDebug(mActivity,
					"SysJavaScriptInterface: facebookShare: opening session");

			SysFacebook.openSession(mActivity, runShare);

		}
	}
}