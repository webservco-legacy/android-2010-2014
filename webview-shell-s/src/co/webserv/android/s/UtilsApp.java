package co.webserv.android.s;

import java.io.IOException;
import java.util.Arrays;

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Current app utils.
 * 
 * Put custom app code (static methods) here and not in UtilsAndroid.
 * 
 * @author radu
 * 
 */
public class UtilsApp {

	private static GoogleCloudMessaging mGoogleCloudMessaging;

	public static boolean shouldUploadImage(final Activity a, String taskType) {

		UtilsAndroid.logDebug(a, "UtilsApp: ShouldUploadImage: taskType: "
				+ taskType);

		String[] image_tasks = a.getResources().getStringArray(
				R.array.upload_image_tasks);

		return Arrays.asList(image_tasks).contains(taskType) ? true : false;
	}

	public static void updateGcmRegistrationId(final Activity a) {

		new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {

				if (!UtilsAndroid.networkIsConnected(a)) {

					UtilsAndroid
							.logDebug(a,
									"UtilsApp: getGcmRegistrationId: doInBackground: no network connection.");

					return false;
				}

				Boolean result;

				String gcm_registration_id = UtilsSharedPreferences.getString(
						a, "gcm_registration_id", "");

				if (gcm_registration_id.length() == 0) {

					try {

						if (mGoogleCloudMessaging == null) {

							UtilsAndroid
									.logDebug(a,
											"UtilsApp: getGcmRegistrationId: doInBackground: init GoogleCloudMessaging");

							mGoogleCloudMessaging = GoogleCloudMessaging
									.getInstance(a.getApplicationContext());

						}

						gcm_registration_id = mGoogleCloudMessaging.register(a
								.getString(R.string.gcm_project_number));

						UtilsAndroid.logDebug(a,
								"UtilsApp: getGcmRegistrationId: doInBackground: gcm_registration_id: "
										+ gcm_registration_id);

					} catch (IOException e) {

						Log.e(UtilsAndroid.LOG_TAG,
								"UtilsApp: getGcmRegistrationId: doInBackground: "
										+ e.getMessage(), e);
					}

				} else {

					UtilsAndroid
							.logDebug(
									a,
									"UtilsApp: getGcmRegistrationId: doInBackground: gcm_registration_id already present");
				}

				if (gcm_registration_id.length() > 0) {

					UtilsSharedPreferences.putString(a, "gcm_registration_id",
							gcm_registration_id);

					String url = a.getString(R.string.app_url) + "droid_token/"
							+ gcm_registration_id;

					UtilsAndroid.logDebug(a,
							"UtilsApp: updateGcmRegistrationId: doInBackground: url: "
									+ url);

					String response = UtilsHttp.getStream(url, true,
							a.getString(R.string.httpd_auth_user),
							a.getString(R.string.httpd_auth_password), a);

					UtilsAndroid.logDebug(a,
							"UtilsApp: updateGcmRegistrationId: doInBackground: response: "
									+ response);

					result = true;

				} else {

					UtilsAndroid
							.logDebug(a,
									"UtilsApp: updateGcmRegistrationId: doInBackground: gcm_registration_id empty");

					result = false;
				}

				return result;
			}

			protected void onPostExecute(Boolean result) {

				UtilsAndroid.logDebug(a,
						"UtilsApp: updateGcmRegistrationId: onPostExecute: result: "
								+ result);
			}

		}.execute(null, null, null);
	}

	/**
	 * Get user id.
	 * 
	 * Returns 0 if error or user is not logged in.
	 * 
	 * @param a
	 * @return
	 */
	public static int getUserId(final Activity a) {

		String url = a.getString(R.string.app_url) + "Users/App/GetId";

		UtilsAndroid.logDebug(a, "UtilsApp: getUserId: url: " + url);

		String data = UtilsHttp.getStream(url, true,
				a.getString(R.string.httpd_auth_user),
				a.getString(R.string.httpd_auth_password), a);

		if (data.length() > 0) {

			UtilsAndroid.logDebug(a, "UtilsApp: getUserId: data: " + data);

			return Integer.parseInt(data);
		}

		UtilsAndroid.logDebug(a, "UtilsApp: getUserId: no data");

		return 0;
	}

	public static String getAppVersionName(final Activity a) {

		String app_version_name = "";

		try {

			app_version_name = a.getPackageManager().getPackageInfo(
					a.getPackageName(), 0).versionName;

		} catch (NameNotFoundException e) {

			Log.e(UtilsAndroid.LOG_TAG,
					"UtilsApp: getAppVersionName" + e.getMessage(), e);
		}

		UtilsAndroid.logDebug(a,
				"UtilsApp: getAppVersionName: app_version_name:"
						+ app_version_name);

		return app_version_name;
	}

	public static String getCustomUserAgentString(final Activity a) {

		String user_agent = System.getProperty("http.agent");

		return UtilsApp.getCustomUserAgentString(a, user_agent);
	}

	public static String getCustomUserAgentString(final Activity a,
			String userAgent) {

		String app_version_name = UtilsApp.getAppVersionName(a);

		return userAgent + " WebApp/" + app_version_name;
	}
}