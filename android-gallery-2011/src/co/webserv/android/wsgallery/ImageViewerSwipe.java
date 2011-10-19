package co.webserv.android.wsgallery;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ViewFlipper;

public class ImageViewerSwipe extends MenuActivity {

	ViewFlipper flipper;

	// RelativeLayout[] l;
	// ImageView[] v;

	RelativeLayout l0, l1, l2, l3, l4, l5, l6, l7, l8, l9;
	ImageView v0, v1, v2, v3, v4, v5, v6, v7, v8, v9;

	private Integer[] mImages = { R.drawable.img1, R.drawable.img2,
			R.drawable.img3, R.drawable.img4, R.drawable.img5, R.drawable.img6,
			R.drawable.img7, R.drawable.img8, R.drawable.img9,
			R.drawable.img10, };

	private GestureDetector mGestureDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		RelativeLayout.LayoutParams params = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		flipper = new ViewFlipper(this);

		l0 = new RelativeLayout(this);
		l1 = new RelativeLayout(this);
		l2 = new RelativeLayout(this);
		l3 = new RelativeLayout(this);
		l4 = new RelativeLayout(this);
		l5 = new RelativeLayout(this);
		l6 = new RelativeLayout(this);
		l7 = new RelativeLayout(this);
		l8 = new RelativeLayout(this);
		l9 = new RelativeLayout(this);

		v0 = new ImageView(this);
		v1 = new ImageView(this);
		v2 = new ImageView(this);
		v3 = new ImageView(this);
		v4 = new ImageView(this);
		v5 = new ImageView(this);
		v6 = new ImageView(this);
		v7 = new ImageView(this);
		v8 = new ImageView(this);
		v9 = new ImageView(this);

		v0.setBackgroundColor(0xFF000000);
		v0.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		v0.setLayoutParams(params);
		v0.setImageResource(mImages[0]);
		v1.setBackgroundColor(0xFF000000);
		v1.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		v1.setLayoutParams(params);
		v1.setImageResource(mImages[1]);
		v2.setBackgroundColor(0xFF000000);
		v2.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		v2.setLayoutParams(params);
		v2.setImageResource(mImages[2]);
		v3.setBackgroundColor(0xFF000000);
		v3.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		v3.setLayoutParams(params);
		v3.setImageResource(mImages[3]);
		v4.setBackgroundColor(0xFF000000);
		v4.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		v4.setLayoutParams(params);
		v4.setImageResource(mImages[4]);
		v5.setBackgroundColor(0xFF000000);
		v5.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		v5.setLayoutParams(params);
		v5.setImageResource(mImages[5]);
		v6.setBackgroundColor(0xFF000000);
		v6.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		v6.setLayoutParams(params);
		v6.setImageResource(mImages[6]);
		v7.setBackgroundColor(0xFF000000);
		v7.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		v7.setLayoutParams(params);
		v7.setImageResource(mImages[7]);
		v8.setBackgroundColor(0xFF000000);
		v8.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		v8.setLayoutParams(params);
		v8.setImageResource(mImages[8]);
		v9.setBackgroundColor(0xFF000000);
		v9.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		v9.setLayoutParams(params);
		v9.setImageResource(mImages[9]);

		l0.addView(v0, params);
		l1.addView(v1, params);
		l2.addView(v2, params);
		l3.addView(v3, params);
		l4.addView(v4, params);
		l5.addView(v5, params);
		l6.addView(v6, params);
		l7.addView(v7, params);
		l8.addView(v8, params);
		l9.addView(v9, params);

		flipper.addView(l0);
		flipper.addView(l1);
		flipper.addView(l2);
		flipper.addView(l3);
		flipper.addView(l4);
		flipper.addView(l5);
		flipper.addView(l6);
		flipper.addView(l7);
		flipper.addView(l8);
		flipper.addView(l9);

		setContentView(flipper);

		mGestureDetector = new GestureDetector(this, new GestureListener());

		flipper.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				if (mGestureDetector.onTouchEvent(event)) {
					return true;
				} else {
					return false;
				}
			}
		});

	}

	private Animation inFromRightAnimation() {
		Animation inFromRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromRight.setDuration(500);
		inFromRight.setInterpolator(new AccelerateInterpolator());
		return inFromRight;
	}

	private Animation outToLeftAnimation() {
		Animation outtoLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		outtoLeft.setDuration(500);
		outtoLeft.setInterpolator(new AccelerateInterpolator());
		return outtoLeft;
	}

	private Animation inFromLeftAnimation() {
		Animation inFromLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromLeft.setDuration(500);
		inFromLeft.setInterpolator(new AccelerateInterpolator());
		return inFromLeft;
	}

	private Animation outToRightAnimation() {
		Animation outtoRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		outtoRight.setDuration(500);
		outtoRight.setInterpolator(new AccelerateInterpolator());
		return outtoRight;
	}

	public class GestureListener extends SimpleOnGestureListener {

		@Override
		public boolean onSingleTapUp(MotionEvent ev) {
			// Log.d("onSingleTapUp", ev.toString());
			return true;
		}

		@Override
		public void onShowPress(MotionEvent ev) {
			// Log.d("onShowPress", ev.toString());
		}

		@Override
		public void onLongPress(MotionEvent ev) {
			// Log.d("onLongPress", ev.toString());
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// Log.d("onScroll", e1.toString());
			return true;
		}

		@Override
		public boolean onDown(MotionEvent ev) {
			// Log.d("onDownd", ev.toString());
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {

			// Log.d("d", e1.toString());
			// Log.d("e2", e2.toString());

			int dx = (int) (e2.getX() - e1.getX());

			if (Math.abs(dx) > 60 && Math.abs(velocityX) > Math.abs(velocityY)) {
				if (velocityX < 0) { //left swipe
					flipper.setInAnimation(inFromRightAnimation());
					flipper.setOutAnimation(outToLeftAnimation());
					flipper.showNext();
					// Log.d("left_swipe", e1.toString());
				} else if (velocityX > 0) { //right swipe
					flipper.setInAnimation(inFromLeftAnimation());
					flipper.setOutAnimation(outToRightAnimation());
					flipper.showPrevious();
					// Log.d("right_swipe", e1.toString());
				}
			}

			return super.onFling(e1, e2, velocityX, velocityY);
		}
	}

}
/* $Id$ */