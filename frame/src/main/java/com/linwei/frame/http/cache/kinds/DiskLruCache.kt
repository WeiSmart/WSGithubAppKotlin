package com.linwei.frame.http.cache.kinds

import com.linwei.frame.http.cache.Cache
import com.linwei.frame.utils.AppExecutors
import java.io.File
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

/**
 * ---------------------------------------------------------------------
 * @Author: WeiShuai
 * @Time: 2019/10/14
 * @Contact: linwei9605@gmail.com"
 * @Follow: https://github.com/WeiShuaiDev
 * @Description: 内存缓存模块，{@link LruCache<K,V>} 实现了 {@link Cache<K,V>} 接口, {@link LruCache<K,V>} 会根据内存最大存储大小，
 *               对内存数据进行优化删除。{@code mMaxSize}指定内存大小, {@code getItemSize()} 指定每个 {@code item} 所占用的size,
 *               默认为1,这个 size 的单位必须和构造函数所传入的@{mMaxSize}一致
 *              K:表示存储Key,内存存储中必须唯一
 *              V:代表存储Value,存储内容数据
 *-----------------------------------------------------------------------
 */
class DiskLruCache(private val cacheDir: File, val sizeLimit: Long, val countLimit: Int) :
    Cache<File, Long>, AppExecutors() {

    /**
     * {@link mMapCache} 是一个线程安全，同时使用 {@code SoftReference}软引用包裹数据源，在内存不足出现报警，GC会进行数据回收
     * 一般在使用HashMap充当内存存储器，保存 {@code Bitmap}数据源，容易出现OOM
     */
    private val mLastUsageDates: MutableMap<File, Long> = Collections.synchronizedMap(
        mutableMapOf()
    )

    /**
     * {@cacheDir}缓存文件下，缓存使用容量大小
     */
    private val mCacheSize: AtomicLong = AtomicLong()

    /**
     * {@cacheDir}缓存目录下，缓存使用容量个数
     */
    private val mCacheCount: AtomicInteger = AtomicInteger()

    init {
        calculateCacheSizeAndCacheCount()
    }

    /**
     * 根据 {@link cacheDir} 缓存目录，计算出缓存目录文件大小 {@link mCacheSize},文件个数 {@link mCacheCount}
     */
    private fun calculateCacheSizeAndCacheCount() {
        diskIO.execute {
            val listFiles: Array<File>? = cacheDir.listFiles()
            var size: Long = 0
            var count = 0
            listFiles?.forEach { file ->
                size += calculateSize(file)
                count += 1
                mLastUsageDates[file] = file.lastModified()
            }
            mCacheSize.set(size)
            mCacheCount.set(count)
        }
    }

    /**
     * {@link mCacheCount} 缓存文件存储个数
     */
    override fun size(): Int = mCacheCount.get()

    /**
     * {@link countLimit} 缓存文件存储最大容量个数
     */
    override fun getMaxSize(): Int = countLimit

    /**
     * 获取缓存文件 {@code key},
     */
    override fun get(key: File): Long? =
        mLastUsageDates[key]

    /**
     *  循环判断 {@link mCacheCount} 加上当前文件大小 {@code key} 是否超过最大缓存最大值 {@link countLimit},
     *  如果缓存不足，进行 {@link #removeNext} 缓存优化，更新 {@link mCacheCount} {@link mCacheSize} 大小。
     *  并{@code key} 存储到 {@link mLastUsageDates} 数据结构。
     * @param key 缓存文件
     * @param value 缓存文件修改时间
     */
    override fun put(key: File, value: Long): Long? {
        var curCacheCount: Int = mCacheCount.get()
        while (curCacheCount + 1 > countLimit) {
            val freedSize: Long = removeNext()
            mCacheSize.addAndGet(-freedSize)
            curCacheCount = mCacheCount.addAndGet(-1)
        }
        mCacheCount.addAndGet(1)

        val valueSize: Long = calculateSize(key)
        var curCacheSize: Long = mCacheSize.get()
        while (curCacheSize + valueSize > sizeLimit) {
            val freedSize: Long = removeNext()
            curCacheSize = mCacheSize.addAndGet(-freedSize)
        }
        mCacheSize.addAndGet(valueSize)

        key.setLastModified(value)

        val lastModified: Long? = mLastUsageDates[key]
        mLastUsageDates[key] = value
        return lastModified
    }

    /**
     * 删除 {@code key} 文件，并同步删除 {@code mLastUsageDates} 中目标 {@code key} 数据
     * @param key 缓存文件
     */
    override fun remove(key: File): Long? {
        var lastModified: Long? = 0
        if (mLastUsageDates.containsKey(key)) {
            lastModified = mLastUsageDates[key]
            mLastUsageDates.remove(key)
        }

        removeFile(key.name)
        return lastModified
    }

    /**
     * 判断 {@link mLastUsageDates} 是否包含 {@code key} 文件
     */
    override fun containsKey(key: File): Boolean =
        mLastUsageDates.containsKey(key)

    /**
     * 返回 {@link mLastUsageDates} 所有文件
     */
    override fun keySet(): Set<File>? = mLastUsageDates.keys

    /**
     * 删除 {@link cacheDir} 文件夹下所有缓存文件，文件最后次修改存储信息
     */
    override fun clear() {
        mCacheSize.set(0)
        mCacheCount.set(0)
        mLastUsageDates.clear()
        //删除cacheDir缓存目录下所有文件
        val listFiles: Array<File>? = cacheDir.listFiles()
        listFiles?.forEach { file ->
            file.delete()
        }
    }

    /**
     * 根据 {@code key} 名称，创建缓存文件,并把存储文件存储到 {@link mLastUsageDates} 数据结构
     */
    private fun newFile(key: String): File {
        val file = File(cacheDir, key.hashCode().toString())
        val currentTime: Long = System.currentTimeMillis()
        file.setLastModified(currentTime)
        mLastUsageDates[file] = currentTime
        return file
    }

    /**
     * 根据 {@code key}文件名，删除 {@link cacheDir}文件目录下缓存文件
     * @param key
     */
    private fun removeFile(key: String): Boolean {
        val file: File = newFile(key)
        return file.delete()
    }


    /**
     * 优化 {@link cacheDir} 缓存目录文件存储，当 {@link cacheDir} 存储文件个数 {@link mCacheCount} 超过
     * {@link countLimit} 最大缓存文件个数时，就会优化一些旧缓存文件(删除)，并更新存储个数 {@link mCacheCount},
     * 缓存大小 {@link mCacheSize}
     */
    private fun removeNext(): Long {
        if (mLastUsageDates.isEmpty()) return 0
        val entries: MutableSet<MutableMap.MutableEntry<File, Long>> = mLastUsageDates.entries
        var oldestUsage: Long = 0
        var mostLongUsedFile: File? = null
        synchronized(mLastUsageDates) {
            for ((key, lastValueUsage) in entries) {
                if (mostLongUsedFile == null) {
                    mostLongUsedFile = key
                    oldestUsage = lastValueUsage
                } else {
                    if (lastValueUsage < oldestUsage) {
                        oldestUsage = lastValueUsage
                        mostLongUsedFile = key
                    }
                }
            }
        }
        if (mostLongUsedFile != null) {
            val fileSize: Long = calculateSize(mostLongUsedFile!!)
            if (mostLongUsedFile!!.delete()) {
                mLastUsageDates.remove(mostLongUsedFile)
            }
            return fileSize
        }
        return 0
    }

    /**
     * 返回{@code file}文件大小
     * @param file 存储文件
     */
    private fun calculateSize(file: File): Long {
        return file.length()
    }

}