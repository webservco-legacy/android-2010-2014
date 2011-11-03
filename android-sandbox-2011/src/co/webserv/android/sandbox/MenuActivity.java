package co.webserv.android.sandbox;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;

public class MenuActivity extends Activity {

	private static final int RESULT_CLOSE_ALL = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_CLOSE_ALL:
			setResult(RESULT_CLOSE_ALL);
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.home:
		case R.id.temp:
			startActivityForResult(new Intent(MenuActivity.this,
					HomeActivity.class), RESULT_CLOSE_ALL);
			return true;
		case R.id.image_gallery:
			final ProgressDialog pd = ProgressDialog.show(this, "Loading",
					"Please wait...", true);

			final Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					pd.dismiss();
				}
			};

			Thread startActivity = new Thread() {
				@Override
				public void run() {
					startActivityForResult(new Intent(MenuActivity.this,
							ImageGallery.class), RESULT_CLOSE_ALL);
					handler.sendEmptyMessage(0);
				}
			};

			startActivity.start();

			return true;
		case R.id.gridweb:
			startActivityForResult(new Intent(MenuActivity.this,
					GridWebActivity.class), RESULT_CLOSE_ALL);
			return true;
		case R.id.grid:
			startActivityForResult(new Intent(MenuActivity.this,
					GridActivity.class), RESULT_CLOSE_ALL);
			return true;
		case R.id.sax_gallery:
			startActivityForResult(new Intent(MenuActivity.this,
					SaxGalleryActivity.class), RESULT_CLOSE_ALL);
			return true;
		case R.id.sax_example:
			startActivityForResult(new Intent(MenuActivity.this,
					SaxExampleActivity.class), RESULT_CLOSE_ALL);
			return true;
		case R.id.about:
			startActivityForResult(new Intent(MenuActivity.this,
					AboutActivity.class), RESULT_CLOSE_ALL);
			return true;

		case R.id.exit:
			setResult(RESULT_CLOSE_ALL);
			finish();
			// moveTaskToBack(true);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
/* $Id$ */