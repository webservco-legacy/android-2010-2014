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

public class DatabaseBackupActivity extends Activity {

	GeneralAdapter mGeneralAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		mGeneralAdapter = new GeneralAdapter(this);
		if (!mGeneralAdapter.onCreate()) {
			return;
		}

		setContentView(R.layout.main);

		TextView content = (TextView) findViewById(R.id.content);

		content.setText("Export database:");
		content.append("\n\n");

		if (Environment.getExternalStorageState().equals("mounted")) {

			String path_storage = Environment.getExternalStorageDirectory()
					.toString()
					+ "/Android/data/"
					+ getPackageName()
					+ "/backups";
			String db_name = getString(R.string.app_name).toLowerCase();
			String path_destination = path_storage + "/" + db_name;
			String path_source = Environment.getDataDirectory().toString()
					+ "/data/" + getPackageName() + "/databases/" + db_name;

			File source = new File(path_source);
			File destination = new File(path_destination);

			if (source.exists() && source.canRead()) {
				try {

					File backup_dir = new File(path_storage);

					boolean copy = false;

					if (!backup_dir.exists()) {

						copy = backup_dir.mkdirs();
					} else {
						copy = true;
					}

					if (!copy) {
						if (GeneralAdapter.LOG) {
							Log.e(GeneralAdapter.TAG,
									"Error creating directory " + path_storage);
						}

						content.append("error: error while creating directory!");
					} else {
						mGeneralAdapter.copyFile(source, destination);
						content.append("success!");
						content.append("\n\nDatabase backed up to\n"
								+ path_destination);
					}
				} catch (IOException e) {
					if (GeneralAdapter.LOG) {
						Log.e(GeneralAdapter.TAG, e.getMessage());
					}
					content.append("error: copy error!");

				}
			} else {
				content.append("error: source not found:\n" + path_source);
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