package com.rocks.fetcher

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.database.Cursor
import android.os.Build
import android.provider.MediaStore
import android.text.TextUtils
import android.util.SparseArray
import com.rocks.INTERNAL_STORAGE
import com.rocks.UNKOWN
import com.rocks.checkPermission
import java.io.File
import java.util.Arrays
import java.util.Objects

class FetchAlbumsAsync(application: Application, recent: Boolean) {
    private var androidX = false
    private val context: Context
    private val recent: Boolean

    init {

        context = application

        this.recent = recent

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            androidX = true

        }

    }

    val allAlbumDetails: MutableList<AlbumModel>

        get() {

            var cursor: Cursor? = null

            if (context.checkPermission()) {

                runCatching {

                    cursor = if (androidX) {

                        context.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            PROJECTION_BUCKET_Q,
                            null,
                            null,
                            null
                        )

                    } else {

                        context.contentResolver.query(

                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            PROJECTION_BUCKET,
                            BUCKET_GROUP_BY,
                            null,
                            BUCKET_ORDER_BY

                        )

                    }

                } .onFailure {

                    runCatching {

                        cursor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            context.contentResolver.query(
                                MediaStore.Video.Media.getContentUri(
                                    MediaStore.VOLUME_EXTERNAL
                                ), PROJECTION_BUCKET_Q, null, null, null
                            )

                        } else {

                            context.contentResolver.query(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                PROJECTION_BUCKET,
                                BUCKET_GROUP_BY,
                                null,
                                BUCKET_ORDER_BY
                            )
                        }
                    }
                }
            }
            return parseAlbumLst(cursor)
        }

    @SuppressLint("Range")
    private fun parseAlbumLst(cursor: Cursor?): MutableList<AlbumModel> {

        val sparseArray = SparseArray<AlbumModel>()

        val albumList = mutableListOf<AlbumModel>()

        if (cursor == null) {

            return albumList
        }

        if (cursor.moveToFirst()) {

            try {
                if (recent) {
                    albumList.add(0, AlbumModel("", "", "Recent", "", ""))
                }
                do {
                    if (androidX) {
                        val bucketId =
                            cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID))
                        var albumModel = sparseArray[bucketId]
                        if (albumModel == null) {
                            val path =
                                cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
                            albumModel = AlbumModel()
                            albumModel.bucket_id = bucketId.toString()
                            val bucketName =
                                cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
                            if (TextUtils.isEmpty(bucketName)) {
                                albumModel.bucketName = UNKOWN
                            } else if (bucketName == "0") {
                                albumModel.bucketName = INTERNAL_STORAGE
                            } else {
                                albumModel.bucketName = bucketName
                            }
                            albumModel.bucketData = path
                            val file = File(path)
                            if (file.exists()) {
                                albumModel.bucketDateTaken =
                                    Objects.requireNonNull(file.parentFile).lastModified()
                                        .toString()
                            } else {
                                albumModel.bucketDateTaken =
                                    cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN))
                            }
                            val dateModifiedColNum =
                                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
                            val dateModified = cursor.getLong(dateModifiedColNum)
                            albumModel.bucketModifiedDate = dateModified
                            albumModel.totalPhotocount = 1
                            sparseArray.append(bucketId, albumModel)
                        } else {
                            albumModel.totalPhotocount = albumModel.totalPhotocount + 1
                        }
                    } else {
                        val albumModel = AlbumModel()
                        albumModel.bucket_id =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID))
                        val bucketName =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
                        if (TextUtils.isEmpty(bucketName)) {
                            albumModel.bucketName = UNKOWN
                        } else if (bucketName == "0" || TextUtils.isEmpty(bucketName)) {
                            albumModel.bucketName = INTERNAL_STORAGE
                        } else {
                            albumModel.bucketName = bucketName
                        }
                        val uri =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
                        albumModel.bucketDateTaken =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN))
                        val dateModifiedColNum =
                            cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
                        val dateModified = cursor.getLong(dateModifiedColNum)
                        albumModel.bucketModifiedDate = dateModified
                        if (!TextUtils.isEmpty(uri)) {
                            val file = File(uri)
                            val fileParent = file.parentFile
                            if (fileParent != null) {
                                val fileArray = fileParent.listFiles(ImageFileFilter())
                                if (fileArray != null && fileArray.size > 0) {
                                    val imagePath = getThumbNail(fileArray)
                                    albumModel.bucketData = imagePath
                                    albumModel.bucketCount = fileArray.size.toString()
                                    albumList.add(albumModel)
                                }
                            }
                        }
                    }
                } while (cursor.moveToNext())
                if (androidX && sparseArray.size() > 0) {
                    for (x in 0 until sparseArray.size()) {
                        albumList.add(sparseArray.valueAt(x))
                    }
                }
            } catch (ignored: Throwable) {

            } finally {

                cursor.close()

            }
        }
        return albumList
    }

    private fun getThumbNail(fileArray: Array<File>?): String? {
        runCatching {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                try {
                    if (fileArray != null) {
                        Arrays.sort(fileArray, Comparator.comparingLong { obj: File -> obj.lastModified() }.reversed())
                    }
                } catch (ignored: Exception) {
                }
            }
            if (fileArray != null) {
                for (file in fileArray) {
                    if (file.exists() && file.length() > 0) {
                        return file.path
                    }
                }
            }
        }
        return null
    }

    companion object {

        private val PROJECTION_BUCKET = arrayOf(
            MediaStore.Images.ImageColumns.BUCKET_ID,
            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
            MediaStore.Images.ImageColumns.DATE_TAKEN,
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns._ID,
            "count()"
        )

        private val PROJECTION_BUCKET_Q = arrayOf(
            MediaStore.Images.ImageColumns.BUCKET_ID,
            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
            MediaStore.Images.ImageColumns.DATE_TAKEN,
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns._ID
        )

        private const val BUCKET_GROUP_BY = "1) GROUP BY 1,(2"

        private const val BUCKET_ORDER_BY = "MAX(datetaken) DESC"
    }
}
