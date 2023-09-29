package com.rocks.api

import android.util.ArrayMap
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object Api {

    fun getBodyForModel(prompt:String): RequestBody {

        val jsonParams: ArrayMap<String?, Any?> = ArrayMap()

        jsonParams["key"] = "oXQquZgB44Ssi2bjm2JxGjE5CGthtW06iSaHzCsLqk4iEauI8L9OqyC1WDpf"
        jsonParams["model_id"] = "midjourney"
        jsonParams["prompt"] = prompt
        jsonParams["negative_prompt"] = "milk"
        jsonParams["width"] = "512"
        jsonParams["height"] = "512"
        jsonParams["safety_checker"] = "yes"
        jsonParams["samples"] = "1"
        jsonParams["num_inference_steps"] = "30"
        jsonParams["seed"] = "null"
        jsonParams["guidance_scale"] = "7.5"
        jsonParams["webhook"] = "null"
        jsonParams["track_id"] = "null"
        jsonParams["tomesd"] = "yes"
        jsonParams["scheduler"] = "DPMSolverMultistepScheduler"

        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JSONObject(jsonParams).toString())

    }

    fun getBodyOnlyKey(): RequestBody {

        val jsonParams: ArrayMap<String?, Any?> = ArrayMap()

        jsonParams["key"] = "oXQquZgB44Ssi2bjm2JxGjE5CGthtW06iSaHzCsLqk4iEauI8L9OqyC1WDpf"

        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JSONObject(jsonParams).toString())

    }


    private const val BASE_URL = "https://stablediffusionapi.com"

    private fun getInstance(): Retrofit {

        return Retrofit.Builder().baseUrl(getBaseUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    private fun getBaseUrl() = BASE_URL


    fun createApi(): ApiInterface? {

        runCatching {

            return getInstance().create(ApiInterface::class.java)

        }

        return null

    }


}