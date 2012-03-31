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

public class ImportActivity extends Activity {
	
	GeneralAdapter mGeneralAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		mGeneralAdapter = new GeneralAdapter(this);
		mGeneralAdapter.onCreate();
		
		setContentView(R.layout.main);
		
		TextView v = (TextView) findViewById(R.id.content);

		v.append("\n\n");
		v.append("Import database:");
		v.append("\n\n");

		if (Environment.getExternalStorageState().equals("mounted")) {

			String source_path = Environment.getExternalStorageDirectory()
					.toString()
					+ "/Download/"
					+ getString(R.string.app_name).toLowerCase();
			
			String destination_path = Environment.getDataDirectory().toString()
					+ "/data/" + getPackageName() + "/databases/"
					+ getString(R.string.app_name).toLowerCase();

			File source = new File(source_path);
			File destination = new File(destination_path);

			if (source.exists() && source.canRead()) {
				try {
					mGeneralAdapter.copyFile(source, destination);
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
	
	@Override
	public void onBackPressed() {
		mGeneralAdapter.onBackPressed();
	}
}
/* $Id$ */