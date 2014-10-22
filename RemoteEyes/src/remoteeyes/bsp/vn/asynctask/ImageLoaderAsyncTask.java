package remoteeyes.bsp.vn.asynctask;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.squareup.picasso.Picasso;

import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.uilt.MemoryCache;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class ImageLoaderAsyncTask extends AsyncTask<String, Void, Void> {

	ImageView ivAvatar;
	Bitmap bitmap;
	int sizeHeight;
	boolean isSaveCache;
	InputStream input;
	URL url;

	public ImageLoaderAsyncTask(ImageView iv, int sizeHeight,
			boolean isSaveCache) {
		this.ivAvatar = iv;
		this.sizeHeight = sizeHeight;
		this.isSaveCache = isSaveCache;
	}

	@Override
	protected Void doInBackground(String... params) {
		url = null;
		try {
			if (params[0] != null) {
				url = new URL(params[0].toString());
			}
			String urldisplay = params[0];
			try {
				input = new java.net.URL(urldisplay).openStream();
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 2;
				bitmap = BitmapFactory.decodeStream(input, null, options);
			} catch (OutOfMemoryError e) {
				System.gc();
				e.printStackTrace();
				if (bitmap != null && bitmap.isRecycled()) {
					bitmap.recycle();
					// bitmap = null;
				}
			}

		} catch (MalformedURLException e) {
		} catch (IOException ex) {
		}

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		try {
			// bitmap = decodeFile(in, 100, 100);
			ivAvatar.getLayoutParams().width = (int) (sizeHeight
					* bitmap.getWidth() / bitmap.getHeight());
			ivAvatar.getLayoutParams().height = sizeHeight;
			ivAvatar.setImageBitmap(bitmap);

			if (isSaveCache) {
				MemoryCache memoryCache = new MemoryCache();
				memoryCache.put(Config.IdUser, bitmap);
			}
		} catch (Exception ex) {
		}
	}

	public static Bitmap decodeFile(InputStream f, int reqWidth, int reqHeight) {
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(f, null, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeStream(f, null, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}
}
