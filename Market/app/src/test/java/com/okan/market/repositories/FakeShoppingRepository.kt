package com.okan.market.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.okan.market.data.local.ShoppingItem
import com.okan.market.other.Resource
import com.okan.market.data.remote.responses.ImageResponse

class FakeShoppingRepository : ShoppingRepository {

	private val shoppingItems = mutableListOf<ShoppingItem>()

	private val observableShoppingItems = MutableLiveData<List<ShoppingItem>>(shoppingItems)

	private val observableTotalPrice = MutableLiveData<Float>()

	private var shouldReturnNetworkError = false

	fun setShouldReturnNetworkError(value: Boolean) {
		shouldReturnNetworkError = value
	}

	private fun refreshLiveData() {
		observableShoppingItems.postValue(shoppingItems)
		observableTotalPrice.postValue(getTotalPrice())
	}

	private fun getTotalPrice(): Float {
		return shoppingItems.sumByDouble {
			it.price.toDouble()
		}.toFloat()
	}

	/**
	 * we used in insert case a list, not a real persistence
	 * because inserting in a list is much faster than
	 * inserting in a real database.
	 */
	override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
		shoppingItems.add(shoppingItem)
		refreshLiveData()
	}

	override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
		shoppingItems.remove(shoppingItem)
		refreshLiveData()
	}

	override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {
		return if (shouldReturnNetworkError) {
			Resource.error(
				msg = "Error",
				data = null
			)
		} else {
			Resource.success(
				ImageResponse(
					listOf(),
					total = 0,
					totalHints = 0
				)
			)
		}
	}

	override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
		return observableShoppingItems
	}

	override fun observeTotalPrice(): LiveData<Float> {
		return observableTotalPrice
	}

}