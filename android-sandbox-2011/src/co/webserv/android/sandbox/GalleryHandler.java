package co.webserv.android.sandbox;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class GalleryHandler extends DefaultHandler {

	private int mIndex = 0;

	final private String mTagItem = "item";
	final private String mAttrTitle = "title";
	final private String mAttrThumb = "thumb";
	final private String mAttrImage = "image";

	private GalleryData mGalleryData = new GalleryData();

	@Override
	public void startDocument() throws SAXException {
		mGalleryData = new GalleryData();
	}

	@Override
	public void endDocument() throws SAXException {

	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (localName.equals(mTagItem)) {
			String attrTitle = attributes.getValue(mAttrTitle);
			String attrThumb = attributes.getValue(mAttrThumb);
			String attrImage = attributes.getValue(mAttrImage);

			mGalleryData.setTitle(mIndex, attrTitle);
			mGalleryData.setThumb(mIndex, attrThumb);
			mGalleryData.setImage(mIndex, attrImage);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (localName.equals(mTagItem)) {
			mIndex++;
		}

	}

}
/* $Id$ */