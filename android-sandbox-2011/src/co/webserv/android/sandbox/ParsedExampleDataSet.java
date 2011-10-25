package co.webserv.android.sandbox;

public class ParsedExampleDataSet {

	private String mExtractedString = null;
	private int mExtractedInt = 0;

	public String getExtractedString() {
		return mExtractedString;
	}

	public void setExtractedString(String extractedString) {
		mExtractedString = extractedString;
	}

	public int getExtractedInt() {
		return mExtractedInt;
	}

	public void setExtractedInt(int extractedInt) {
		mExtractedInt = extractedInt;
	}

	public String toString() {
		return mExtractedString.trim() + "\n" + mExtractedInt;
	}
}
/* $Id$ */