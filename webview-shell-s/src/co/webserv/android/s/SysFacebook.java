package co.webserv.android.s;

import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphPlace;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;

public class SysFacebook {

	private static Activity mActivity;

	private static UiLifecycleHelper uiHelper;

	protected static void onCreate(Bundle savedInstanceState, Activity a) {

		mActivity = a;

		UtilsAndroid.logDebug(mActivity, "SysFacebook: onCreate");

		uiHelper = new UiLifecycleHelper(mActivity, null);
		uiHelper.onCreate(savedInstanceState);
	}

	protected static void onActivityResult(int requestCode, int resultCode,
			Intent data) {

		UtilsAndroid.logDebug(mActivity, "SysFacebook: onActivityResult");

		uiHelper.onActivityResult(requestCode, resultCode, data,
				new FacebookDialog.Callback() {
					@Override
					public void onError(FacebookDialog.PendingCall pendingCall,
							Exception error, Bundle data) {

						UtilsAndroid.logDebug(mActivity,
								"SysFacebook: onActivityResult: onError");

						Log.e(UtilsAndroid.LOG_TAG,
								"SysFacebook: onActivityResult: "
										+ error.getMessage(), error);
					}

					@Override
					public void onComplete(
							FacebookDialog.PendingCall pendingCall, Bundle data) {

						UtilsAndroid.logDebug(mActivity,
								"SysFacebook: onActivityResult: onComplete");
					}
				});
	}

	protected static void onResume() {

		UtilsAndroid.logDebug(mActivity, "SysFacebook: onResume");

		uiHelper.onResume();
	}

	protected static void onSaveInstanceState(Bundle outState) {

		UtilsAndroid.logDebug(mActivity, "SysFacebook: onSaveInstanceState");

		uiHelper.onSaveInstanceState(outState);
	}

	public static void onPause() {

		UtilsAndroid.logDebug(mActivity, "SysFacebook: onPause");

		uiHelper.onPause();
	}

	public static void onDestroy() {

		UtilsAndroid.logDebug(mActivity, "SysFacebook: onDestroy");

		uiHelper.onDestroy();
	}

	public static void openSession(Activity a, final Runnable postExecute) {

		UtilsAndroid.logDebug(mActivity, "SysFacebook: openSession");

		mActivity = a;

		StatusCallback statusCallback = new Session.StatusCallback() {

			@Override
			public void call(Session session, SessionState state,
					Exception exception) {
				UtilsAndroid.logDebug(mActivity,
						"SysFacebook: openSession: statusCallback: call");

				UtilsAndroid.logDebug(mActivity,
						"SysFacebook: openSession: statusCallback: call: state: "
								+ state);

				if (session.isOpened() && postExecute != null) {

					UtilsAndroid
							.logDebug(mActivity,
									"SysFacebook: openSession: statusCallback: running postExecute");

					postExecute.run();
				}
			}
		};

		Session.OpenRequest openRequest = new Session.OpenRequest(mActivity);

		openRequest.setPermissions(Arrays.asList("email", "public_profile",
				"user_friends"));

		openRequest.setCallback(statusCallback);

		Session session = Session.getActiveSession();

		if (session == null || !session.isOpened()) {

			UtilsAndroid.logDebug(mActivity,
					"SysFacebook: openSession: opening session");

			session = new Session(mActivity);

			Session.setActiveSession(session);

			session.openForRead(openRequest);

		} else {

			UtilsAndroid.logDebug(mActivity,
					"SysFacebook: openSession: session already opened");

			if (postExecute != null) {

				UtilsAndroid.logDebug(mActivity,
						"SysFacebook: openSession: running postExecute");

				postExecute.run();
			}
		}
	}

