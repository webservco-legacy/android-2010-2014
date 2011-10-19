package co.webserv.android.wsgallery;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

public class GalleryBrowserWeb extends MenuActivity implements
		AdapterView.OnItemSelectedListener, ViewSwitcher.ViewFactory {

	private ImageSwitcher mSwitcher;

	private String[] mImageUrls = {
			"http://147.123.157.186/election/site/obama/img/img1.jpg",
			"http://147.123.157.186/election/site/obama/img/img2.jpg",
			"http://147.123.157.186/election/site/obama/img/img3.jpg",
			"http://147.123.157.186/election/site/obama/img/img4.jpg",
			"http://147.123.157.186/election/site/obama/img/img5.jpg",
			"http://147.123.157.186/election/site/obama/img/img6.jpg",
			"http://147.123.157.186/election/site/obama/img/img7.jpg",
			"http://147.123.157.186/election/site/obama/img/img8.jpg",
			"http://147.123.157.186/election/site/obama/img/img9.jpg",
			"http://147.123.157.186/election/site/obama/img/img10.jpg",

	};

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
	}

	@SuppressWarnings("rawtypes")
	public void onItemSelected(AdapterView parent, View v, int position, long id) {

		try {
			URL imgURL = new URL(mImageUrls[position]);
			URLConnection con = imgURL.openConnection();
			con.connect();
			InputStream is = con.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			Bitmap bm = BitmapFactory.decodeStream(bis);
			bis.close();
			is.close();
			Drawable bmd = new BitmapDrawable(bm);
			mSwitcher.setImageDrawable(bmd);
		} catch (MalformedURLException e) {
			mSwitcher.setImageResource(R.drawable.network_error);
			Log.e("DEBUGTAG", "Remote Image Exception / MalformedURLException",
					e);
		} catch (IOException e) {
			mSwitcher.setImageResource(R.drawable.network_error);
			Log.e("DEBUGTAG", "Remote Image Exception / IOException", e);
		}
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