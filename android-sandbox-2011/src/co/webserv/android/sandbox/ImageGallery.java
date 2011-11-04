package co.webserv.android.sandbox;

import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageGallery extends MenuActivity implements
		AdapterView.OnItemClickListener, View.OnClickListener {

	private ViewGroup mImageContainer;
	private GridView mImageList;
	private ImageView mImageItem;
	private GalleryData mGallery;

	private final ImageDownloader mImageDownloader = new ImageDownloader();

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.image_gallery);

		mImageList = (GridView) findViewById(R.id.image_list);
		mImageItem = (ImageView) findViewById(R.id.image_item);
		mImageContainer = (ViewGroup) findViewById(R.id.image_container);

		mGallery = getGallery();

		mImageList.setAdapter(new ImageAdapterGridWeb(this, mGallery));
		mImageList.setOnItemClickListener(this);

		mImageItem.setClickable(true);
		mImageItem.setFocusable(true);
		mImageItem.setOnClickListener(this);

		mImageItem.setAdjustViewBounds(true);

		mImageItem.setLayoutParams(new FrameLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mImageContainer
				.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);

		ImageDownloader.Mode mode = ImageDownloader.Mode.CORRECT;

		mImageDownloader.setMode(mode);
	}

	public void onClick(View v) {
		applyRotation(-1, 180, 90);

	}

	public void onItemClick(AdapterView<?> parent, View v, final int position,
			long id) {

		final ArrayList<String> galleryImages = mGallery.getImage();

		mImageDownloader.download(galleryImages.get(position), mImageItem);

		applyRotation(position, 0, 90);
	}

	public void applyRotation(int position, float start, float end) {

		final float centerX = mImageContainer.getWidth() / 2.0f;
		final float centerY = mImageContainer.getHeight() / 2.0f;

		final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end,
				centerX, centerY, 310.0f, true);

		rotation.setDuration(500);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new AccelerateInterpolator());
		rotation.setAnimationListener(new DisplayNextView(position));

		mImageContainer.startAnimation(rotation);
	}

	private final class DisplayNextView implements Animation.AnimationListener {

		private final int mPosition;

		private DisplayNextView(int position) {
			mPosition = position;
		}

		public void onAnimationStart(Animation animation) {
		}

		public void onAnimationEnd(Animation animation) {
			mImageContainer.post(new SwapViews(mPosition));
		}

		public void onAnimationRepeat(Animation animation) {
		}
	}

	private final class SwapViews implements Runnable {

		private final int mPosition;

		private SwapViews(int position) {
			mPosition = position;
		}

		public void run() {

			final float centerX = mImageContainer.getWidth() / 2.0f;
			final float centerY = mImageContainer.getHeight() / 2.0f;

			Rotate3dAnimation rotation;

			if (mPosition > -1) {
				mImageList.setVisibility(View.GONE);
				mImageItem.setVisibility(View.VISIBLE);
				mImageItem.requestFocus();
				rotation = new Rotate3dAnimation(90, 180, centerX, centerY,
						310.0f, false);
			} else {
				mImageItem.setVisibility(View.GONE);
				mImageList.setVisibility(View.VISIBLE);
				mImageList.requestFocus();
				rotation = new Rotate3dAnimation(90, 0, centerX, centerY,
						310.0f, false);
			}

			rotation.setDuration(500);
			rotation.setFillAfter(true);
			rotation.setInterpolator(new DecelerateInterpolator());

			mImageContainer.startAnimation(rotation);
		}
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