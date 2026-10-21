package co.webserv.android.s;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.webkit.WebBackForwardList;
import android.webkit.WebView;

import com.ipaulpro.afilechooser.utils.FileUtils;

/**
 * SysActivity.
 * 
 * To be used in Android Activities.
 * 
 * @author radu
 * 
 */
public class SysActivity {

    private Activity mActivity;

    private SysWebBrowser mWebBrowser;

    private SysFileUploader mSysFileUploader;

    private WebView mWebView;

    public SysActivity(Activity a) {

        mActivity = a;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        UtilsAndroid.logDebug(mActivity,
                "SysActivity: onActivityResult: requestCode: " + requestCode);

        UtilsAndroid.logDebug(mActivity,
                "SysActivity: onActivityResult: resultCode: " + resultCode);

        switch (requestCode) {

        case UtilsAndroid.REQUEST_CODE_FILEUPLOAD_INIT:

            UtilsAndroid
                    .logDebug(mActivity,
                            "SysActivity; onActivityResult: REQUEST_CODE_FILEUPLOAD_INIT");

            handleFileUploadInit(resultCode, data);

            break;

        default:

            switch (resultCode) {

            case UtilsAndroid.RESULT_CODE_EXIT:

                UtilsAndroid.logDebug(mActivity,
                        "SysActivity: onActivityResult: RESULT_CODE_EXIT");

                mActivity.setResult(UtilsAndroid.RESULT_CODE_EXIT);
                mActivity.finish();

                break;
            }

            break;
        }

        if (mActivity.getString(R.string.set_use_facebook_native_sdk).length() > 0) {

            SysFacebook.onActivityResult(requestCode, resultCode, data);
        }

    }

    protected void OnCreate(Bundle savedInstanceState) {

        UtilsAndroid.logDebug(mActivity, "SysActivity: OnCreate");

        Thread.setDefaultUncaughtExceptionHandler(new SysUncaughtExceptionHandler(
                mActivity.getApplicationContext()));

        /**
         * Enable to activate strict mode. if
         * (mActivity.getString(R.string.devel).length() > 0) {
         * 
         * StrictMode.enableDefaults(); }
         */

        /**
         * Placeholder: force certain language (todo tutorial).
         */

        /**
         * Placeholder: network connection check - cache check (todo tutorial).
         */

        /**
         * Placeholder: network connection check - show message if no connection
         * (todo tutorial).
         */
        
        /**
         * Hide title bar
         */
        mActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (mActivity.getString(R.string.set_use_facebook_native_sdk).length() > 0) {

            SysFacebook.onCreate(savedInstanceState, mActivity);
        }

    }

    protected void OnDestroy() {

        UtilsAndroid.logDebug(mActivity, "SysActivity: OnDestroy");

        if (mWebBrowser instanceof SysWebBrowser) {
            mWebBrowser.onDestroy();
        }
    }

    protected void onPause() {

        UtilsAndroid.logDebug(mActivity, "SysActivity: onPause");

        UtilsSharedPreferences.putBoolean(mActivity, "app_is_active", false);

        if (mWebBrowser instanceof SysWebBrowser) {

            mWebBrowser.onPause();
        }
    }

    protected void onResume() {

        UtilsAndroid.logDebug(mActivity, "SysActivity: onResume");

        UtilsSharedPreferences.putBoolean(mActivity, "app_is_active", true);

        if (mWebBrowser instanceof SysWebBrowser) {

            mWebBrowser.onResume();
        }
    }

    protected void onSaveInstanceState(Bundle outState) {

    }

    protected void onBackPressed() {

        UtilsAndroid.logDebug(mActivity, "SysActivity: onBackPressed");

        /**
         * Check if we are on browser activity or not.
         */
        if (mWebView instanceof WebView) {

            WebBackForwardList history = mWebView.copyBackForwardList();

            String url = history.getCurrentItem().getUrl();

            if (url.startsWith("file:///android_asset/webview_error")) {

                UtilsAndroid
                        .logDebug(mActivity,
                                "SysActivity: onBackPressed: custom error page, close browser activity.");

                mActivity.finish();

            } else {

                /**
                 * Check if we have browser history.
                 */
                if (mWebView.canGoBack()) {

                    UtilsAndroid
                            .logDebug(mActivity,
                                    "SysActivity: onBackPressed: have browser history, go back.");

                    /**
                     * Go to the previous page.
                     */
                    mWebView.goBack();

                } else {

                    UtilsAndroid
                            .logDebug(mActivity,
                                    "SysActivity: onBackPressed: no browser history, close browser activity.");

                    /**
                     * Browser activity but no history to navigate. Close
                     * current activity.
                     */
                    mActivity.finish();
                }
            }

        } else {

            UtilsAndroid
                    .logDebug(mActivity,
                            "SysActivity: onBackPressed: not browser, close current activity.");

            /**
             * Not in browser activity. Simply close current activity.
             */
            mActivity.finish();
        }
    }

    protected void webBrowserInit() {

        UtilsAndroid.logDebug(mActivity, "SysActivity: webBrowserInit");

        mWebView = (WebView) mActivity.findViewById(R.id.browserWindow);

        if (mWebBrowser == null) {

            mWebBrowser = new SysWebBrowser(mActivity);
        }
    }

    protected void webBrowserLoadUrl(String url) {

        UtilsAndroid.logDebug(mActivity, "SysActivity: webBrowserLoadUrl: "
                + url);

        if (mWebBrowser instanceof SysWebBrowser) {

            mWebBrowser.loadUrl(mWebView, url);
        }
    }

