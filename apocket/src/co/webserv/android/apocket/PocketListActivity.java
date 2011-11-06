package co.webserv.android.apocket;

import co.webserv.android.apocket.database.PocketDbAdapter;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

public class PocketListActivity extends ListActivity {

	private PocketDbAdapter mDbAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		mDbAdapter = new PocketDbAdapter(this);
		mDbAdapter.open();

		setContentView(R.layout.pocket_list);
		
		showPockets();

	}

	private void showPockets() {
		Cursor pockets = mDbAdapter.getPockets();
		startManagingCursor(pockets);

		String[] from = new String[] { PocketDbAdapter.msKeyTitle };
		int[] to = new int[] {R.id.label};
		SimpleCursorAdapter pocket_list = new SimpleCursorAdapter(this,
				R.layout.pocket_list_item, pockets, from, to);
		setListAdapter(pocket_list);
	}
}
