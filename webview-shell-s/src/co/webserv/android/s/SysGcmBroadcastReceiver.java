package co.webserv.android.s;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class SysGcmBroadcastReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        /**
         * Explicitly specify that GcmMessageHandler will handle the intent.
         */
        ComponentName component_name = new ComponentName(
                context.getPackageName(), SysGcmMessageHandler.class.getName());

        /**
         * Start the service, keeping the device awake while it is launching.
         */
        startWakefulService(context, (intent.setComponent(component_name)));

        setResultCode(Activity.RESULT_OK);
    }
}