package co.webserv.android.wsgallery;

import co.webserv.android.wsgallery.R;

import co.webserv.android.wsgallery.ImageAdapter;

import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Gallery;
//import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
//import android.widget.Toast;
import android.widget.ViewSwitcher;

public class GalleryBrowser extends MenuActivity implements
		AdapterView.OnItemSelectedListener, ViewSwitcher.ViewFactory {

	private ImageSwitcher mSwitcher;

	private Integer[] mImages = { R.drawable.img1, R.drawable.img2,
			R.drawable.img3, R.drawable.img4, R.drawable.img5, R.drawable.img6,
			R.drawable.img7, R.drawable.img8, R.drawable.img9,
			R.drawable.img10, };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.browser);

		mSwitcher = (ImageSwitcher) findViewById(R.id.switcher);

		mSwitcher.setFactory(this);
		mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_in));
		mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_out));

		Gallery g = (Gallery) findViewById(R.id.gallery);
		g.setAdapter(new ImageAdapter(this));
		g.setOnItemSelectedListener(this);

		/*
		 * g.setOnItemClickListener(new OnItemClickListener() {
		 * 
		 * @SuppressWarnings("rawtypes") public void onItemClick(AdapterView
		 * parent, View v, int position, long id) {
		 * Toast.makeText(GalleryBrowser.this, "image caption " + position,
		 * Toast.LENGTH_SHORT).show(); } });
		 */
	}

	@SuppressWarnings("rawtypes")
	public void onItemSelected(AdapterView parent, View v, int position, long id) {
		mSwitcher.setImageResource(mImages[position]);
	}

	@SuppressWarnings("rawtypes")
	public void onNothingSelected(AdapterView parent) {
	}

	public View makeView() {
		ImageView i = new ImageView(this);
		i.setBackgroundColor(0xFF000000);
		i.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		i.setLayoutParams(new ImageSwitcher.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		return i;
	}
}

/* $Id$ */