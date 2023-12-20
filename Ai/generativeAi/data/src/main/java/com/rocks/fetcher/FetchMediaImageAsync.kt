package com.rocks.fetcher

import android.app.Application
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.rocks.checkPermission

class FetchMediaImageAsync(private val application: Application, private val mBucketId: Array<String?>?) {

    private val selection = MediaStore.Images.Media.BUCKET_ID + "=?"

    fun queryImages(): MutableList<MediaStoreData> {

        // -1 for all

        return query(

            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,

            MediaStore.Images.ImageColumns.DATE_TAKEN, MediaStore.Images.ImageColumns._ID,

            MediaStore.Images.ImageColumns.DATE_TAKEN, MediaStore.Images.ImageColumns.DATE_MODIFIED,

            MediaStore.Images.ImageColumns.MIME_TYPE, MediaStore.Images.ImageColumns.ORIENTATION,

            -1

        )

    }



    fun queryImages(limit: Int): List<MediaStoreData> {

        return query(

            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,

            MediaStore.Images.ImageColumns.DATE_MODIFIED, MediaStore.Images.ImageColumns._ID,


            MediaStore.Images.ImageColumns.DATE_TAKEN, MediaStore.Images.ImageColumns.DATE_MODIFIED,

            MediaStore.Images.ImageColumns.MIME_TYPE, MediaStore.Images.ImageColumns.ORIENTATION,

            limit
        )
    }

    private fun query(contentUri: Uri, projection: Array<String>, sortByCol: String, idCol: String, dateTakenCol: String, dateModifiedCol: String, mimeTypeCol: String, orientationCol: String, limit: Int): MutableList<MediaStoreData> {

        val data: MutableList<MediaStoreData> = mutableListOf()

        val cursor: Cursor?

        var activeLimitOperation = false

        if (application.checkPermission()) {

            try {

                if (mBucketId != null && mBucketId[0] != null) {

                    if (limit > 0) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

                            activeLimitOperation = true

                            cursor = application.contentResolver.query(
                                contentUri,
                                projection,
                                selection,
                                mBucketId,
                                "$sortByCol DESC"
                            )
                        } else {

                            cursor = application.contentResolver.query(
                                contentUri,
                                projection,
                                selection,
                                mBucketId,
                                "$sortByCol DESC LIMIT $limit"
                            )
                        }

                    } else {

                        cursor = application.contentResolver.query(
                            contentUri,
                            projection,
                            selection,
                            mBucketId,
                            "$sortByCol DESC"
                        )

                    }

                } else {

                    if (limit > 0) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            activeLimitOperation = true
                            cursor = application.contentResolver.query(
                                contentUri,
                                projection,
                                null,
                                null,
                                "$sortByCol DESC"
                            )
                        } else {

                            cursor = application.contentResolver.query(
                                contentUri,
                                projection,
                                null,
                                null,
                                "$sortByCol DESC LIMIT $limit"
                            )

                        }

                    } else {

                        cursor = application.contentResolver.query(
                            contentUri,
                            projection,
                            null,
                            null,
                            "$sortByCol DESC"
                        )

                    }

                }
            } catch (e: Throwable) {

                return data
            }


            if (cursor == null) {

                return data

            }

            try {

                val idColNum = cursor.getColumnIndexOrThrow(idCol)
                val imageData = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA)
                val dateTakenColNum = cursor.getColumnIndexOrThrow(dateTakenCol)
                val dateModifiedColNum = cursor.getColumnIndexOrThrow(dateModifiedCol)
                val mimeTypeColNum = cursor.getColumnIndex(mimeTypeCol)
                val orientationColNum = cursor.getColumnIndexOrThrow(orientationCol)
                val fileSize = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.SIZE)
                var count = 0
                var checkLimit = true
                while (cursor.moveToNext() && checkLimit) {
                    count += 1
                    if (activeLimitOperation) {
                        checkLimit = count < limit
                    }
                    val id = cursor.getLong(idColNum)
                    val dateTaken = cursor.getLong(dateTakenColNum)
                    val mimeType = cursor.getString(mimeTypeColNum)
                    val dateModified = cursor.getLong(dateModifiedColNum)
                    val orientation = cursor.getInt(orientationColNum)
                    val uri = cursor.getString(imageData)
                    val fileSizeValue = cursor.getLong(fileSize)

                    val mediaStoreData = MediaStoreData(
                        id, uri, fileSizeValue,
                        mimeType, dateTaken, dateModified, orientation, ""
                    )
                   data.add(mediaStoreData)

                }
            } catch (_: Throwable) {
            } finally {
                if (!cursor.isClosed) {
                    cursor.close()
                }
            }
        }
        return data
    }

    companion object {

        private val IMAGE_PROJECTION = arrayOf(

            MediaStore.Images.ImageColumns._ID,

            MediaStore.Images.ImageColumns.DATE_TAKEN,

            MediaStore.Images.ImageColumns.DATE_MODIFIED,

            MediaStore.Images.ImageColumns.MIME_TYPE,


            MediaStore.Images.ImageColumns.DATA,

            MediaStore.Images.ImageColumns.SIZE,

            MediaStore.Images.ImageColumns.ORIENTATION

        )
    }
}
