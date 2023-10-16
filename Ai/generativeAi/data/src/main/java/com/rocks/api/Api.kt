package com.rocks.api

import android.util.ArrayMap
import com.rocks.BodyDataHandler
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


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

        if (bodyDataHandler.uploadImage!=null){

            with(bodyDataHandler.uploadImage){

                jsonParams["init_image"] = this?.link

                jsonParams["scheduler"] = bodyDataHandler.scheduler

                jsonParams["strength"] = bodyDataHandler.strength

                jsonParams["use_karras_sigmas"] = "yes"

                jsonParams["vae"] = null

                jsonParams["lora_model"] = null

                jsonParams["lora_strength"] = null

                jsonParams["embeddings_model"] = null

                jsonParams["enhance_prompt"] = "yes"

            }

        }

        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JSONObject(jsonParams).toString())

    }

    fun getBodyForUploadImage(bodyDataHandler: BodyDataHandler): RequestBody {
        val jsonParams: ArrayMap<String?, Any?> = ArrayMap()

        jsonParams["key"] = bodyDataHandler.key

        jsonParams["image"] ="data:image/jpeg;base64," + bodyDataHandler.base64

        jsonParams["crop"] ="false"

        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JSONObject(jsonParams).toString())

    }

    fun getBodyOnlyKey(bodyDataHandler: BodyDataHandler): RequestBody {

        val jsonParams: ArrayMap<String?, Any?> = ArrayMap()

        jsonParams["key"] = bodyDataHandler.key

        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JSONObject(jsonParams).toString())

    }


    private const val BASE_URL = "https://stablediffusionapi.com"

    private fun getInstance(): Retrofit {

        val client: OkHttpClient.Builder = OkHttpClient.Builder()
        client.connectTimeout(150, TimeUnit.SECONDS)
        client.readTimeout(150, TimeUnit.SECONDS)
        client.writeTimeout(150, TimeUnit.SECONDS)

        return Retrofit.Builder().baseUrl(getBaseUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
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