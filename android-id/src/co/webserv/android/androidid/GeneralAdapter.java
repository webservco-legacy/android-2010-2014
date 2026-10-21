package co.webserv.android.androidid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GeneralAdapter implements OnClickListener {

    protected static final String LOG_TAG = "COWEBSERVANDROID";
    protected static final int REQUEST_CODE = 10;
    protected static final int RESULT_CLOSE_ALL = 1;

    private Activity mActivity;

    /**
     * Custom app members.
     */

    protected String mAndroidId;

    protected int mSdkVersion;

    protected TextView mTitle;

    protected TextView mContent;

    protected TextView mId;

    protected TextView mInfo;

    protected RelativeLayout mSendContainer;

    protected Button mSend;

    protected TextView mWarning;

    public GeneralAdapter(Activity a) {
        mActivity = a;
    }

    protected void onCreate() {

        /**
         * Force certain language - see tutorial.
         */

        /**
         * Hide title bar.
         */
        mActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);

        /**
         * Network connection check - cache check - see tutorial.
         */

        /**
         * Network connection check - show message if no connection - see
         * tutorial.
         */

        /**
         * Custom app functionality.
         */
        mSdkVersion = getSdkVersion();

    }

    protected void initActivity(int section_id) {

        mTitle = (TextView) mActivity.findViewById(R.id.title);

        mContent = (TextView) mActivity.findViewById(R.id.content);

        switch (section_id) {

        case 5:

            break;

        case 4:

            break;

        case 3:

            mTitle.setText(R.string.section3_title);
            mContent.setText(Html.fromHtml(mActivity
                    .getString(R.string.section3_content)));
            mContent.setMovementMethod(LinkMovementMethod.getInstance());

            break;

        case 2:

            mTitle.setText(R.string.section2_title);
            mContent.setText(Html.fromHtml(mActivity
                    .getString(R.string.section2_content)));

            break;

        case 1:
        default:

            mId = (TextView) mActivity.findViewById(R.id.id);

            mInfo = (TextView) mActivity.findViewById(R.id.info);

            mSendContainer = (RelativeLayout) mActivity
                    .findViewById(R.id.send_container);

            mSend = (Button) mActivity.findViewById(R.id.send);

            mSend.setOnClickListener(this);

            mWarning = (TextView) mActivity.findViewById(R.id.warning);

            mTitle.setText(R.string.section1_title);

            mContent.setText(R.string.id_content);

            break;
        }
    }

    protected void onDestroy() {

    }

    protected void onPause() {

    }

    protected void onResume() {

    }

    protected void onBackPressed() {

        mActivity.finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (resultCode) {

        case RESULT_CLOSE_ALL:

            exit();

            break;
        }
    }

    protected boolean onCreateOptionsMenu(Menu menu) {

        mActivity.getMenuInflater().inflate(R.menu.application, menu);

        return true;
    }

    protected boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

        case R.id.menu_section1:

            mActivity.startActivityForResult(new Intent(mActivity,
                    IdActivity.class), RESULT_CLOSE_ALL);

            return true;

        case R.id.menu_section2:

            mActivity.startActivityForResult(new Intent(mActivity,
                    InfoActivity.class), RESULT_CLOSE_ALL);

            return true;

        case R.id.menu_section3:

            mActivity.startActivityForResult(new Intent(mActivity,
                    CreditsActivity.class), RESULT_CLOSE_ALL);

            return true;

        case R.id.menu_exit:

            exit();

            return true;

        default:

            return mActivity.getParent().onOptionsItemSelected(item);
        }
    }

    protected void exit() {

        mActivity.setResult(RESULT_CLOSE_ALL);
        mActivity.finish();
    }

    public void onClick(View v) {

        int button_id = v.getId();

        switch (button_id) {

        case R.id.send:

            Intent si = new Intent();

            si.setAction(Intent.ACTION_SEND);

            si.putExtra(Intent.EXTRA_SUBJECT,
                    mActivity.getString(R.string.send_subject));

            si.putExtra(Intent.EXTRA_TEXT,
                    mActivity.getString(R.string.send_text) + mAndroidId);

            si.setType("text/plain");

            mActivity.startActivity(Intent.createChooser(si,
                    mActivity.getString(R.string.send_title)));

            break;

        default:
            break;

        }
    }

    public boolean isEmulator() {

        return (Build.PRODUCT.contains("sdk"));
    }

    public int getSdkVersion() {

        return android.os.Build.VERSION.SDK_INT;
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public boolean setClipboard(String label, String text) {

        if (mSdkVersion < android.os.Build.VERSION_CODES.HONEYCOMB) {

            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) mActivity
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);

        } else {

            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) mActivity
                    .getSystemService(Context.CLIPBOARD_SERVICE);

            android.content.ClipData clip = android.content.ClipData
                    .newPlainText(label, text);

            clipboard.setPrimaryClip(clip);
        }

        return true;
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public String getClipboard() {

        if (mSdkVersion < android.os.Build.VERSION_CODES.HONEYCOMB) {

            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) mActivity
                    .getSystemService(Context.CLIPBOARD_SERVICE);

            if (clipboard.hasText()) {

                return (String) clipboard.getText();

            } else {

                return "";
            }

        } else {

            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) mActivity
                    .getSystemService(Context.CLIPBOARD_SERVICE);

            if (clipboard.hasPrimaryClip()) {

                if (clipboard.getPrimaryClipDescription().hasMimeType(
                        android.content.ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                    android.content.ClipData.Item item = clipboard
                            .getPrimaryClip().getItemAt(0);

                    return (String) item.getText();
                }
            }

            return "";
        }

    }
}