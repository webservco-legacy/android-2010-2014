package co.webserv.android.s;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

public class AppSplash extends Activity {

	private SysActivity mSysActivity;

	protected boolean mSplashActive;

	protected int mSplashTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		/**
		 * Custom activity.
		 */
		mSysActivity = new SysActivity(AppSplash.this);

		/**
		 * Custom functionality.
		 */
		mSysActivity.OnCreate(savedInstanceState);

		/**
		 * Set splash layout.
		 */
		setContentView(R.layout.splash);

		/**
		 * Set splash active.
		 */
		mSplashActive = true;

		/**
		 * Set splash time.
		 */
		mSplashTime = Integer.parseInt(getString(R.string.splash_time));

		/**
		 * Splash thread.
		 */
		Thread splashThread = new Thread() {

			@Override
			public void run() {

				UtilsAndroid.logDebug(AppSplash.this,
						"AppSplash: splashThread: run");

				try {

					int waited = 0;

					while (mSplashActive && (waited < mSplashTime)) {

						sleep(100);

						if (mSplashActive) {

							waited += 100;
						}
					}
				} catch (InterruptedException e) {

					mSplashActive = false;

					UtilsAndroid.logDebug(AppSplash.this,
							"AppSplash: splashThread: interrupted");

				} finally {

					finish();

					UtilsAndroid.logDebug(AppSplash.this,
							"AppSplash: splashThread: finish");

					Intent i = new Intent(AppSplash.this, AppBrowser.class);
					startActivityForResult(i, UtilsAndroid.REQUEST_CODE_SPLASH);

					synchronized (this) {
						notifyAll();
					}
				}

			}

		};

		/**
		 * Start splash thread.
		 */
		splashThread.start();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		UtilsAndroid.logDebug(AppSplash.this, "AppSplash: onTouchEvent");

		if (event.getAction() == MotionEvent.ACTION_DOWN) {

			mSplashActive = false;
		}
		return true;
	}
}