package co.webserv.android.apocket;

import co.webserv.android.apocket.database.PocketDbAdapter;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;

public class PocketListActivity extends ListActivity {

	private PocketDbAdapter mDbAdapter;

	private GeneralAdapter mGeneralAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		mDbAdapter = new PocketDbAdapter(this);
		mDbAdapter.open();

		mGeneralAdapter = new GeneralAdapter(this);
		mGeneralAdapter.onCreate();

		setContentView(R.layout.pocket_list);

		showPockets();
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

	private void showPockets() {
		Cursor pockets = mDbAdapter.getPockets();
		startManagingCursor(pockets);

		String[] from = new String[] { PocketDbAdapter.msKeyTitle };
		int[] to = new int[] { R.id.label };
		SimpleCursorAdapter pocket_list = new SimpleCursorAdapter(this,
				R.layout.pocket_list_item, pockets, from, to);
		setListAdapter(pocket_list);
	}
}
