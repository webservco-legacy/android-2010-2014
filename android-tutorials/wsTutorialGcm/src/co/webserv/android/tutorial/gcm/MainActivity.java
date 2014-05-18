package co.webserv.android.tutorial.gcm;

import java.io.IOException;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends ActionBarActivity implements OnClickListener {

	protected static final String LOG_TAG = "COWEBSERVANDROID";

	protected static final String PROJECT_NUMBER = "791595523283";

	GoogleCloudMessaging mGoogleCloudMessaging;

	String mRegId;

	String mRegMsg;

	Button mRegIdButton;

	EditText mRegIdEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mRegIdButton = (Button) findViewById(R.id.regIdButton);

		mRegIdEditText = (EditText) findViewById(R.id.regIdEditText);

		mRegIdButton.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {

		//testMethod(v);
		
		getRegistrationId();

	}

	public void testMethod(View v) {

		Log.i("COWEBSERVANDROID", "click click click");

		mRegIdEditText.setText("button click");
	}

	public void getRegistrationId() {

		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {

				try {

					if (mGoogleCloudMessaging == null) {

						mGoogleCloudMessaging = GoogleCloudMessaging
								.getInstance(getApplicationContext());
					}

					mRegId = mGoogleCloudMessaging.register(PROJECT_NUMBER);

					mRegMsg = "Success: " + mRegId;

				} catch (IOException ex) {

					mRegMsg = "Error: " + ex.getMessage();

				}

				Log.i(LOG_TAG, mRegMsg);

				return mRegMsg;
			}

			protected void onPostExecute(String result) {

				mRegIdEditText.setText(result);
			};

		}.execute(null, null, null);
	}

}