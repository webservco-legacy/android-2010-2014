package co.webserv.android.sandbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;

public class MenuActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
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
			startActivity(new Intent(MenuActivity.this, HomeActivity.class));
			return true;
		case R.id.about:
			startActivity(new Intent(MenuActivity.this, AboutActivity.class));
			return true;
		case R.id.sax:
			startActivity(new Intent(MenuActivity.this, SaxActivity.class));
			return true;
		case R.id.exit:
			moveTaskToBack(true);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
/* $Id$ */