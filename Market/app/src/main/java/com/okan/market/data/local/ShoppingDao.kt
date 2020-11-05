package com.okan.market.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShoppingDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

	@Delete
	suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

	/**
	 * LiveData works asynchronous for this reason we do not need to use
	 * suspend function here
	 */
	@Query("SELECT * FROM shopping_items")
	fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>

	@Query("SELECT SUM(price * amount) FROM shopping_items")
	fun observeTotalPrice(): LiveData<Float>

}