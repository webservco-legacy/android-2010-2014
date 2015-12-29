package co.webserv.android.s;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * General android utils.
 * 
 * This should be used in all apps without any modifications.
 * 
 * Put custom app code (static methods) in UtilsApp and not here.
 * 
 * @author radu
 * 
 */
public class UtilsAndroid {

	/**
	 * Log tag.
	 */
	public static final String LOG_TAG = "COWEBSERVANDROID";

	protected static final int NOTIFICATION_ID = 1;

	/**
	 * Splash request code.
	 */
	protected static final int REQUEST_CODE_SPLASH = 10;

	/**
	 * File upload request code.
	 */
	protected static final int REQUEST_CODE_FILEUPLOAD_INIT = 11;

	/**
	 * Application exit code.
	 */
	public static final int RESULT_CODE_EXIT = 1;

	public static final String CAMERA_CAPTURE_FILE_NAME = "capture";

	public static boolean assetExists(Activity a, String asset) {

		UtilsAndroid.logDebug(a, "UtilsAndroid: assetExists: asset: " + asset);

		AssetManager asset_manager;

		List<String> asset_list = null;

		if (asset_list == null) {

			asset_manager = a.getAssets();

			try {

				asset_list = Arrays.asList(asset_manager.list(""));

			} catch (IOException e) {

				Log.e(UtilsAndroid.LOG_TAG,
						"UtilsAndroid: assetExists: " + e.getMessage(), e);
			}
		}

		return asset_list.contains(asset);
	}

	public static void showToast(Context c, String message) {

		UtilsAndroid.logDebug(c, "UtilsAndroid: showToast");

		Toast.makeText(c, message, Toast.LENGTH_LONG).show();
	}

	public static void showNotification(Context c, String title, String message) {

		UtilsAndroid.logDebug(c, "UtilsAndroid: showNotification");

		NotificationManager notification_manager = (NotificationManager) c
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent notification_intent = new Intent(c, AppBrowser.class);

		notification_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Notification.FLAG_SHOW_LIGHTS);

		PendingIntent content_intent = PendingIntent.getActivity(c, 0,
				notification_intent, 0);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(c)
				.setSmallIcon(R.drawable.ic_launcher)
				.setTicker(title)
				.setContentTitle(c.getString(R.string.app_name))
				.setStyle(
						new NotificationCompat.BigTextStyle().bigText(message))
				.setContentText(message);

		Uri alarm_sound = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		builder.setSound(alarm_sound);

		if (c.getString(R.string.set_use_vibrate).length() > 0) {

			builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
		}

		builder.setLights(Color.WHITE, 500, 500);

		builder.setContentIntent(content_intent);

		builder.setAutoCancel(true);

		Notification notification = builder.build();

		notification_manager.notify(UtilsAndroid.NOTIFICATION_ID, notification);
	}

	public static Uri getAppCameraCaptureFileUri(Activity a) {

		UtilsAndroid.logDebug(a, "UtilsAndroid: getNewCameraFileUri");

		File camera_dir;

		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {

			UtilsAndroid
					.logDebug(a,
							"UtilsAndroid: getNewCameraFileUri: external storage present.");

			camera_dir = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		} else {

			UtilsAndroid
					.logDebug(a,
							"UtilsAndroid: getNewCameraFileUri: external storage present.");

			camera_dir = a.getCacheDir();
		}

		if (!camera_dir.exists()) {

			camera_dir.mkdirs();
		}

		return Uri.fromFile(new File(camera_dir + File.separator
				+ UtilsAndroid.CAMERA_CAPTURE_FILE_NAME + ".jpg"));
	}

	public static boolean networkIsConnected(Context c) {

		UtilsAndroid.logDebug(c, "UtilsAndroid: networkIsConnected");

		boolean result;

		ConnectivityManager connectivity_manager = (ConnectivityManager) c
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo network_info = connectivity_manager.getActiveNetworkInfo();

		result = network_info != null && network_info.isConnectedOrConnecting();

		UtilsAndroid.logDebug(c, "UtilsAndroid: networkIsConnected: result: "
				+ result);

		return result;
	}
	
	public static boolean gpsIsConnected(Context c) {
		
		UtilsAndroid.logDebug(c, "UtilsAndroid: gpsIsConnected");
		
		boolean result;
		
		LocationManager location_manager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
		
		result = location_manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		
		UtilsAndroid.logDebug(c, "UtilsAndroid: gpsIsConnected: result: "
				+ result);
		
		return result;
	}

	public static void logDebug(Context c, String msg) {

		if (c.getResources().getString(R.string.devel).length() > 0) {

			Log.d(UtilsAndroid.LOG_TAG, msg);
		}
	}
}