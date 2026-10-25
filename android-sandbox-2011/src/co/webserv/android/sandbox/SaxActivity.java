package co.webserv.android.sandbox;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class SaxActivity extends MenuActivity {

	private final static String msUrl = "http://dev.rudisoft.net/25779f8829ab7a7650e85a4cc871e6ac/gallery.xml";

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.sax);

		TextView tv = (TextView) findViewById(R.id.saxresult);

		try {
			URL url = new URL(msUrl);

			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();

			XMLReader xr = sp.getXMLReader();

			ExampleHandler myExampleHandler = new ExampleHandler();
			xr.setContentHandler(myExampleHandler);

			xr.parse(new InputSource(url.openStream()));

			ParsedExampleDataSet parsedExampleDataSet = myExampleHandler
					.getParsedData();

			tv.setText(parsedExampleDataSet.toString());

		} catch (Exception e) {
			Toast.makeText(SaxActivity.this, "Error: " + e.getMessage(),
					Toast.LENGTH_SHORT).show();
			Log.e(getString(R.string.app_name), this.toString(), e);
		}
	}

}
/* $Id$ */