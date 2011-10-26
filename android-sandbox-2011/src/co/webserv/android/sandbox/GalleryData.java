package co.webserv.android.sandbox;

public class GalleryData {

	private String[] mTitle;
	private String[] mThumb;
	private String[] mImage;

	public String[] getTitle() {
		return mTitle;
	}

	public String[] getThumb() {
		return mThumb;
	}

	public String[] getImage() {
		return mImage;
	}

	public void setTitle(int index, String newTitle) {
		mTitle[index] = newTitle;
	}

	public void setThumb(int index, String newThumb) {
		mThumb[index] = newThumb;
	}

	public void setImage(int index, String newImage) {
		mImage[index] = newImage;
	}

	public String toString() {
		return mImage.toString();
	}
}
/* $Id$ */