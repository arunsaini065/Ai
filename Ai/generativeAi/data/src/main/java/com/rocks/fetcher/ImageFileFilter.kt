package com.rocks.fetcher

import java.io.File
import java.io.FilenameFilter

class ImageFileFilter : FilenameFilter {

    private val acceptedExtensions = imageAcceptedExtensions

    override fun accept(dir: File, filename: String): Boolean {

        acceptedExtensions.forEach {

            if (filename.endsWith(".$it")) {

                return true

            }

        }

        return false

    }

    companion object {

        val imageAcceptedExtensions = arrayOf("jpg", "jpeg", "png")

    }
}