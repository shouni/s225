package com.example.myapp.image;

import android.graphics.Bitmap;
import android.util.LruCache;

public class ImageMemoryCache {

    private static int cacheSize = 6 * 1024;

    private static LruCache<String, Bitmap> mMemoryCache = new LruCache<String, Bitmap>(
            cacheSize) {
        @Override
        protected int sizeOf(String key, Bitmap bitmap) {
            return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
        }

        protected void entryRemoved(boolean evicted, String key,
                                    Bitmap oldValue, Bitmap newValue) {

            if (oldValue != null && !oldValue.isRecycled()) {
                oldValue.recycle();
                oldValue = null;
            }
        }

        ;
    };

    public static Bitmap getImage(String key) {
        Bitmap bitmap = mMemoryCache.get(key);
        if (bitmap != null && bitmap.isRecycled()) {
            return null;
        } else {
            return bitmap;
        }
    }

    public static void setImage(String key, Bitmap bitmap) {
        if (!ImageMemoryCache.hasImage(key) && bitmap != null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public static boolean hasImage(String key) {
        if (getImage(key) == null) {
            return false;
        } else {
            return true;
        }
    }

    public static void clear() {
        mMemoryCache.evictAll();
    }
}