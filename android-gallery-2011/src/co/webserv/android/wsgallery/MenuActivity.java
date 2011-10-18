package co.webserv.android.wsgallery;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public abstract class MenuActivity extends Activity {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.gallery:
			finish();
			startActivity(new Intent(MenuActivity.this, GalleryBrowser.class));
			return true;
		case R.id.viewer:
			finish();
			startActivity(new Intent(MenuActivity.this, ImageViewerButtons.class));
			return true;
		case R.id.about:
			finish();
			startActivity(new Intent(MenuActivity.this, About.class));
			return true;
		case R.id.exit:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
