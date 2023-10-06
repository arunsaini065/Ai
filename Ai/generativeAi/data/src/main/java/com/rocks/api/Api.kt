package com.rocks.api

import android.util.ArrayMap
import com.rocks.BodyDataHandler
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object Api {

    fun getBodyForModel(bodyDataHandler: BodyDataHandler): RequestBody = with(bodyDataHandler) {

        val jsonParams: ArrayMap<String?, Any?> = ArrayMap()

        jsonParams["key"] = key
        jsonParams["model_id"] = "midjourney"
        jsonParams["prompt"] = positivePrompt
        jsonParams["negative_prompt"] = negativePrompt
        jsonParams["width"] = aspectRatio?.getSize()?.width.toString()
        jsonParams["height"] = aspectRatio?.getSize()?.width.toString()
        jsonParams["safety_checker"] = safetyChecker
        jsonParams["samples"] = "1"
        jsonParams["num_inference_steps"] = numbInferenceSteps
        jsonParams["seed"] = seed
        jsonParams["guidance_scale"] = guidanceScale
        jsonParams["webhook"] = webhook
        jsonParams["track_id"] =trackId
        jsonParams["tomesd"] = tomesd
        jsonParams["scheduler"] = scheduler

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