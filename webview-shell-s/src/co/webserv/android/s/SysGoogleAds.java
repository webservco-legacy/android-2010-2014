package co.webserv.android.s;

import java.util.Locale;

import android.app.Activity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.InterstitialAd;

public class SysGoogleAds {

	private Activity mActivity;

	private InterstitialAd mInterstitialAd;

	public SysGoogleAds(Activity a) {

		mActivity = a;
	}

	public void interstitialLoadDisplay() {

		mInterstitialAd = new InterstitialAd(mActivity);

		UtilsAndroid.logDebug(mActivity,
				"SysGoogleAds: interstitialLoad: setting ad_unit_id");

		mInterstitialAd.setAdUnitId(mActivity
				.getString(R.string.gads_id_interstitial));

		Builder builder = new AdRequest.Builder();

		if (mActivity.getString(R.string.devel).length() > 0) {

			UtilsAndroid
					.logDebug(mActivity,
							"SysGoogleAds: interstitialLoad: Debug mode: set device id: emulator");

			builder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);

			String[] gads_test_devices = mActivity.getResources()
					.getStringArray(R.array.gads_test_devices);

			for (String gads_test_device : gads_test_devices) {

				String gads_test_device_hash = UtilsString.getMd5Hash(
						gads_test_device).toUpperCase(Locale.getDefault());

				UtilsAndroid.logDebug(mActivity,
						"SysGoogleAds: interstitialLoad: Debug mode: set device id: "
								+ gads_test_device_hash);

				builder.addTestDevice(gads_test_device_hash);
			}
		}

		/**
		 * Add targeting here.
		 */

		AdRequest ad_request = builder.build();

		UtilsAndroid.logDebug(mActivity,
				"SysGoogleAds: interstitialLoad: loadAd");

		mInterstitialAd.loadAd(ad_request);

		UtilsAndroid.logDebug(mActivity,
				"SysGoogleAds: interstitialLoad: setAdListener");

		mInterstitialAd.setAdListener(new AdListener() {

			@Override
			public void onAdLoaded() {

				UtilsAndroid
						.logDebug(mActivity,
								"SysGoogleAds: interstitialLoad: AdListener: onAdLoaded");

				interstitialDisplay();

				super.onAdLoaded();
			}
		});
	}

	public void interstitialDisplay() {

		if (mInterstitialAd instanceof InterstitialAd) {

			if (mInterstitialAd.isLoaded()) {

				UtilsAndroid.logDebug(mActivity,
						"SysGoogleAds: interstitialDisplay: Show add");

				mInterstitialAd.show();

			} else {

				UtilsAndroid
						.logDebug(mActivity,
								"SysGoogleAds: interstitialDisplay: Error: Ad not loaded");
			}

		} else {

			UtilsAndroid
					.logDebug(mActivity,
							"SysGoogleAds: interstitialDisplay: Error: Interstitial instance missing.");
		}
	}
}