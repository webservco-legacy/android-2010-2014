package co.webserv.android.apocket;

import co.webserv.android.apocket.database.UserDbAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UserActivity extends Activity {

	private TextView mErrorMeessage;

	private EditText mUsername;
	private EditText mPassword;

	private UserDbAdapter mUserDbAdapter;

	private GeneralAdapter mGeneralAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		mUserDbAdapter = new UserDbAdapter(this);
		mUserDbAdapter.open();

		mGeneralAdapter = new GeneralAdapter(this);
		mGeneralAdapter.onCreate();

		setContentView(R.layout.user);

		mUsername = (EditText) findViewById(R.id.user_username);
		mPassword = (EditText) findViewById(R.id.user_password);

		mErrorMeessage = (TextView) findViewById(R.id.user_error);

		Button button = (Button) findViewById(R.id.user_button);

		button.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				loginUser();
			}
		});
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();

		mUserDbAdapter.close();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mGeneralAdapter.onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/**
		 * No menu
		 * return mGeneralAdapter.onCreateOptionsMenu(menu);
		 */
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/**
		 * No menu
		 * return mGeneralAdapter.onOptionsItemSelected(item);
		 */
		return false;
	}

	@Override
	public void onBackPressed() {
		/**
		 * Exit application if back is pressed
		 * mGeneralAdapter.onBackPressed();
		 */
		mGeneralAdapter.exit();
	}

	private void loginUser() {

		String errorMessage = "";

		String username = mUsername.getText().toString().trim();

		String password = mPassword.getText().toString().trim();

		if (username.length() == 0) {
			errorMessage += "\n" + getString(R.string.user_username_error);
		}

		if (password.length() == 0) {
			errorMessage += "\n" + getString(R.string.user_password_error);
		}

		if (errorMessage.length() > 0) {

			mErrorMeessage.setText(errorMessage);
		} else {

			long id = mUserDbAdapter.getUserId(username, password);

			if (id > 0) {
				Intent ip = new Intent(this, PocketListActivity.class);
				ip.putExtra(GeneralAdapter.msKeyUserId, id);
				startActivityForResult(ip, GeneralAdapter.RESULT_CLOSE_ALL);
				setResult(GeneralAdapter.RESULT_CLOSE_ALL);
				finish();
			} else {
				mErrorMeessage.setText(getString(R.string.user_login_error));
			}
		}
	}
}
/* $Id$ */