    protected void handleFileUploadInit(int resultCode, Intent data) {

        try {
            Uri selected_file_uri = data == null
                    || resultCode != Activity.RESULT_OK ? null : data.getData();

            if (selected_file_uri == null) {

                UtilsAndroid
                        .logDebug(
                                mActivity,
                                "SysActivity: handleFileUploadInit: selected_file_uri empty, checking camera capture file uri");

                Uri camera_file_uri = UtilsAndroid
                        .getAppCameraCaptureFileUri(mActivity);

                UtilsAndroid.logDebug(mActivity,
                        "SysActivity: handleFileUploadInit: camera file uri: "
                                + camera_file_uri);

                File camera_file = new File(camera_file_uri.getPath());

                if (camera_file.exists()) {

                    UtilsAndroid
                            .logDebug(
                                    mActivity,
                                    "SysActivity: handleFileUploadInit: camera file path exists, using as selected_file_uri");

                    selected_file_uri = camera_file_uri;

                    UtilsSharedPreferences.putBoolean(mActivity,
                            "isCameraCapture", true);
                } else {

                    UtilsAndroid
                            .logDebug(mActivity,
                                    "SysActivity: handleFileUploadInit: camera file path doesn't exist.");
                }

            }

            if (selected_file_uri != null) {

                UtilsAndroid.logDebug(mActivity,
                        "SysActivity: handleFileUploadInit: selected_file_uri: "
                                + selected_file_uri);

                String selected_file_path = FileUtils.getPath(mActivity,
                        selected_file_uri);

                UtilsAndroid.logDebug(mActivity,
                        "SysActivity: handleFileUploadInit: selected_file_path: "
                                + selected_file_path);

                if (selected_file_path.length() > 0
                        && FileUtils.isLocal(selected_file_path)) {

                    File upload_file = new File(selected_file_path);

                    String task_type = UtilsSharedPreferences.getString(
                            mActivity, "taskType", null);

                    if (task_type != null) {

                        UtilsAndroid.logDebug(mActivity,
                                "SysActivity: handleFileUploadInit: task_type: "
                                        + task_type);

                        String task_asset = UtilsSharedPreferences.getString(
                                mActivity, "taskAsset", null);

                        UtilsAndroid.logDebug(mActivity,
                                "SysActivity: handleFileUploadInit: task_asset: "
                                        + task_asset);

                        String upload_url = mActivity
                                .getString(R.string.app_url)
                                + "droid_upload/"
                                + task_type;

                        if (task_asset != null) {

                            upload_url = upload_url.concat("/" + task_asset);
                        }

                        UtilsAndroid.logDebug(mActivity,
                                "SysActivity: handleFileUploadInit: upload_url: "
                                        + upload_url);

                        UtilsSharedPreferences.remove(mActivity, "taskType");
                        UtilsSharedPreferences.remove(mActivity, "taskAsset");

                        mSysFileUploader = new SysFileUploader(mActivity,
                                upload_url, upload_file);
                        mSysFileUploader.mSysAsyncResponse = (SysAsyncResponse) mActivity;
                        mSysFileUploader.setUseProgressDialog(true);
                        mSysFileUploader.execute();

                    } else {

                        UtilsAndroid
                                .logDebug(mActivity,
                                        "SysActivity: handleFileUploadInit: no task type in shared preferences");
                    }

                } else {

                    UtilsAndroid
                            .logDebug(mActivity,
                                    "SysActivity: handleFileUploadInit: no file selected or not local file");

                }

            } else {

                UtilsAndroid
                        .logDebug(mActivity,
                                "SysActivity: handleFileUploadInit: file select canceled");
            }

        } catch (Exception e) {

            Log.e(UtilsAndroid.LOG_TAG, "SysActivity: handleFileUploadInit: "
                    + e.getMessage(), e);
        }
    }

    protected void handleFileUploadResult(String uploadResultCode) {

        UtilsAndroid.logDebug(mActivity,
                "SysActivity: handleFileUploadResult: uploadResultCode: "
                        + uploadResultCode);

        if (mWebBrowser instanceof SysWebBrowser) {

            int result_code = Integer.parseInt(uploadResultCode);

            String url = mActivity.getString(R.string.app_url)
                    + "droid_result/" + result_code;

            UtilsAndroid.logDebug(mActivity,
                    "SysActivity: handleFileUploadResult: url: " + url);

            boolean is_camera_capture = UtilsSharedPreferences.getBoolean(
                    mActivity, "isCameraCapture", false);

            if (is_camera_capture) {

                UtilsAndroid
                        .logDebug(mActivity,
                                "SysActivity: handleFileUploadResult: file is camera capture.");

                Uri camera_file_uri = UtilsAndroid
                        .getAppCameraCaptureFileUri(mActivity);

                File camera_file = new File(camera_file_uri.getPath());

                if (camera_file.exists()) {

                    UtilsAndroid
                            .logDebug(mActivity,
                                    "SysActivity: handleFileUploadResult: deleting camera capture.");

                    camera_file.delete();

                } else {

                    UtilsAndroid
                            .logDebug(mActivity,
                                    "SysActivity: handleFileUploadResult: camera capture file doesn't exist.");
                }

                UtilsSharedPreferences.remove(mActivity, "isCameraCapture");
            }

            if (mActivity.getString(R.string.set_upload_result_load_url)
                    .length() > 0) {

                /**
                 * Load result URL.
                 */
                webBrowserLoadUrl(url);
            }

            if (mActivity.getString(
                    R.string.set_upload_result_show_notification).length() > 0) {

                /**
                 * Show notification.
                 */
                UtilsAndroid.showNotification(
                        mActivity.getApplicationContext(),
                        mActivity.getString(R.string.app_name),
                        mActivity.getString(R.string.file_upload_complete));
            }

        } else {

            UtilsAndroid
                    .logDebug(mActivity,
                            "SysActivity: handleFileUploadResult: mWebBrowser not initialized");
        }
    }
}