package com.rocks.fetcher

import android.text.TextUtils
import androidx.recyclerview.widget.DiffUtil
import java.io.Serializable

class AlbumModel : Serializable {
    var bucket_id: String? = null
    var path: String? = null

    constructor()
    constructor(
        bucket_id: String?,
        bucketData: String?,
        bucketName: String?,
        buckeDateTaken: String?,
        bucketCount: String?
    ) {
        this.bucket_id = bucket_id
        this.bucketData = bucketData
        this.bucketName = bucketName
        bucketDateTaken = buckeDateTaken
        this.bucketCount = bucketCount
    }

    var bucketName: String? = null
    var bucketData: String? = null
    var bucketDateTaken: String? = null
    var bucketCount: String? = null
    var bucketModifiedDate: Long = 0
    var totalPhotocount = 0



    companion object{


        val DIFF = object : DiffUtil.ItemCallback<AlbumModel>(){

            override fun areContentsTheSame(oldItem: AlbumModel, newItem: AlbumModel): Boolean {

                return oldItem.path==newItem.path

            }

            override fun areItemsTheSame(oldItem: AlbumModel, newItem: AlbumModel): Boolean {

                return oldItem==newItem

            }

        }

    }
}
