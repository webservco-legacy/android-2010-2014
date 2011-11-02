package co.webserv.android.sandbox;

import android.os.Bundle;
import android.widget.GridView;

public class GridWebActivity extends MenuActivity {

	//Context mContext = this;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.gridview);

		final GridView gridview = (GridView) findViewById(R.id.gridview);

		/* 
		final ProgressDialog pd = ProgressDialog.show(this, "Loading",
				"Please wait...", true);
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				pd.dismiss();
			}
		};

		Thread startActivity = new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				gridview.setAdapter(new ImageAdapterGridWeb(mContext));
				handler.sendEmptyMessage(0);
			}
		};

		startActivity.start();

		*/

		gridview.setAdapter(new ImageAdapterGridWeb(this));
	}
}
/* $Id$ */