package com.rocks.fetcher

import androidx.recyclerview.widget.DiffUtil
import java.io.Serializable

data class MediaStoreData(
    val rowId: Long,
    val uri: String,
    var fileSize: Long,
    val mimeType: String,
    val dateTaken: Long,
    var dateModified: Long,
    val orientation: Int,
    val formatted: String
) : Serializable {



    companion object{


        val DIFF = object : DiffUtil.ItemCallback<MediaStoreData>(){

            override fun areContentsTheSame(oldItem: MediaStoreData, newItem: MediaStoreData): Boolean {

                return oldItem==newItem

            }

            override fun areItemsTheSame(oldItem: MediaStoreData, newItem: MediaStoreData): Boolean {

                return oldItem==newItem

            }

        }

    }
}
