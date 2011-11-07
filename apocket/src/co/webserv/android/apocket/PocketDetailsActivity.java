package co.webserv.android.apocket;

import co.webserv.android.apocket.database.PocketDbAdapter;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class PocketDetailsActivity extends Activity {

	private Long mId;

	private RelativeLayout mLayoutForm;
	private RelativeLayout mLayoutResult;

	private EditText mTitle;
	private Spinner mType;
	private Spinner mCurrency;
	private EditText mDescription;

	private PocketDbAdapter mDbAdapter;

	private GeneralAdapter mGeneralAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		mDbAdapter = new PocketDbAdapter(this);
		mDbAdapter.open();

		mGeneralAdapter = new GeneralAdapter(this);
		mGeneralAdapter.onCreate();

		setContentView(R.layout.pocket_form);

		mLayoutForm = (RelativeLayout) findViewById(R.id.pocket_form);
		mLayoutResult = (RelativeLayout) findViewById(R.id.pocket_result);

		mTitle = (EditText) findViewById(R.id.pocket_title);
		mType = (Spinner) findViewById(R.id.pocket_type);
		mCurrency = (Spinner) findViewById(R.id.pocket_currency);
		mDescription = (EditText) findViewById(R.id.pocket_description);

		Button button = (Button) findViewById(R.id.pocket_button);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			mId = extras.getLong(PocketDbAdapter.msKeyId);
		}

		showPocket();

		button.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				savePocket();
			}
		});
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

	private void showPocket() {
		if (mId != null) {
			Cursor pocket = mDbAdapter.getPocket(mId);
			startManagingCursor(pocket);
			String title = pocket.getString(pocket
					.getColumnIndexOrThrow(PocketDbAdapter.msKeyTitle));
			String type = pocket.getString(pocket
					.getColumnIndexOrThrow(PocketDbAdapter.msKeyType));
			String currency = pocket.getString(pocket
					.getColumnIndexOrThrow(PocketDbAdapter.msKeyCurrency));
			String description = pocket.getString(pocket
					.getColumnIndexOrThrow(PocketDbAdapter.msKeyDescription));

			mTitle.setText(title);

			for (int i = 0; i < mType.getCount(); i++) {
				String s = (String) mType.getItemAtPosition(i);
				if (s.equalsIgnoreCase(type)) {
					mType.setSelection(i);
				}
			}

			for (int j = 0; j < mCurrency.getCount(); j++) {
				String s = (String) mCurrency.getItemAtPosition(j);
				if (s.equalsIgnoreCase(currency)) {
					mCurrency.setSelection(j);
				}
			}

			mDescription.setText(description);

			stopManagingCursor(pocket);
			pocket.close();
		}
	}

	public void savePocket() {
		String errorMessage = "";
		String title = mTitle.getText().toString().trim();
		String type = mType.getSelectedItem().toString();
		String currency = mCurrency.getSelectedItem().toString();
		String description = mDescription.getText().toString().trim();

		if (title.length() == 0) {
			errorMessage += "\n" + getString(R.string.pocket_title_error);
		}

		if (errorMessage.length() > 0) {
			TextView errorField = (TextView) findViewById(R.id.pocket_error);
			errorField.setText(errorMessage);
		} else {
			if (mId == null) {
				long id = mDbAdapter.createPocket(title, type, currency,
						description);
				if (id > 0) {
					mId = id;
					mLayoutForm.setVisibility(View.GONE);
					mLayoutResult.setVisibility(View.VISIBLE);
				}
			} else {
				mDbAdapter
						.updatePocket(mId, title, type, currency, description);
				showPocket();
				mLayoutForm.setVisibility(View.GONE);
				mLayoutResult.setVisibility(View.VISIBLE);
			}
		}
	}
}
/* $Id$ */