package co.webserv.android.wsgallery;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import android.widget.Gallery.LayoutParams;

public class ImageViewerSwipe extends MenuActivity implements
		ViewSwitcher.ViewFactory {

	private ImageSwitcher mSwitcher;

	private Integer[] mImages = { R.drawable.img1, R.drawable.img2,
			R.drawable.img3, R.drawable.img4, R.drawable.img5, R.drawable.img6,
			R.drawable.img7, R.drawable.img8, R.drawable.img9,
			R.drawable.img10, };

	private GestureDetector mGestureDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.viewerswipe);

		mGestureDetector = new GestureDetector(this, new GestureListener());

		mSwitcher = (ImageSwitcher) findViewById(R.id.imageswipe);

		mSwitcher.setFactory(this);
		mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_in));
		mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_out));

		/*
		 * XXX for ( int i = 0; i < 10; i++ ) {
		 * mSwitcher.setImageResource(mImages[i]); }
		 */
		mSwitcher.setImageResource(mImages[0]);
	}

	public View makeView() {
		ImageView i = new ImageView(this);
		i.setBackgroundColor(0xFF000000);
		i.setScaleType(ImageView.ScaleType.FIT_CENTER);
		i.setLayoutParams(new ImageSwitcher.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		return i;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mGestureDetector.onTouchEvent(event)) {
			Toast.makeText(getApplicationContext(), "touchdown!", Toast.LENGTH_SHORT);
			return true;
		} else {
			Toast.makeText(getApplicationContext(), "no touchdown!", Toast.LENGTH_SHORT);
			return false;
		}
	}
	
}
/* $Id$ */