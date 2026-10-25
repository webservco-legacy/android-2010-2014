package co.webserv.android.tutorial.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmMessageHandler extends IntentService {

    public static final int NOTIFICATION_ID = 1;

    private static NotificationManager mNotificationManager;

    private SharedPreferences mSharedPreferences;

    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        Log.i("GCM",
                "Received : (" + messageType + ")  "
                        + extras.getString("title"));

        if (!extras.isEmpty()) {

            Context context = getApplicationContext();

            mSharedPreferences = context.getSharedPreferences(
                    context.getPackageName(), ActionBarActivity.MODE_PRIVATE);

            boolean is_active = mSharedPreferences.getBoolean("active", false);

            if (!is_active) {

                if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
                        .equals(messageType)) {

                    showNotification(context,
                            "Send error: " + extras.toString());

                } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                        .equals(messageType)) {

                    showNotification(context, "Deleted messages on server: "
                            + extras.toString());

                } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                        .equals(messageType)) {

                    for (int i = 0; i < 3; i++) {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                        }
                    }

                    showNotification(context,
                            "Message Received from Google GCM Server: "
                                    + extras.get("gcm_title"));
                }

            }

        }

        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    public static void showNotification(Context context, String msg) {

        mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);

        Uri alarmSound = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.drawable.ic_launcher)
                .setTicker("Notification!")
                .setContentTitle(context.getString(R.string.app_name))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg).setSound(alarmSound)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

        mBuilder.setContentIntent(contentIntent);

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}