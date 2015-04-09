package com.tieto.systemmanagement.authority.utilities;

import android.graphics.drawable.BitmapDrawable;
import android.support.v4.util.LruCache;

import java.util.Objects;

/**
 * @author Jiang Ping
 */
public class MemCache {
	private int mCapacity = 3 * 1024 * 1024; // 3MB Memory bitmap cache
	private LruCache<Object, BitmapDrawable> mCache;
    private final Object mLock = new Object();

	public MemCache(int capacity) {
		if (capacity > 0) {
			mCapacity = capacity;
		}
		mCache = new LruCache<Object, BitmapDrawable>(mCapacity) {
			protected int sizeOf(Object key, BitmapDrawable value) {
				return value.getBitmap().getRowBytes() * value.getBitmap().getHeight();
			}

			@Override
			protected void entryRemoved(boolean evicted, Object key,
					BitmapDrawable oldValue, BitmapDrawable newValue) {
				super.entryRemoved(evicted, key, oldValue, newValue);
			}
		};
	}

	public void addBitmap(String key, BitmapDrawable bitmap) {
		if (bitmap == null) {
			return;
		}
		synchronized (mCache) {
			mCache.put(key, bitmap);
		}
	}

	public BitmapDrawable getBitmap(Object key) {
		synchronized (mCache) {
			BitmapDrawable bitmap = mCache.get(key);
			// Because of the Transition Animation, the alpha in memory
			// may be set to smaller than 255.
			// Just restore the alpha value to 255.
			if (bitmap != null) {
				bitmap.setAlpha(255);
			}
			return bitmap;
		}
	}

	public boolean containsKey(String key) {
		synchronized (mCache) {
			return mCache.snapshot().containsKey(key);
		}
	}

	public void clear() {
		synchronized (mCache) {
			mCache.evictAll();
			mCache = null;
		}
	}
}
