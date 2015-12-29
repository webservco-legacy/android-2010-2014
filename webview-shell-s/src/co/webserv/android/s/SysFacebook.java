package co.webserv.android.s;

import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

public class SysFacebook {

	private static Activity mActivity;

	private static CallbackManager mCallbackManager;

	private static ShareDialog mShareDialog;

	protected static void onCreate(Bundle savedInstanceState, final Activity a) {

		mActivity = a;

		UtilsAndroid.logDebug(mActivity, "SysFacebook: onCreate");

		FacebookSdk.sdkInitialize(a.getApplicationContext());

		mCallbackManager = CallbackManager.Factory.create();
	}

	protected static void onActivityResult(int requestCode, int resultCode, Intent data) {

		UtilsAndroid.logDebug(mActivity, "SysFacebook: onActivityResult");

		mCallbackManager.onActivityResult(requestCode, resultCode, data);
	}

	protected static void onResume() {

		UtilsAndroid.logDebug(mActivity, "SysFacebook: onResume");
	}

	protected static void onSaveInstanceState(Bundle outState) {

		UtilsAndroid.logDebug(mActivity, "SysFacebook: onSaveInstanceState");
	}

	public static void onPause() {

		UtilsAndroid.logDebug(mActivity, "SysFacebook: onPause");
	}

	public static void onDestroy() {

		UtilsAndroid.logDebug(mActivity, "SysFacebook: onDestroy");
	}

	public static void hybridLogin(final Activity a, final WebView wv) {

		mActivity = a;

		UtilsAndroid.logDebug(mActivity, "SysFacebook: hybridLogin");

		List<String> permissionNeeds = Arrays.asList("email", "public_profile", "user_friends");
		LoginManager.getInstance().logInWithReadPermissions(mActivity, permissionNeeds);

		LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {

			@Override
			public void onSuccess(LoginResult result) {

				UtilsAndroid.logDebug(mActivity, "SysFacebook: onCreate: onSuccess: " + result.toString());

				GraphRequest mGraphRequest = GraphRequest.newMeRequest(result.getAccessToken(),
						new GraphRequest.GraphJSONObjectCallback() {

					@Override
					public void onCompleted(JSONObject object, GraphResponse response) {

						if (response.getError() != null) {

							UtilsAndroid.logDebug(mActivity, "SysFacebook: hybridLogin: GraphRequest error: "
									+ response.getError().getErrorMessage());
						} else {

							try {

								String user_email = object.getString("email");

								if (user_email != null && !user_email.isEmpty()) {

									UtilsAndroid.logDebug(mActivity,
											"SysFacebook: hybridLogin: GraphRequest: user email: " + user_email);

									String hash = UtilsString
											.getMd5Hash(mActivity.getString(R.string.facebook_hash_prefix) + user_email
													+ mActivity.getString(R.string.facebook_hash_suffix));

									String user_id = object.getString("id");

									UtilsAndroid.logDebug(mActivity,
											"SysFacebook:hybridLogin: GraphRequest: user name: " + user_id);

									String user_name = object.getString("name");

									UtilsAndroid.logDebug(mActivity,
											"SysFacebook:hybridLogin: GraphRequest: user name: " + user_name);

									String url = mActivity.getString(R.string.app_url) + "fbdroid/" + user_email + "/"
											+ hash + "/" + user_id + "/" + user_name;

									UtilsAndroid.logDebug(mActivity,
											"SysFacebook: hybridLogin: GraphRequest: url: " + url);

									String parsed_url = Uri.parse(url).toString();

									UtilsAndroid.logDebug(mActivity,
											"SysFacebook: hybridLogin: GraphRequest: parsed_url: " + parsed_url);

									final class LoadUrlRunnable implements Runnable {

										private WebView mWebView;
										private String mUrl;

										public LoadUrlRunnable(final WebView vw, String url) {

											this.mWebView = wv;
											this.mUrl = url;
										}

										@Override
										public void run() {

											this.mWebView.loadUrl(this.mUrl);

										}
									}

									LoadUrlRunnable loadUrlRunnable = new LoadUrlRunnable(wv, url);
									wv.post(loadUrlRunnable);

								} else {

									UtilsAndroid.logDebug(mActivity,
											"SysFacebook: hybridLogin: GraphRequest error: user email not available");

									UtilsAndroid.showToast(mActivity,
											mActivity.getString(R.string.facebook_error_noemail));
								}

							} catch (JSONException e) {

								UtilsAndroid.logDebug(mActivity,
										"SysFacebook: hybridLogin: meRequest JSONException: " + e.getMessage());
							}
						}

						UtilsAndroid.logDebug(mActivity, "SysFacebook: onCreate: onSuccess: " + response.toString());

					}
				});

				Bundle parameters = new Bundle();
				parameters.putString("fields", "id,name,email");
				mGraphRequest.setParameters(parameters);
				mGraphRequest.executeAsync();

			}

			@Override
			public void onError(FacebookException error) {
				UtilsAndroid.logDebug(mActivity, "SysFacebook: hybridLogin: onError: " + error.getMessage());
			}

			@Override
			public void onCancel() {

				UtilsAndroid.logDebug(mActivity, "SysFacebook: hybridLogin: onCancel");

			}
		});
	}

	public static void share(Activity a, String shareName, String shareCaption, String shareDescription,
			String shareLink, String shareImageUrl) {

		UtilsAndroid.logDebug(mActivity, "SysFacebook: share");

		mActivity = a;

		mShareDialog = new ShareDialog(mActivity);

		mShareDialog.registerCallback(mCallbackManager, new FacebookCallback<Sharer.Result>() {

			@Override
			public void onSuccess(com.facebook.share.Sharer.Result result) {
				UtilsAndroid.showToast(mActivity, mActivity.getString(R.string.action_done_ok));
			}

			@Override
			public void onCancel() {
				UtilsAndroid.logDebug(mActivity, "SysFacebook: share: onCancel");

			}

			@Override
			public void onError(FacebookException error) {
				UtilsAndroid.logDebug(mActivity, "SysFacebook: share: onError:" + error.getMessage());

			}
		});

		if (ShareDialog.canShow(ShareLinkContent.class)) {

			ShareLinkContent linkContent = new ShareLinkContent.Builder().setContentTitle(shareName)
					.setImageUrl(Uri.parse(shareImageUrl)).setContentDescription(shareDescription)
					.setContentUrl(Uri.parse(shareLink)).build();

			ShareDialog.show(mActivity, linkContent);

		} else {

			UtilsAndroid.logDebug(mActivity, "SysFacebook: share: can't show ShareDialog");
		}
	}
}