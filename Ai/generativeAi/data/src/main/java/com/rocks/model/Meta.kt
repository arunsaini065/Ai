package com.rocks.model

data class Meta(
    val H: Int,
    val W: Int,
    val algorithm_type: String,
    val base64: String,
    val init_image: String,
    val clip_skip: Int,
    val embeddings: Any,
    val file_prefix: String,
    val full_url: String,
    val guidance_scale: Double,
    val instant_response: String,
    val lora: Any,
    val lora_strength: Int,
    val model_id: String,
    val multi_lingual: String,
    val n_samples: Int,
    val negative_prompt: String,
    val panorama: String,
    val prompt: String,
    val safety_checker: String,
    val safety_checker_type: String,
    val scheduler: String,
    val seed: Long,
    val self_attention: String,
    val steps: Int,
    val temp: String,
    val tomesd: String,
    val upscale: String,
    val use_karras_sigmas: String,
    val vae: Any
)