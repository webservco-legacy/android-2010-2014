package co.webserv.android.apocket;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class OperationListActivity extends ListActivity {

	private Long mPocketId;

	private OperationDbAdapter mOperationDbAdapter;

	private PocketDbAdapter mPocketDbAdapter;

	private GeneralAdapter mGeneralAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		mOperationDbAdapter = new OperationDbAdapter(this);
		mOperationDbAdapter.open();

		mGeneralAdapter = new GeneralAdapter(this);
		mGeneralAdapter.onCreate();

		setContentView(R.layout.operation_list);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			mPocketId = extras.getLong(OperationDbAdapter.msKeyPocket);
		}

		showOperations();

		registerForContextMenu(getListView());
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();

		mOperationDbAdapter.close();
		if (mPocketDbAdapter != null) {
			mPocketDbAdapter.close();
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
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderIcon(R.drawable.icon);
		menu.setHeaderTitle(R.string.operation_context);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.operation_context, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		switch (item.getItemId()) {
		case R.id.operation_context_edit:
			Intent i = new Intent(this, OperationDetailsActivity.class);
			i.putExtra(OperationDbAdapter.msKeyId, info.id);
			startActivityForResult(i, GeneralAdapter.RESULT_CLOSE_ALL);
			setResult(GeneralAdapter.RESULT_CLOSE_ALL);
			finish();
			return true;
		case R.id.operation_context_delete:
			mOperationDbAdapter.deleteOperation(info.id);
			showOperations();
			Toast.makeText(this, getString(R.string.form_success),
					Toast.LENGTH_SHORT).show();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		super.onListItemClick(l, v, position, id);

		Intent i = new Intent(this, OperationDetailsActivity.class);
		i.putExtra(OperationDbAdapter.msKeyId, id);
		startActivityForResult(i, GeneralAdapter.RESULT_CLOSE_ALL);
		setResult(GeneralAdapter.RESULT_CLOSE_ALL);
		finish();
	}

	private void showOperations() {

		List<Operation> operations = getOperationsList();

		String[] from = new String[] { Operation.msKeyPocket,
				Operation.msKeyDate, Operation.msKeyAmount,
				Operation.msKeyNotes };
		int[] to = new int[] { R.id.pocket, R.id.date, R.id.amount, R.id.notes };
		ListAdapter adapter = new OperationListAdapter(this, operations,
				R.layout.operation_list_item, from, to);
		setListAdapter(adapter);
	}

	private List<Operation> getOperationsList() {

		Cursor cursor;

		if (mPocketId != null) {
			cursor = mOperationDbAdapter.getPocketOperations(mPocketId);
		} else {
			cursor = mOperationDbAdapter.getOperations();
		}

		startManagingCursor(cursor);

		int opnum = cursor.getCount();

		List<Operation> operations = new ArrayList<Operation>();

		if (opnum > 0) {

			if (mPocketDbAdapter == null) {
				mPocketDbAdapter = new PocketDbAdapter(this);
				mPocketDbAdapter.open();
			}

			cursor.moveToFirst();

			while (cursor.isAfterLast() == false) {

				Cursor pocket = mPocketDbAdapter.getPocket(cursor.getLong(1));

				startManagingCursor(pocket);

				operations.add(new Operation(cursor.getLong(0), pocket
						.getString(1), cursor.getString(2),
						cursor.getString(3), cursor.getString(4)));

				stopManagingCursor(pocket);
				pocket.close();

				cursor.moveToNext();
			}
		}

		stopManagingCursor(cursor);
		cursor.close();

		return operations;
	}
}
/* $Id$ */