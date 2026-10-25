package co.webserv.android.wsgallery;

//import android.util.Log;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;

public class GestureListener extends SimpleOnGestureListener {
	
	@Override
	public boolean onSingleTapUp(MotionEvent ev) {
		//Log.d("onSingleTapUp", ev.toString());
		return true;
	}

	@Override
	public void onShowPress(MotionEvent ev) {
		//Log.d("onShowPress", ev.toString());
	}

	@Override
	public void onLongPress(MotionEvent ev) {
		//Log.d("onLongPress", ev.toString());
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		//Log.d("onScroll", e1.toString());
		return true;
	}

	@Override
	public boolean onDown(MotionEvent ev) {
		//Log.d("onDownd", ev.toString());
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {

		//Log.d("d", e1.toString());
		//Log.d("e2", e2.toString());

		int dx = (int) (e2.getX() - e1.getX());

		if (Math.abs(dx) > 60 && Math.abs(velocityX) > Math.abs(velocityY)) {
			if (velocityX < 0) {
				//Log.d("left_swipe", e1.toString());
			} else if (velocityX > 0) {
				//Log.d("right_swipe", e1.toString());
			}
		}

		return false;
	}
}
/* $Id$ */