	public static void hybridLogin(final Activity a, final WebView wv) {

		UtilsAndroid.logDebug(a, "SysFacebook: hybridLogin");

		Session session = Session.getActiveSession();

		if (session.isOpened()) {

			UtilsAndroid.logDebug(a,
					"SysFacebook: hybridLogin: session is opened");

			GraphUserCallback graphUserCallback = new Request.GraphUserCallback() {

				@Override
				public void onCompleted(GraphUser user, Response response) {

					UtilsAndroid
							.logDebug(a,
									"SysFacebook: hybridLogin: graphUserCallback: onCompleted");

					if (user != null) {

						String user_id = user.getId();

						UtilsAndroid.logDebug(a,
								"SysFacebook:hybridLogin: graphUserCallback: onCompleted: user id: "
										+ user_id);

						Object email = user.getProperty("email");
						
						//XXX
						//Object test = response.getGraphObject().getProperty("email");
						//UtilsAndroid.logDebug(a, "XXXXXXXXXX: " + test);
						
						//XXX
						
						if (email != null) { //XXX
							
							

							String user_email = user.getProperty("email")
									.toString();

							UtilsAndroid.logDebug(a,
									"SysFacebook: hybridLogin: graphUserCallback: onCompleted: user email: "
											+ user_email);

							String hash = UtilsString
									.getMd5Hash(mActivity
											.getString(R.string.facebook_hash_prefix)
											+ user_email
											+ mActivity
													.getString(R.string.facebook_hash_suffix));

							String user_name = user.getName();

							UtilsAndroid.logDebug(a,
									"SysFacebook:hybridLogin: graphUserCallback: onCompleted: user name: "
											+ user_name);

							GraphPlace location = user.getLocation();

							String location_qs = "";

							if (location != null) {

								location_qs = "/" + location.getName();

								UtilsAndroid.logDebug(a,
										"SysFacebook:hybridLogin: graphUserCallback: onCompleted: user location qs: "
												+ location_qs);
							} else {

								UtilsAndroid
										.logDebug(
												a,
												"SysFacebook:hybridLogin: graphUserCallback: onCompleted: user location not available");
							}

							String url = mActivity.getString(R.string.app_url)
									+ "fbdroid/" + user_email + "/" + hash
									+ "/" + user_id + "/" + user_name
									+ location_qs;

							UtilsAndroid.logDebug(a,
									"SysFacebook: hybridLogin: graphUserCallback: onCompleted: url: "
											+ url);

							String parsed_url = Uri.parse(url).toString();

							UtilsAndroid.logDebug(a,
									"SysFacebook: hybridLogin: graphUserCallback: onCompleted: parsed_url: "
											+ parsed_url);

							final class LoadUrlRunnable implements Runnable {

								private WebView mWebView;
								private String mUrl;

								public LoadUrlRunnable(final WebView vw,
										String url) {

									this.mWebView = wv;
									this.mUrl = url;
								}

								@Override
								public void run() {

									this.mWebView.loadUrl(this.mUrl);

								}
							}

							LoadUrlRunnable loadUrlRunnable = new LoadUrlRunnable(
									wv, url);
							wv.post(loadUrlRunnable);

						} else {

							UtilsAndroid
									.logDebug(
											a,
											"SysFacebook: hybridLogin: graphUserCallback: onCompleted: user email not available");

							UtilsAndroid
									.logDebug(mActivity,
											"SysJavaScriptInterface: facebookLogout: closing session");

							Session session = Session.getActiveSession();

							session.closeAndClearTokenInformation();

							UtilsAndroid
									.showToast(
											a,
											a.getString(R.string.facebook_error_noemail));
						}
					}
				}
			};

			Request request = Request.newMeRequest(session, graphUserCallback);

			Bundle parameters = request.getParameters();

			request.setParameters(parameters);

			request.executeAsync();

		} else {

			UtilsAndroid.logDebug(a,
					"SysFacebook: hybridLogin: session not opened");
		}
	}

	public static void share(Activity a, String shareName, String shareCaption,
			String shareDescription, String shareLink, String shareImageUrl) {

		UtilsAndroid.logDebug(mActivity, "SysFacebook: share");

		mActivity = a;

		Session session = Session.getActiveSession();

		if (session == null || session.isClosed()) {

			UtilsAndroid.logDebug(mActivity,
					"SysFacebook: share: session not available!");

		} else {

			Bundle params = new Bundle();

			if (shareName != null) {

				params.putString("name", shareName);

				UtilsAndroid.logDebug(mActivity, "SysFacebook: share: name: "
						+ shareName);
			}

			if (shareCaption != null) {

				params.putString("caption", shareCaption);

				UtilsAndroid.logDebug(mActivity,
						"SysFacebook: share: caption: " + shareCaption);
			}

			if (shareDescription != null) {

				params.putString("description", shareDescription);

				UtilsAndroid.logDebug(mActivity,
						"SysFacebook: share: description: " + shareDescription);
			}

			if (shareLink != null) {

				params.putString("link", shareLink);

				UtilsAndroid.logDebug(mActivity, "SysFacebook: share: link: "
						+ shareLink);
			}

			if (shareImageUrl != null) {

				params.putString("picture", shareImageUrl);

				UtilsAndroid.logDebug(mActivity,
						"SysFacebook: share: picture: " + shareImageUrl);
			}

			WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(mActivity,
					session, params)).setOnCompleteListener(
					new OnCompleteListener() {

						@Override
						public void onComplete(Bundle values,
								FacebookException error) {

							UtilsAndroid.logDebug(mActivity,
									"SysFacebook: share: onComplete");

							if (error == null) {

								final String post_id = values
										.getString("post_id");

								if (post_id != null) {

									UtilsAndroid.logDebug(mActivity,
											"SysFacebook: share: onComplete: post_id: "
													+ post_id);

									UtilsAndroid.showToast(mActivity, mActivity
											.getString(R.string.action_done_ok));

								} else {

									UtilsAndroid
											.logDebug(mActivity,
													"SysFacebook: share: onComplete: CANCELED");

									UtilsAndroid.showToast(
											mActivity,
											mActivity
													.getString(R.string.action_cancelled));
								}

							} else if (error instanceof FacebookOperationCanceledException) {

								UtilsAndroid
										.logDebug(mActivity,
												"SysFacebook: share: onComplete: CANCELED (FB)");

								UtilsAndroid.showToast(mActivity, mActivity
										.getString(R.string.action_cancelled));

							} else {

								UtilsAndroid
										.logDebug(mActivity,
												"SysFacebook: share: onComplete: ERROR");

								UtilsAndroid.showToast(mActivity, mActivity
										.getString(R.string.action_done_ko));
							}
						}
					}).build();

			feedDialog.show();
		}
	}
}