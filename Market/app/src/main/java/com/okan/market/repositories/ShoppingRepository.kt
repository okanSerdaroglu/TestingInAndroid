package com.okan.market.repositories

import androidx.lifecycle.LiveData
import com.okan.market.data.local.ShoppingItem
import com.okan.market.other.Resource
import com.okan.market.remote.responses.ImageResponse

/**
 * we created this interface, because we implement this
 * interface both ShoppingRepository and our ShoppingRepositoryTest.
 * With this method we can implement same methods in both classes.
 * For this reason it is very good technique for testing
 */

interface ShoppingRepository {

	suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

	suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

	suspend fun searchForImage(imageQuery: String): Resource<ImageResponse>

	fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>

	fun observeTotalPrice(): LiveData<Float>

}