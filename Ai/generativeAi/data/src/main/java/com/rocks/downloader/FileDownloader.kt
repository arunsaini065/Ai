package com.rocks.downloader

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.os.Environment
import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*


class FileDownloader(private val lifecycleScope: LifecycleCoroutineScope) {


    fun saveImage(bitmap:Bitmap, callback: (File) -> Unit){

        lifecycleScope.launch(Dispatchers.IO) {

            runCatching {

                val blob = ByteArrayOutputStream()

                bitmap.compress(CompressFormat.PNG, 0 , blob)

                val file =saveFile(blob)

                withContext(Dispatchers.Main){
                    callback(file)
                }

            }

        }

    }



     private fun saveFile( result: ByteArrayOutputStream?): File {

        val file = File(getFilePath("/RocksAiImage").absolutePath +"/"+"IMG_AI_"+System.currentTimeMillis()+".png")

        runCatching {

            if (!file.exists()) {

                file.createNewFile()

            }

            val fos = FileOutputStream(file)

            fos.write(result?.toByteArray())

            fos.close()

        }

         return file

     }


    companion object {


        fun getFilePath(location:String): File {

            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)?.absolutePath + location)
            if (!file.exists()) {
                if (file.mkdirs()) {
                    Log.e("@STORAGE", "Directory not created")
                }
            }
            return file
       }
    }
}