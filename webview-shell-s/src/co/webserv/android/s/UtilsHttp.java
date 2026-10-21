package co.webserv.android.s;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.util.Base64;
import android.util.Log;
import android.webkit.CookieManager;

public class UtilsHttp {

    public static String getCookieFromAppCookieManager(String url)
            throws MalformedURLException {

        CookieManager cookieManager = CookieManager.getInstance();

        if (cookieManager == null) {

            return null;
        }

        String rawCookieHeader = null;

        URL parsedURL = new URL(url);

        // Extract Set-Cookie header value from app CookieManager for this URL
        rawCookieHeader = cookieManager.getCookie(parsedURL.getHost());

        if (rawCookieHeader == null) {

            return null;
        }

        return rawCookieHeader;
    }

    public static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        StringBuilder sb = new StringBuilder();

        String line = null;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {

            Log.e(UtilsAndroid.LOG_TAG, "UtilsHttp: convertStreamToString: "
                    + e.getMessage(), e);

        } finally {

            try {
                is.close();

            } catch (IOException e) {

                Log.e(UtilsAndroid.LOG_TAG,
                        "UtilsHttp: convertStreamToString: " + e.getMessage(),
                        e);
            }
        }

        return sb.toString();
    }

    public static String getStream(String uri, boolean useCookie,
            String httpd_auth_user, String httpd_auth_password, final Activity a) {

        try {

            InputStream input_stream = null;

            URL url = new URL(uri);

            DefaultHttpClient http_client = new DefaultHttpClient();

            HttpUriRequest request = new HttpGet(url.toString());

            if (a.getString(R.string.set_use_ua_version).length() > 0) {

                String user_agent_string = UtilsApp.getCustomUserAgentString(a);

                request.setHeader("User-Agent", user_agent_string);
            }

            if (useCookie) {

                request.setHeader("Cookie",
                        getCookieFromAppCookieManager(url.toString()));
            }

            if (httpd_auth_user.length() > 0) {

                byte[] auth = (httpd_auth_user + ":" + httpd_auth_password)
                        .getBytes();
                String basic = Base64.encodeToString(auth, Base64.NO_WRAP);
                request.setHeader("Authorization", "Basic " + basic);
            }

            HttpResponse response = http_client.execute(request);

            HttpEntity entity = response.getEntity();

            if (entity != null) {

                input_stream = entity.getContent();

                Header content_encoding = response
                        .getFirstHeader("Content-Encoding");

                if (content_encoding != null
                        && content_encoding.getValue().equalsIgnoreCase("gzip")) {
                    input_stream = new GZIPInputStream(input_stream);
                }

                return convertStreamToString(input_stream).trim();

            } else {

                Log.e(UtilsAndroid.LOG_TAG,
                        "UtilsHttp: getHttpStream: Error getting http entity");
            }

        } catch (MalformedURLException e) {

            Log.e(UtilsAndroid.LOG_TAG,
                    "UtilsHttp: getHttpStream: " + e.getMessage(), e);

        } catch (IOException e) {

            Log.e(UtilsAndroid.LOG_TAG,
                    "UtilsHttp: getHttpStream: " + e.getMessage(), e);
        }

        return "";
    }
}