package com.rocks.api

import android.util.ArrayMap
import android.util.Log
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

        jsonParams["model_id"] = bodyDataHandler.modelId

        jsonParams["prompt"] = bodyDataHandler.positivePrompt

        jsonParams["negative_prompt"] = bodyDataHandler.negativePrompt

        jsonParams["width"] = bodyDataHandler.aspectRatio?.getSize()?.width.toString()

        jsonParams["height"] = bodyDataHandler.aspectRatio?.getSize()?.height.toString()

        jsonParams["safety_checker"] = bodyDataHandler.safetyChecker

        jsonParams["enhance_prompt"] = bodyDataHandler.enhancePrompt

        jsonParams["samples"] = bodyDataHandler.samples

        jsonParams["scheduler"] = bodyDataHandler.scheduler

        jsonParams["num_inference_steps"] = bodyDataHandler.numbInferenceSteps

        jsonParams["seed"] = bodyDataHandler.seed

        jsonParams["guidance_scale"] = bodyDataHandler.guidanceScale

        jsonParams["webhook"] = null

        jsonParams["track_id"] =null

        jsonParams["tomesd"] = bodyDataHandler.tomesd

        jsonParams["lora_model"] = null

        jsonParams["lora_strength"] = null

        jsonParams["embeddings_model"] = null

        jsonParams["use_karras_sigmas"] = bodyDataHandler.useKarrasSigmas

        jsonParams["vae"] = null



        if (bodyDataHandler.uploadImage!=null){

            with(bodyDataHandler.uploadImage){

                jsonParams["init_image"] = this?.link

                jsonParams["strength"] = bodyDataHandler.strength



            }

        }
        Log.d("@Arun", "getBodyForModel: "+JSONObject(jsonParams).toString())
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