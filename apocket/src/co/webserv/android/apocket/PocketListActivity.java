package co.webserv.android.apocket;

import co.webserv.android.apocket.database.OperationDbAdapter;
import co.webserv.android.apocket.database.PocketDbAdapter;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class PocketListActivity extends ListActivity {

	private PocketDbAdapter mPocketDbAdapter;

	private GeneralAdapter mGeneralAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		mPocketDbAdapter = new PocketDbAdapter(this);
		mPocketDbAdapter.open();

		mGeneralAdapter = new GeneralAdapter(this);
		mGeneralAdapter.onCreate();

		setContentView(R.layout.pocket_list);

		showPockets();

		registerForContextMenu(getListView());
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
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderIcon(R.drawable.icon);
		menu.setHeaderTitle(R.string.pocket_context);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.pocket_context, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.pocket_context_edit:
			Intent i = new Intent(this, PocketDetailsActivity.class);
			i.putExtra(PocketDbAdapter.msKeyId, info.id);
			startActivityForResult(i, 1);
			return true;
		case R.id.pocket_context_delete:
			mPocketDbAdapter.deletePocket(info.id);
			showPockets();
			Toast.makeText(this, getString(R.string.form_success),
					Toast.LENGTH_SHORT).show();
			return true;
		case R.id.pocket_context_operation_add:
			Intent i1 = new Intent(this, OperationDetailsActivity.class);
			i1.putExtra(OperationDbAdapter.msKeyPocket, info.id);
			startActivityForResult(i1, 1);
			return true;
		case R.id.pocket_context_operation_list:
			Intent i2 = new Intent(this, OperationListActivity.class);
			i2.putExtra(OperationDbAdapter.msKeyPocket, info.id);
			startActivityForResult(i2, 1);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private void showPockets() {
		Cursor pockets = mPocketDbAdapter.getPockets();

		startManagingCursor(pockets);

		String[] from = new String[] { PocketDbAdapter.msKeyTitle };
		int[] to = new int[] { R.id.label };
		SimpleCursorAdapter pocket_list = new SimpleCursorAdapter(this,
				R.layout.pocket_list_item, pockets, from, to);
		setListAdapter(pocket_list);

		stopManagingCursor(pockets);
	}
}
/* $Id$ */