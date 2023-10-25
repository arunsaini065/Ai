package com.rocks.model

data class ApiOutput(
    val generationTime: Double,
    val id: Long,
    val meta: Meta,
    var output: MutableList<String>,
    val status: String,
    val webhook_status: String,
    val message: String,
)