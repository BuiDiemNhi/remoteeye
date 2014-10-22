package remoteeyes.bsp.vn.uilt;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

public class PicUtil {

	static private LruCache mMemoryCache;

	static public void init(Context context) {
		final int memClass = ((ActivityManager) context.getSystemService(
	            Context.ACTIVITY_SERVICE)).getMemoryClass();

	    // Use 1/8th of the available memory for this memory cache.
	    final int cacheSize = 1024 * 1024 * memClass / 8;

	    mMemoryCache = new LruCache(cacheSize) {
	        
	        protected int sizeOf(String key, Bitmap bitmap) {
	            // The cache size will be measured in bytes rather than number of items.
	            return bitmap.getByteCount();
	        }
	    };
	}
	
	static public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
	    if (getBitmapFromMemCache(key) == null) {
	        mMemoryCache.put(key, bitmap);
	    }
	}

	static public Bitmap getBitmapFromMemCache(String key) {
	    return (Bitmap)mMemoryCache.get(key);
	}
}
