package co.webserv.android.sandbox;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ExampleHandler extends DefaultHandler {

    protected boolean mInOutertag = false;
    protected boolean mInInnertag = false;
    protected boolean mInMytag = false;
    
    StringBuilder mExtractedString = new StringBuilder();

    private ParsedExampleDataSet mParsedExampleDataSet = new ParsedExampleDataSet();

    public ParsedExampleDataSet getParsedData() {
        return mParsedExampleDataSet;
    }

    @Override
    public void startDocument() throws SAXException {
        mParsedExampleDataSet = new ParsedExampleDataSet();
    }

    @Override
    public void endDocument() throws SAXException {
        // Nothing to do
    }

    @Override
    public void startElement(String namespaceURI, String localName,
            String qName, Attributes atts) throws SAXException {
        if (localName.equals("outertag")) {
            mInOutertag = true;
        } else if (localName.equals("innertag")) {
            mInInnertag = true;
        } else if (localName.equals("mytag")) {
            mInMytag = true;
        } else if (localName.equals("tagwithnumber")) {
            // Extract an Attribute
            String attrValue = atts.getValue("thenumber");
            int i = Integer.parseInt(attrValue);
            mParsedExampleDataSet.setExtractedInt(i);
        }
    }

    @Override
    public void endElement(String namespaceURI, String localName, String qName)
            throws SAXException {
        if (localName.equals("outertag")) {
            mInOutertag = false;
        } else if (localName.equals("innertag")) {
            mInInnertag = false;
        } else if (localName.equals("mytag")) {
            mParsedExampleDataSet.setExtractedString(mExtractedString.toString());
            mInMytag = false;
            mExtractedString.setLength(0);
        } else if (localName.equals("tagwithnumber")) {
            // Nothing to do here
        }
    }

    @Override
    public void characters(char ch[], int start, int length) {
        if (mInMytag) {
            mExtractedString.append(ch, start, length);
            //mParsedExampleDataSet.setExtractedString(new String(ch, start, length));
        }
    }
}
/* $Id$ */