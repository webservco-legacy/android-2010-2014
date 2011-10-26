package co.webserv.android.sandbox;

import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class SaxGalleryActivity extends MenuActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// setContentView(R.layout.sax);

		// TextView tv = (TextView) findViewById(R.id.saxresult);

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

			int galleryNum = galleryList.size();

			ScrollView sv = new ScrollView(this);
			LinearLayout ll = new LinearLayout(this);
			ll.setOrientation(LinearLayout.VERTICAL);
			sv.addView(ll);

			for (int i = 0; i < galleryNum; i++) {
				TextView itv = new TextView(this);
				GalleryData galleryData = galleryList.get(i);
				itv.setText(galleryData.toString());
				ll.addView(itv);
			}

			setContentView(sv);

			// tv.setText("test");

		} catch (Exception e) {
			Toast.makeText(SaxGalleryActivity.this, "Error: " + e.getMessage(),
					Toast.LENGTH_LONG).show();
			Log.e(getString(R.string.app_name), this.toString(), e);
		}
	}

}
/* $Id$ */