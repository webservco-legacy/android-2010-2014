package co.webserv.android.wsgallery;

import co.webserv.android.wsgallery.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;

    private Integer[] mThumbs = { R.drawable.img1_t, R.drawable.img2_t,
            R.drawable.img3_t, R.drawable.img4_t, R.drawable.img5_t,
            R.drawable.img6_t, R.drawable.img7_t, R.drawable.img8_t,
            R.drawable.img9_t, R.drawable.img10_t, };

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbs.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView i = new ImageView(mContext);

        i.setImageResource(mThumbs[position]);

        i.setAdjustViewBounds(true);
        i.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        i.setBackgroundResource(R.drawable.picture_frame);
        return i;
    }
}
/* $Id$ */