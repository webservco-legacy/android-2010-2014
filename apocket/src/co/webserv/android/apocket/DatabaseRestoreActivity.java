package co.webserv.android.apocket;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DatabaseRestoreActivity extends Activity {

	GeneralAdapter mGeneralAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		mGeneralAdapter = new GeneralAdapter(this);
		mGeneralAdapter.onCreate();

		setContentView(R.layout.main);

		TextView content = (TextView) findViewById(R.id.content);

		content.setText("Import database:");
		content.append("\n\n");

		if (Environment.getExternalStorageState().equals("mounted")) {

			String path_storage = Environment.getExternalStorageDirectory()
					.toString();
			String db_name = getString(R.string.app_name).toLowerCase();
			String path_source = path_storage + "/" + db_name;
			String path_destination = Environment.getDataDirectory().toString()
					+ "/data/" + getPackageName() + "/databases/" + db_name;

			File source = new File(path_source);
			File destination = new File(path_destination);

			if (source.exists() && source.canRead()) {
				try {
					mGeneralAdapter.copyFile(source, destination);
					content.append("success!");
					content.append("\n\nDatabase restored from\n" + path_source);
				} catch (IOException e) {
					Log.d(GeneralAdapter.TAG, e.getMessage());
					content.append("error: copy error!");

				}
			} else {
				content.append("error: source " + path_source + " not found!");
			}

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mGeneralAdapter.onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return mGeneralAdapter.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return mGeneralAdapter.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		mGeneralAdapter.onBackPressed();
	}
}
/* $Id$ */