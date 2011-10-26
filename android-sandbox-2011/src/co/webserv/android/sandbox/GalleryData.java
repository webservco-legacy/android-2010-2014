package co.webserv.android.sandbox;

import java.util.ArrayList;

public class GalleryData {

	private String mName;

	private ArrayList<String> mTitle = new ArrayList<String>();
	private ArrayList<String> mThumb = new ArrayList<String>();
	private ArrayList<String> mImage = new ArrayList<String>();

	public String getName() {
		return mName;
	}

	public ArrayList<String> getTitle() {
		return mTitle;
	}

	public ArrayList<String> getThumb() {
		return mThumb;
	}

	public ArrayList<String> getImage() {
		return mImage;
	}

	public void setName(String newName) {
		mName = newName;
	}

	public void setTitle(int index, String newTitle) {
		mTitle.add(index, newTitle);
	}

	public void setThumb(int index, String newThumb) {
		mThumb.add(index, newThumb);
	}

	public void setImage(int index, String newImage) {
		mImage.add(index, newImage);
	}

	public String toString() {
		return mName + ": " + mImage.size() + " images, " + mThumb.size() + " thumbs, " + mTitle.size() + " titles";
	}
}
/* $Id$ */