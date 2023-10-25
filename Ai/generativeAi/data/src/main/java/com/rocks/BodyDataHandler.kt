package com.rocks

import com.rocks.model.UploadImage

class BodyDataHandler {

    var key  = "bXXHLMZmQL5D7Lgb4yM1ZWtUuqYleNf6Tqyz658zj1PIAvC7N2hk0jgtCk7N"

    var positivePrompt:String?=null

    var negativePrompt:String?=null

    var modelId:String?="midjourney"

    var aspectRatio:AspectRatio?= AspectRatio()

    var safetyChecker:String?="yes"

    var enhancePrompt:String?="yes"

    var useKarrasSigmas:String?="yes"

    var samples:String?="1"

    var numbInferenceSteps:String?="30"

    var seed:Int?=89

    var guidanceScale:Double?=7.5

    var webhook:String?="null"

    var trackId:String?="null"

    var tomesd:String?="yes"

    var base64:String?="yes"

    var scheduler:String?="UniPCMultistepScheduler"

    var uploadImage:UploadImage?=null

    var isAddImage = false

    var strength = 0.7



}