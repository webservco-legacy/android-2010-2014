package co.webserv.android.androidid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.text.Html;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class IdActivity extends Activity {

    private GeneralAdapter mGeneralAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        /**
         * General Adapter
         */
        mGeneralAdapter = new GeneralAdapter(this);

        /**
         * General functionality.
         */
        mGeneralAdapter.onCreate();

        /**
         * Set browser layout.
         */
        setContentView(R.layout.main);

        mGeneralAdapter.initActivity(1);

        mGeneralAdapter.mAndroidId = getAndroidId();

        mGeneralAdapter.mId.setText(mGeneralAdapter.mAndroidId);

        if (mGeneralAdapter.setClipboard("ANDROID_ID",
                mGeneralAdapter.mAndroidId)) {

            mGeneralAdapter.mInfo.setVisibility(View.VISIBLE);
            mGeneralAdapter.mInfo.setText(Html
                    .fromHtml(getString(R.string.id_info)));

            mGeneralAdapter.mSendContainer.setVisibility(View.VISIBLE);
        }

        if (mGeneralAdapter.mAndroidId.equals("9774d56d682e549c")
                || mGeneralAdapter.isEmulator()) {

            mGeneralAdapter.mWarning.setVisibility(View.VISIBLE);
            mGeneralAdapter.mWarning.setText(Html
                    .fromHtml(getString(R.string.id_warning)));
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        mGeneralAdapter.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {

        mGeneralAdapter.onDestroy();

        super.onDestroy();
    }

    @Override
    protected void onPause() {

        mGeneralAdapter.onPause();

        super.onPause();
    }

    @Override
    protected void onResume() {

        super.onResume();

        mGeneralAdapter.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            mGeneralAdapter.onBackPressed();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return mGeneralAdapter.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return mGeneralAdapter.onOptionsItemSelected(item);
    }

    public String getAndroidId() {

        return Secure.getString(getBaseContext().getContentResolver(),
                Secure.ANDROID_ID);

    }
}