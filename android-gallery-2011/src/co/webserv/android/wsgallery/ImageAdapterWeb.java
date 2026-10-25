package co.webserv.android.wsgallery;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import co.webserv.android.wsgallery.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;

public class ImageAdapterWeb extends BaseAdapter {

    private Context mContext;

    /*
     * private Integer[] mThumbs = { R.drawable.img1_t, R.drawable.img2_t,
     * R.drawable.img3_t, R.drawable.img4_t, R.drawable.img5_t,
     * R.drawable.img6_t, R.drawable.img7_t, R.drawable.img8_t,
     * R.drawable.img9_t, R.drawable.img10_t, };
     */

    private String[] mThumbUrls = {
            "http://147.123.157.186/election/site/obama/img/img1_t.jpg",
            "http://147.123.157.186/election/site/obama/img/img2_t.jpg",
            "http://147.123.157.186/election/site/obama/img/img3_t.jpg",
            "http://147.123.157.186/election/site/obama/img/img4_t.jpg",
            "http://147.123.157.186/election/site/obama/img/img5_t.jpg",
            "http://147.123.157.186/election/site/obama/img/img6_t.jpg",
            "http://147.123.157.186/election/site/obama/img/img7_t.jpg",
            "http://147.123.157.186/election/site/obama/img/img8_t.jpg",
            "http://147.123.157.186/election/site/obama/img/img9_t.jpg",
            "http://147.123.157.186/election/site/obama/img/img10_t.jpg", };

    public ImageAdapterWeb(Context c) {
        mContext = c;
    }

    public int getCount() {
        // return mThumbs.length;
        return mThumbUrls.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView i = new ImageView(mContext);

        try {
            URL imgURL = new URL(mThumbUrls[position]);
            URLConnection con = imgURL.openConnection();
            con.connect();
            InputStream is = con.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            Bitmap bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
            i.setImageBitmap(bm);

        } catch (MalformedURLException e) {
            i.setImageResource(R.drawable.network_error_t);
            Log.e("DEBUGTAG", "Remote Image Exception / MalformedURLException",
                    e);
        } catch (IOException e) {
            i.setImageResource(R.drawable.network_error_t);
            Log.e("DEBUGTAG", "Remote Image Exception / IOException", e);
        }

        // i.setImageResource(mThumbs[position]);

        i.setAdjustViewBounds(true);
        i.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        i.setBackgroundResource(R.drawable.picture_frame);
        return i;
    }
}
/* $Id$ */