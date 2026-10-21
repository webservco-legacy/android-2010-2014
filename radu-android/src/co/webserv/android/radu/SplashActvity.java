package co.webserv.android.radu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.widget.TextView;

public class SplashActvity extends Activity {

    private GeneralAdapter mGeneralAdapter;

    TextView title_lb;
    TextView footermsg;
    WebView browser;

    protected boolean _active = true;
    protected int _splashTime = 5000;
    private static final int REQUEST_CODE = 10;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mGeneralAdapter = new GeneralAdapter(this);
        mGeneralAdapter.mCheckConnection = false;
        mGeneralAdapter.onCreate();

        setContentView(R.layout.splash);

        Thread splashThread = new Thread() {

            @Override
            public void run() {

                try {
                    int waited = 0;
                    while (_active && (waited < _splashTime)) {
                        sleep(100);
                        if (_active) {
                            waited += 100;
                        }
                    }
                } catch (InterruptedException e) {
                    _active = false;
                } finally {
                    finish();

                    Intent i = new Intent(SplashActvity.this,
                            BrowserActivity.class);
                    i.putExtra("main", true);
                    startActivityForResult(i, REQUEST_CODE);
                    synchronized (this) {
                        notifyAll();
                    }
                }

            }

        };
        splashThread.start();
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            _active = false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return mGeneralAdapter.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mGeneralAdapter.onOptionsItemSelected(item);
    }

    /**
     * Splash crashes on orientation change, so we disable this.
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        mGeneralAdapter.onConfigurationChanged(newConfig);
        setContentView(R.layout.splash);
    }
    */

    @Override
    public void onBackPressed() {
        mGeneralAdapter.onBackPressed();
    }

}
/* $Id$ */