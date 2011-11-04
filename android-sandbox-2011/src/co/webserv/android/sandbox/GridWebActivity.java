package co.webserv.android.sandbox;

import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.os.Bundle;
import android.view.Window;
import android.widget.GridView;
import android.widget.Toast;

public class GridWebActivity extends MenuActivity {

	private GalleryData mGallery;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.gridview);

		setProgressBarIndeterminateVisibility(true);

		GridView gridview = (GridView) findViewById(R.id.gridview);

		mGallery = getGallery();

		gridview.setAdapter(new ImageAdapterGridWeb(this, mGallery));
	}

	public GalleryData getGallery() {

		if (mGallery != null) {
			return mGallery;
		}

		GalleryData gallery = new GalleryData();

		try {

			String galleryUrl = getString(R.string.app_url) + "site/"
					+ getString(R.string.app_id) + "/gallery/gallery.xml";
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
			Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}

		return gallery;
	}
}
/* $Id$ */