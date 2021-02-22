package com.okan.market.repositories

import androidx.lifecycle.LiveData
import com.okan.market.data.local.ShoppingDao
import com.okan.market.data.local.ShoppingItem
import com.okan.market.other.Resource
import com.okan.market.data.remote.ApiService
import com.okan.market.data.remote.responses.ImageResponse
import javax.inject.Inject


class DefaultShoppingRepository
@Inject
constructor(
	private val shoppingDao: ShoppingDao,
	private val apiService: ApiService
) : ShoppingRepository {
	override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
		shoppingDao.insertShoppingItem(shoppingItem)
	}

	override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
		shoppingDao.deleteShoppingItem(shoppingItem)
	}

	override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {
		return try {
			val response = apiService.searchForImage(imageQuery)
			if (response.isSuccessful) {
				response.body()?.let { imageResponse ->
					return@let Resource.success(imageResponse)
				} ?: Resource.error("Unknown error occurred", null)
			} else {
				Resource.error("Unknown error occurred", null)
			}
		} catch (e: Exception) {
			Resource.error("Could not reach the server. Check your connection",null)
		}
	}


	override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
		return shoppingDao.observeAllShoppingItems()
	}

	override fun observeTotalPrice(): LiveData<Float> {
		return shoppingDao.observeTotalPrice()
	}

}