package com.jktaihe.utils.v4;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;
/**
 * Created by jktaihe on 2016/7/24.
 * email:jktaihe@gmail.com
 * blog:jktaihe.top
 * https://github.com/jixh
 */
public class BitmapLruCache {

	private LruCache<String, Bitmap> mMemoryCache;
	
	//内存缓存
	
	private BitmapLruCache() {
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);  
		if (mMemoryCache == null){
			mMemoryCache = new LruCache<String, Bitmap>(maxMemory / 8) {
				@Override
				protected int sizeOf(String key, Bitmap bitmap) {
					// 重写此方法来衡量每张图片的大小，默认返回图片数量。
					return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
				}
	
				@Override
				protected void entryRemoved(boolean evicted, String key,Bitmap oldValue, Bitmap newValue) {
					Log.v("tag", "hard cache is full , push to soft cache");
				}
			};
		}
	}
	

	/**
     * 清空缓存
     */
	public void clearCache() {
        if (mMemoryCache != null) {
            if (mMemoryCache.size() > 0) {
                Log.d("CacheUtils","mMemoryCache.size() " + mMemoryCache.size());
                mMemoryCache.evictAll();
                Log.d("CacheUtils", "mMemoryCache.size()" + mMemoryCache.size());
            }
            mMemoryCache = null;
        }
    }

	/**
     * 添加图片到缓存
     */
    public synchronized void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (mMemoryCache.get(key) == null) {
            if (key != null && bitmap != null)
                mMemoryCache.put(key, bitmap);
        } else
            Log.w("addBitmapToMemoryCache", "the res is aready exits");
    }

    /**
     * 从缓存中取得图片
     */
    public synchronized Bitmap getBitmapFromMemCache(String key) {
        Bitmap bm = mMemoryCache.get(key);
        if (key != null) {
            return bm;
        }
        return null;
    }

    /**
     * 移除缓存
     */
    public synchronized void removeImageCache(String key) {
        if (key != null) {
            if (mMemoryCache != null) {
                Bitmap bm = mMemoryCache.remove(key);
                if (bm != null)
                    bm.recycle();
            }
        }
    }
}
