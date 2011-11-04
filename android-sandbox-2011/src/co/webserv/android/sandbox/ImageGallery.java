package co.webserv.android.sandbox;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageGallery extends MenuActivity implements
		AdapterView.OnItemClickListener, View.OnClickListener {

	private ViewGroup mImageContainer;
	private GridView mImageList;
	private ImageView mImageItem;
	private GalleryData mGallery = new GalleryData();

	private final ImageDownloader mImageDownloader = new ImageDownloader();

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.image_gallery);

		mImageList = (GridView) findViewById(R.id.image_list);
		mImageItem = (ImageView) findViewById(R.id.image_item);
		mImageContainer = (ViewGroup) findViewById(R.id.image_container);

		mImageList.setAdapter(new ImageAdapterGridWeb(this));
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

		setProgressBarIndeterminateVisibility(true);
	}

	public void onClick(View v) {
		applyRotation(-1, 180, 90);

	}

	public void onItemClick(AdapterView<?> parent, View v, final int position,
			long id) {

		ImageAdapterGridWeb adapter = (ImageAdapterGridWeb) parent.getAdapter();
		mGallery = adapter.getGallery();

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
}
/* $Id$ */