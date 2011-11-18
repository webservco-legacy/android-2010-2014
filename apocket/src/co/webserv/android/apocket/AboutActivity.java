package co.webserv.android.apocket;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class AboutActivity extends Activity {

	private GeneralAdapter mGeneralAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		mGeneralAdapter = new GeneralAdapter(this);
		mGeneralAdapter.onCreate();

		setContentView(R.layout.main);

		TextView v = (TextView) findViewById(R.id.content);

		v.append("\n\n");
		v.append("Copy database:");
		v.append("\n\n");

		if (Environment.getExternalStorageState().equals("mounted")) {

			String source_path = Environment.getDataDirectory().toString()
					+ "/data/" + getPackageName() + "/databases/"
					+ getString(R.string.app_name).toLowerCase();

			String destination_path = Environment.getExternalStorageDirectory()
					.toString()
					+ "/download/"
					+ getString(R.string.app_name).toLowerCase();

			File source = new File(source_path);
			File destination = new File(destination_path);

			if (source.exists() && source.canRead()) {
				try {
					copyFile(source, destination);
					v.append("success!");
				} catch (IOException e) {
					Log.d("COWEBSERV", e.getMessage());
					v.append("error: copy error!");

				}
			} else {
				v.append("error: source not found!");
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

	public void copyFile(File source, File destination) throws IOException {

		InputStream input = new FileInputStream(source);
		OutputStream output = new FileOutputStream(destination);

		byte[] buffer = new byte[1024];

		int length;

		while ((length = input.read(buffer)) > 0) {
			output.write(buffer, 0, length);
		}
		input.close();
		output.close();

	}
}
/* $Id$ */