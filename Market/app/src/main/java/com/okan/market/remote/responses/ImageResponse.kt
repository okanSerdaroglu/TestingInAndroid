package com.okan.market.remote.responses

data class ImageResponse(
	val hints: List<ImageResult>,
	val total: Int,
	val totalHints: Int
)