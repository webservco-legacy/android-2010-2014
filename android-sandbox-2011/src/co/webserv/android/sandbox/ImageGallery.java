package co.webserv.android.sandbox;

import android.os.Bundle;
import android.view.Window;
import android.widget.GridView;

public class ImageGallery extends MenuActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.image_gallery);

		setProgressBarIndeterminateVisibility(true);

		GridView gridview = (GridView) findViewById(R.id.image_list);

		gridview.setAdapter(new ImageAdapterGridWeb(this));
	}
}
/* $Id$ */