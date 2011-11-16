package co.webserv.android.apocket;

import java.util.Calendar;

import co.webserv.android.apocket.database.OperationDbAdapter;
import co.webserv.android.apocket.database.PocketDbAdapter;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class OperationDetailsActivity extends Activity {

	private Long mId;
	private Long mPocketId;

	private RelativeLayout mLayoutForm;
	private RelativeLayout mLayoutResult;

	private Spinner mType;
	private Spinner mPocket;
	private EditText mDate;
	private EditText mAmount;
	private EditText mNotes;

	private int mYear;
	private int mMonth;
	private int mDay;

	static final int DATE_DIALOG_ID = 0;

	private OperationDbAdapter mOperationDbAdapter;
	private PocketDbAdapter mPocketDbAdapter;
	private Cursor mPockets;

	private GeneralAdapter mGeneralAdapter;

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;

			setDateField();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		mOperationDbAdapter = new OperationDbAdapter(this);
		mOperationDbAdapter.open();

		mGeneralAdapter = new GeneralAdapter(this);
		mGeneralAdapter.onCreate();

		setContentView(R.layout.operation_form);

		mLayoutForm = (RelativeLayout) findViewById(R.id.operation_form);
		mLayoutResult = (RelativeLayout) findViewById(R.id.operation_result);

		mType = (Spinner) findViewById(R.id.operation_type);
		mPocket = (Spinner) findViewById(R.id.operation_pocket);
		mDate = (EditText) findViewById(R.id.operation_date);
		mAmount = (EditText) findViewById(R.id.operation_amount);
		mNotes = (EditText) findViewById(R.id.operation_notes);

		Button button = (Button) findViewById(R.id.operation_button);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {

			long id = extras.getLong(OperationDbAdapter.msKeyId);
			if (id > 0) {
				mId = id;
			}

			long pocket = extras.getLong(OperationDbAdapter.msKeyPocket);
			if (pocket > 0) {
				mPocketId = pocket;
			}
		}

		mPocketDbAdapter = new PocketDbAdapter(this);
		mPocketDbAdapter.open();

		mPockets = mPocketDbAdapter.getPockets();

		String[] from = new String[] { PocketDbAdapter.msKeyTitle };
		int[] to = new int[] { android.R.id.text1 };
		SimpleCursorAdapter pocket_list = new SimpleCursorAdapter(this,
				android.R.layout.simple_spinner_item, mPockets, from, to);
		pocket_list
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mPocket.setAdapter(pocket_list);

		mDate.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);

			}
		});

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		setDateField();

		showOperation();

		button.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				insertOperation();
			}
		});
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();

		mPockets.close();

		mOperationDbAdapter.close();
		mPocketDbAdapter.close();
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
	protected Dialog onCreateDialog(int id) {

		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}
		return null;
	}

	private void setDateField() {
		mDate.setText(new StringBuilder().append(mYear).append("-")
				.append(mMonth + 1).append("-").append(mDay).append(" "));
	}

	private void showOperation() {
		if (mId != null) {
			Cursor operation = mOperationDbAdapter.getOperation(mId);
			startManagingCursor(operation);

			if (operation != null && operation.getCount() > 0) {
				if (operation.moveToFirst()) {

					long pocket = operation
							.getLong(operation
									.getColumnIndexOrThrow(OperationDbAdapter.msKeyPocket));
					String date = operation
							.getString(operation
									.getColumnIndexOrThrow(OperationDbAdapter.msKeyDate));
					double amount = operation
							.getDouble(operation
									.getColumnIndexOrThrow(OperationDbAdapter.msKeyAmount));
					String notes = operation
							.getString(operation
									.getColumnIndexOrThrow(OperationDbAdapter.msKeyNotes));

					mType.setSelection((amount > 0) ? 1 : 0);

					for (int i = 0; i < mPocket.getCount(); i++) {
						long id = mPocket.getItemIdAtPosition(i);
						if (id == pocket) {
							mPocket.setSelection(i);
						}
					}

					mDate.setText(date);

					mAmount.setText(Math.abs(amount) + "");

					mNotes.setText(notes);

				}
			}

			stopManagingCursor(operation);
			operation.close();
		}
		if (mPocketId != null) {
			for (int i = 0; i < mPocket.getCount(); i++) {
				long id = mPocket.getItemIdAtPosition(i);
				if (id == mPocketId) {
					mPocket.setSelection(i);
				}
			}
		}
	}

	public void insertOperation() {
		String errorMessage = "";

		long type = mType.getSelectedItemId();

		long pocket = mPocket.getSelectedItemId();

		String date = mDate.getText().toString().trim();

		String amountString = mAmount.getText().toString().trim();

		Double amount = (amountString.length() > 0) ? Double
				.parseDouble(amountString) : 0;

		if (type == 0) {
			amount = 0 - amount;
		}

		String notes = mNotes.getText().toString().trim();

		if (date.length() == 0) {
			errorMessage += "\n" + getString(R.string.operation_date_error);
		}

		if (amount.doubleValue() == 0) {
			errorMessage += "\n" + getString(R.string.operation_amount_error);
		}

		if (errorMessage.length() > 0) {
			TextView errorField = (TextView) findViewById(R.id.operation_error);
			errorField.setText(errorMessage);
		} else {
			if (mId == null) {
				long id = mOperationDbAdapter.addOperation(pocket, date,
						amount, notes);
				if (id > 0) {
					mId = id;
					mLayoutForm.setVisibility(View.GONE);
					mLayoutResult.setVisibility(View.VISIBLE);
				}
			} else {
				mOperationDbAdapter.updateOperation(mId, pocket, date, amount,
						notes);
				mLayoutForm.setVisibility(View.GONE);
				mLayoutResult.setVisibility(View.VISIBLE);
			}
		}
	}
}
/* $Id$ */