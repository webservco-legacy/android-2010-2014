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
			startActivity(new Intent(MenuActivity.this, GalleryBrowser.class));
			return true;
		case R.id.viewer:
			startActivity(new Intent(MenuActivity.this, ImageViewer.class));
			return true;
		case R.id.about:
			startActivity(new Intent(MenuActivity.this, About.class));
			return true;
		case R.id.exit:
			finish();
            System.exit(0);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
