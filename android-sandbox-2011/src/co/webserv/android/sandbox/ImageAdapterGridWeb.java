package co.webserv.android.sandbox;

import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageAdapterGridWeb extends BaseAdapter {

	private Context mContext;

	private GalleryData mGallery = new GalleryData();

	private final ImageDownloader mImageDownloader = new ImageDownloader();

	public ImageAdapterGridWeb(Context c) {
		mContext = c;
		mGallery = getGallery();
	}

	public GalleryData getGallery() {

		GalleryData gallery = new GalleryData();

		try {

			String galleryUrl = mContext.getString(R.string.app_url) + "site/"
					+ mContext.getString(R.string.app_id)
					+ "/gallery/gallery.xml";
			URL url = new URL(galleryUrl);

			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();

			XMLReader xr = sp.getXMLReader();

			GalleryHandler gh = new GalleryHandler();
			xr.setContentHandler(gh);

			xr.parse(new InputSource(url.openStream()));

			ArrayList<GalleryData> galleryList = gh.getGalleryList();

			gallery = galleryList.get(0);

		} catch (Exception e) {
			Toast.makeText(mContext, "Error: " + e.getMessage(),
					Toast.LENGTH_LONG).show();
		}

		return gallery;
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