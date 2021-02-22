package com.okan.market.data.remote

import com.okan.market.BuildConfig
import com.okan.market.data.remote.responses.ImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

	@GET("/api/")
	suspend fun searchForImage(
		@Query("q") searchQuery: String,
		@Query("key") apiKey: String = BuildConfig.API_KEY
	): Response<ImageResponse>
}