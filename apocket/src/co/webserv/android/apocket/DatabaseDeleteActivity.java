package co.webserv.android.apocket;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class DatabaseDeleteActivity extends Activity {

	GeneralAdapter mGeneralAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		mGeneralAdapter = new GeneralAdapter(this);
		mGeneralAdapter.onCreate();

		setContentView(R.layout.main);

		TextView v = (TextView) findViewById(R.id.content);

		v.setText("Delete database:");
		v.append("\n\n");

		try {
			deleteDatabase(getString(R.string.app_name).toLowerCase());
			v.append("success!");
			v.append("\n\nPLEASE RESTART APPLICATION.");
		} catch (Exception e) {
			Log.d(GeneralAdapter.TAG, e.getMessage());
			v.append("error: copy error!");
		}

	}
}
