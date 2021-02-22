package com.okan.market.data.remote.responses

data class ImageResponse(
	val hints: List<ImageResult>,
	val total: Int,
	val totalHints: Int
)