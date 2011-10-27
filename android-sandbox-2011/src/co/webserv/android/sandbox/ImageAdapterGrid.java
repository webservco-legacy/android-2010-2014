package co.webserv.android.sandbox;

import android.content.Context;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapterGrid extends BaseAdapter {

	private Context mContext;

	private Integer[] mThumbIds = { R.drawable.img1_t, R.drawable.img2_t,
			R.drawable.img3_t, R.drawable.img4_t, R.drawable.img5_t,
			R.drawable.img6_t, R.drawable.img7_t, R.drawable.img8_t,
			R.drawable.img9_t, R.drawable.img10_t, };

	public ImageAdapterGrid(Context c) {
		mContext = c;
	}

	public int getCount() {
		return mThumbIds.length;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;

		if (convertView == null) {

			imageView = new ImageView(mContext);

			imageView.setAdjustViewBounds(true);
			imageView.setLayoutParams(new GridView.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			imageView.setPadding(0, 0, 0, 0);
		} else {
			imageView = (ImageView) convertView;
		}

		imageView.setImageResource(mThumbIds[position]);

		return imageView;
	}
}
/* $Id$ */