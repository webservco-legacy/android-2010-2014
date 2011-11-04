package co.webserv.android.sandbox;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapterGridWeb extends BaseAdapter {

	private Context mContext;

	private GalleryData mGallery = new GalleryData();

	private final ImageDownloader mImageDownloader = new ImageDownloader();

	public ImageAdapterGridWeb(Context c, GalleryData g) {
		mContext = c;
		mGallery = g;
	}

	public int getCount() {
		ArrayList<String> galleryThumbs = mGallery.getThumb();
		return galleryThumbs.size();
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
			imageView.setPadding(3, 3, 3, 3);

		} else {
			imageView = (ImageView) convertView;
		}

		ArrayList<String> galleryThumbs = mGallery.getThumb();

		ImageDownloader.Mode mode = ImageDownloader.Mode.CORRECT;

		mImageDownloader.setMode(mode);
		mImageDownloader.download(galleryThumbs.get(position), imageView);

		return imageView;
	}
}
/* $Id$ */