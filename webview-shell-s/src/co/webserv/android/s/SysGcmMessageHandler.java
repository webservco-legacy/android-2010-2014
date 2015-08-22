package co.webserv.android.s;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class SysGcmMessageHandler extends IntentService {

	public SysGcmMessageHandler() {
		super("SysGcmMessageHandler");
	}

	@Override
	public void onCreate() {

		super.onCreate();
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		Bundle extras = intent.getExtras();

		GoogleCloudMessaging google_cloud_messaging = GoogleCloudMessaging
				.getInstance(this);

		/**
		 * The getMessageType() intent parameter must be the intent you received
		 * in your BroadcastReceiver.
		 */
		String message_type = google_cloud_messaging.getMessageType(intent);

		UtilsAndroid.logDebug(getApplicationContext(),
				"SysGcmMessageHandler: onHandleIntent: message type: "
						+ message_type);

		if (!extras.isEmpty()) {

			if (message_type
					.equals(GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR)) {

				UtilsAndroid.logDebug(getApplicationContext(),
						"SysGcmMessageHandler: onHandleIntent: send error: "
								+ extras.toString());

			} else if (message_type
					.equals(GoogleCloudMessaging.MESSAGE_TYPE_DELETED)) {

				UtilsAndroid.logDebug(getApplicationContext(),
						"SysGcmMessageHandler: onHandleIntent: deleted: "
								+ extras.toString());

			} else if (message_type
					.equals(GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE)) {

				for (int i = 0; i < 3; i++) {

					try {

						Thread.sleep(5000);

					} catch (InterruptedException e) {

						Log.e(UtilsAndroid.LOG_TAG,
								"SysFileUploader: onHandleIntent: "
										+ e.getMessage(), e);
					}
				}

				String title = extras.getString("gcm_title");
				String message = extras.getString("gcm_message");

				if (title != null && message != null) {

					UtilsAndroid.logDebug(getApplicationContext(),
							"SysGcmMessageHandler: onHandleIntent: title: "
									+ title);

					UtilsAndroid.logDebug(getApplicationContext(),
							"SysGcmMessageHandler: onHandleIntent: message: "
									+ message);

					boolean app_is_active = UtilsSharedPreferences.getBoolean(
							getApplicationContext(), "app_is_active", false);

					if (!app_is_active
							|| getApplicationContext().getString(
									R.string.gcm_also_if_not_active).length() > 0) {

						UtilsAndroid
								.logDebug(
										getApplicationContext(),
										"SysGcmMessageHandler: onHandleIntent: conditions are met, showing notification");

						UtilsAndroid.showNotification(getApplicationContext(),
								title, message);

					} else {

						UtilsAndroid
								.logDebug(
										getApplicationContext(),
										"SysGcmMessageHandler: onHandleIntent: conditions not met, discarding notification");
					}

				} else {

					UtilsAndroid
							.logDebug(getApplicationContext(),
									"SysGcmMessageHandler: onHandleIntent: title or message is empty.");
				}
			}

		} else {

			UtilsAndroid.logDebug(getApplicationContext(),
					"SysGcmMessageHandler: onHandleIntent: extras is empty.");
		}

		SysGcmBroadcastReceiver.completeWakefulIntent(intent);
	}
}