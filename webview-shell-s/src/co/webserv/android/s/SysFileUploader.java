package co.webserv.android.s;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.ipaulpro.afilechooser.utils.FileUtils;

/**
 * Adapted from:
 * http://delimitry.blogspot.hu/2011/08/android-upload-progress.html
 * 
 * @author radu
 * 
 */
public class SysFileUploader extends AsyncTask<Void, Integer, String> implements
		DialogInterface.OnCancelListener {

	public SysAsyncResponse mSysAsyncResponse;

	private Activity mActivity;

	private ProgressDialog mProgressDialog;

	private String mUrl;

	private File mFile;

	private boolean mUseProgessDialog = false;

	public SysFileUploader(Activity a, String u, File f) {

		mActivity = a;

		mUrl = u;

		mFile = f;

		UtilsAndroid.logDebug(mActivity, "SysFileUploader: url: " + mUrl);
	}

	public void setUseProgressDialog(boolean useProgressDialog) {

		UtilsAndroid.logDebug(mActivity,
				"SysFileUploader: setUseProgressDialog: " + useProgressDialog);

		mUseProgessDialog = useProgressDialog;

	}

	@Override
	protected void onPreExecute() {

		if (mUseProgessDialog) {

			UtilsAndroid.logDebug(mActivity,
					"SysFileUploader: onPreExecute: progress dialog enabled");

			mProgressDialog = new ProgressDialog(mActivity);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressDialog.setMessage(mActivity
					.getString(R.string.progress_uploading));
			mProgressDialog.setCancelable(false);
			mProgressDialog.setMax((int) mFile.length());
			mProgressDialog.show();
		} else {
			UtilsAndroid.logDebug(mActivity,
					"SysFileUploader: onPreExecute: progress dialog disabled");
		}

	}

	@Override
	protected String doInBackground(Void... v) {

		UtilsAndroid.logDebug(mActivity, "SysFileUploader: doInBackground");

		String response = null;

		HttpURLConnection.setFollowRedirects(false);

		HttpURLConnection connection = null;

		String file_name = mFile.getName();

		String file_content_type = FileUtils.getMimeType(mFile);

		UtilsAndroid.logDebug(mActivity,
				"SysFileUploader: doInBackground: file_name: " + file_name);
		UtilsAndroid.logDebug(mActivity,
				"SysFileUploader: doInBackground: file_content_type: "
						+ file_content_type);

		try {

			connection = (HttpURLConnection) new URL(mUrl).openConnection();

			if (mActivity.getString(R.string.set_use_ua_version).length() > 0) {

				String user_agent_string = UtilsApp
						.getCustomUserAgentString(mActivity);

				connection.setRequestProperty("User-Agent", user_agent_string);
			}

			String httpd_auth_user = mActivity
					.getString(R.string.httpd_auth_user);

			if (httpd_auth_user.length() > 0) {

				byte[] auth = (httpd_auth_user + ":" + mActivity
						.getString(R.string.httpd_auth_password)).getBytes();
				String basic = Base64.encodeToString(auth, Base64.NO_WRAP);
				connection
						.setRequestProperty("Authorization", "Basic " + basic);
			}

			connection.setRequestMethod("POST");

			connection.setRequestProperty("Cookie",
					UtilsHttp.getCookieFromAppCookieManager(mUrl));

			String boundary = "---------------------------boundary";

			String tail = "\r\n--" + boundary + "--\r\n";

			connection.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + boundary);

			connection.setDoOutput(true);

			String metadata_part = "--"
					+ boundary
					+ "\r\n"
					+ "Content-Disposition: form-data; name=\"metadata\"\r\n\r\n"
					+ "" + "\r\n";

			String file_header1 = "--" + boundary + "\r\n"
					+ "Content-Disposition: form-data; name=\""
					+ mActivity.getString(R.string.set_upload_field_name)
					+ "\"; filename=\"" + file_name + "\"\r\n"
					+ "Content-Type: " + file_content_type + "\r\n"
					+ "Content-Transfer-Encoding: binary\r\n";

			long file_length = mFile.length() + tail.length();

			String file_header2 = "Content-length: " + file_length + "\r\n";
			String file_header = file_header1 + file_header2 + "\r\n";
			String string_data = metadata_part + file_header;

			long request_length = string_data.length() + file_length;
			connection
					.setRequestProperty("Content-length", "" + request_length);
			connection.setFixedLengthStreamingMode((int) request_length);
			connection.connect();

			UtilsAndroid.logDebug(mActivity,
					"SysFileUploader: doInBackground: string_data:"
							+ string_data);

			DataOutputStream out = new DataOutputStream(
					connection.getOutputStream());

			try {

				out.writeBytes(string_data);
				out.flush();

				int progress = 0;
				int bytes_read = 0;
				byte buf[] = new byte[1024];
				BufferedInputStream buf_input = new BufferedInputStream(
						new FileInputStream(mFile));

				while ((bytes_read = buf_input.read(buf)) != -1) {

					// write output
					out.write(buf, 0, bytes_read);
					out.flush();
					progress += bytes_read;

					if (mUseProgessDialog) {

						// update progress bar
						publishProgress(progress);
					}
				}

				buf_input.close();

				// Write closing boundary and close stream
				out.writeBytes(tail);
				out.flush();
				out.close();

				response = UtilsHttp.convertStreamToString(connection
						.getInputStream());

			} catch (SocketException e) {

				response = "error_epipe"; // handled in onPostExecute

				Log.e(UtilsAndroid.LOG_TAG, "SysFileUploader: doInBackground: "
						+ e.getMessage(), e);
			}

			UtilsAndroid.logDebug(mActivity,
					"SysFileUploader: doInBackground: response: " + response);

		} catch (Exception e) {

			Log.e(UtilsAndroid.LOG_TAG,
					"SysFileUploader: doInBackground: " + e.getMessage(), e);

		} finally {

			if (connection != null) {

				connection.disconnect();
			}
		}

		return response;
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {

		if (mUseProgessDialog) {

			mProgressDialog.setProgress((int) (progress[0]));
		}
	}

	@Override
	protected void onPostExecute(String result) {

		UtilsAndroid.logDebug(mActivity,
				"SysFileUploader: onPostExecute: result is: " + result);

		if (mUseProgessDialog) {

			mProgressDialog.dismiss();
		}

		if (result.equals("error_epipe")) {
			UtilsAndroid.showToast(mActivity,
					mActivity.getString(R.string.connection_error_epipe));
		} else {

			mSysAsyncResponse.asyncTaskFinishFileUpload(result.trim());
		}
	}

	@Override
	public void onCancel(DialogInterface dialog) {

		cancel(true);

		dialog.dismiss();

		UtilsAndroid.logDebug(mActivity, "SysFileUploader: onCancel");
	}

}