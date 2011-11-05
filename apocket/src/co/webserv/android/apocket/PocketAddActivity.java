package co.webserv.android.apocket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class PocketAddActivity extends MenuActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.pocket_add);
	}
	
	public void addPocket(View button) {
		final EditText pocketTitleField = (EditText) findViewById(R.id.pocket_add_form_title);
		String pocketTitle = pocketTitleField.getText().toString();
		final Spinner pocketTypeField = (Spinner) findViewById(R.id.pocket_add_form_type);
		String pocketType = pocketTypeField.getSelectedItem().toString();
		final Spinner pocketCurrencyField = (Spinner) findViewById(R.id.pocket_add_form_currency);
		String pocketCurrency = pocketCurrencyField.getSelectedItem().toString();
		
		Log.d("APOCKET", "ADD POCKET");
		Log.d("APOCKET", "title: " + pocketTitle);
		Log.d("APOCKET", "type: " + pocketType);
		Log.d("APOCKET", "currency: " + pocketCurrency);
		
		boolean error = false;
		String errorMessage = "";
		
		if ( pocketTitle.length() == 0 ) {
			error = true;
			errorMessage += "\n" + getString(R.string.pocket_title_error);
		}
		
		if ( error ) {
			TextView errorField = (TextView) findViewById(R.id.pocket_add_form_error);
			errorField.setText(errorMessage);
		} else {
			// add to database
			
			startActivityForResult(new Intent(PocketAddActivity.this,
					HomeActivity.class), RESULT_CLOSE_ALL);
		}
	}
}
/* $Id$ */