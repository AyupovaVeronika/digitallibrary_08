package com.logan.digitallibrary

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.logan.digitallibrary.epub.utils.EpubImageFetcher
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class digitallibraryApplication : Application(), ImageLoaderFactory {
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.02)
                    .build()
            }
            .components {
                add(EpubImageFetcher.Factory())
            }
            .crossfade(true)
            .build()
    }
}
