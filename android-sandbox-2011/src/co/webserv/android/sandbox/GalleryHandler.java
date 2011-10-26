package co.webserv.android.sandbox;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class GalleryHandler extends DefaultHandler {

	private int mGallery = 0;
	private int mItem = 0;

	final private String mTagGallery = "gallery";
	final private String mTagItem = "item";
	final private String mAttrName = "name";
	final private String mAttrTitle = "title";
	final private String mAttrThumb = "thumb";
	final private String mAttrImage = "image";

	private GalleryData mGalleryData = new GalleryData();

	private ArrayList<GalleryData> mGalleryList = new ArrayList<GalleryData>();

	public ArrayList<GalleryData> getGalleryList() {
		return mGalleryList;
	}

	@Override
	public void startDocument() throws SAXException {

	}

	@Override
	public void endDocument() throws SAXException {

	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (localName.equals(mTagGallery)) {
			mGalleryData = new GalleryData();

			String attrName = attributes.getValue(mAttrName);

			Log.d("wsAndroid", "gallery: " + mGallery + "; name: " + attrName);

			mGalleryData.setName(attrName);

		} else if (localName.equals(mTagItem)) {
			String attrTitle = attributes.getValue(mAttrTitle);
			String attrThumb = attributes.getValue(mAttrThumb);
			String attrImage = attributes.getValue(mAttrImage);

			Log.d("wsAndroid", "index: " + mItem + "; title: " + attrTitle);
			Log.d("wsAndroid", "index: " + mItem + "; thumb: " + attrThumb);
			Log.d("wsAndroid", "index: " + mItem + "; image: " + attrImage);

			mGalleryData.setTitle(mItem, attrTitle);
			mGalleryData.setThumb(mItem, attrThumb);
			mGalleryData.setImage(mItem, attrImage);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (localName.equals(mTagGallery)) {

			mGalleryList.add(mGallery, mGalleryData);

			mItem = 0;
			mGallery++;
		} else if (localName.equals(mTagItem)) {
			mItem++;
		}

	}
}
/* $Id$ */