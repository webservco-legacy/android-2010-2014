package co.webserv.android.s;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Environment;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebBackForwardList;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class SysWebBrowser {

    private Activity mActivity;

    private WebView mWebView;

    private WebSettings mWebSettings;

    public SysWebBrowser(Activity a) {

        mActivity = a;
    }

    public Activity getActivity() {

        UtilsAndroid.logDebug(mActivity, "SysWebBrowser: getActivity");

        return mActivity;
    }

    public void onDestroy() {

        UtilsAndroid.logDebug(mActivity, "SysWebBrowser: onDestroy");

    }

    public void onPause() {

        UtilsAndroid.logDebug(mActivity, "SysWebBrowser: onPause");

        UtilsAndroid.logDebug(mActivity,
                "SysWebBrowser: CookieSyncManager stopSync");

        CookieSyncManager.getInstance().stopSync();
    }

    public void onResume() {

        UtilsAndroid.logDebug(mActivity, "SysWebBrowser: onResume");

        UtilsAndroid.logDebug(mActivity, "SysWebBrowser: cookies init");

        CookieSyncManager.createInstance(mActivity);

        CookieManager.getInstance().setAcceptCookie(true);

        UtilsAndroid.logDebug(mActivity,
                "SysWebBrowser: CookieSyncManager startSync");

        CookieSyncManager.getInstance().startSync();
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void loadUrl(WebView wv, String url) {

        UtilsAndroid.logDebug(mActivity, "SysWebBrowser: loadUrl: url:" + url);

        mWebView = wv;

        mWebSettings = mWebView.getSettings();

        /**
         * Specifically enable JavaScript
         */
        mWebSettings.setJavaScriptEnabled(true);

        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        /**
         * Enable GPS Location.
         */
        if (mActivity.getString(R.string.set_use_geolocation).length() > 0) {

            UtilsAndroid.logDebug(mActivity,
                    "SysWebBrowser: loadUrl: using geolocation");

            mWebSettings.setGeolocationEnabled(true);
        }

        if (mActivity.getString(R.string.set_use_cache).length() > 0) {

            UtilsAndroid.logDebug(mActivity,
                    "SysWebBrowser: loadUrl: using cache");

            mWebSettings.setDomStorageEnabled(true);

            mWebSettings.setDatabaseEnabled(true);

            mWebSettings.setAppCachePath(Environment.getDataDirectory()
                    .toString()
                    + "/data/"
                    + mActivity.getPackageName()
                    + "/cache");

            mWebSettings.setAllowFileAccess(true);

            mWebSettings.setAppCacheEnabled(true);
        }

        if (mActivity.getString(R.string.set_use_offline).length() > 0) {

            UtilsAndroid.logDebug(mActivity,
                    "SysWebBrowser: loadUrl: using offline");

            mWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        /**
         * Form data handling.
         */
        mWebSettings.setSaveFormData(false);

        /**
         * Scroll bars.
         */
        mWebView.setVerticalScrollBarEnabled(false);

        mWebView.setHorizontalScrollBarEnabled(false);

        /**
         * Custom WebView Client.
         */
        mWebView.setWebViewClient(new SysWebViewClient(this));

        /**
         * Custom WebChrome Client.
         */
        mWebView.setWebChromeClient(new SysWebChromeClient(mActivity));

        if (mActivity.getString(R.string.set_use_js).length() > 0) {

            UtilsAndroid.logDebug(mActivity,
                    "SysWebBrowser: loadUrl: using javascript interface");

            mWebView.addJavascriptInterface(new SysJavaScriptInterface(
                    mActivity, mWebView), "androidInterface");
        }

        if (mActivity.getString(R.string.set_use_ua_version).length() > 0) {

            String user_agent_string = UtilsApp.getCustomUserAgentString(
                    mActivity, mWebSettings.getUserAgentString());

            mWebSettings.setUserAgentString(user_agent_string);
        }

        /**
         * Load URL
         */
        mWebView.loadUrl(url);

        if (mActivity.getString(R.string.set_use_gads).length() > 0) {

            UtilsAndroid.logDebug(mActivity, "loadUrl: google ads enabled.");

            SysGoogleAds gads = new SysGoogleAds(mActivity);

            gads.interstitialLoadDisplay();
        }
    }

    public static void loadLastAppUrl(WebView wv, Activity a, String url_append) {

        String app_url = a.getString(R.string.app_url);

        WebBackForwardList history = wv.copyBackForwardList();

        int i;
        String next_url = null;

        for (i = 1; i < history.getSize(); i++) {

            next_url = history.getItemAtIndex(history.getCurrentIndex() - i)
                    .getUrl();

            if (next_url.startsWith(app_url)) {

                break;
            }
        }

        wv.loadUrl(next_url + url_append);
    }
}