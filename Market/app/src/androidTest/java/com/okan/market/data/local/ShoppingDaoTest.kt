 package com.okan.market.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.okan.market.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class) // this says that we are in Android test
@SmallTest // unit tests
class ShoppingDaoTest {

	/**
	 * This rule says Junit4 work the
	 * methods one and another step by step
	 * in the same thread
	 */
	@get:Rule
	var instantTaskExecutorRule = InstantTaskExecutorRule()

	private lateinit var database: ShoppingItemDB
	private lateinit var dao: ShoppingDao

	@Before
	fun setup() {
		database = Room.inMemoryDatabaseBuilder( // this means it is only available in ram. Not a real database
			ApplicationProvider.getApplicationContext(),
			ShoppingItemDB::class.java
		).allowMainThreadQueries().build()
		/**
		 * we added allowMainThreadQueries() method, because
		 * we want to access our db from main thread. If
		 * another threads access our fake db ascyn, this can effect
		 * our result. That's why we added this code.
		 */

		dao = database.shoppingDao()
	}

	@After //close your dao after test
	fun teardown() {
		database.close()
	}

	/**
	 *  runBlockingTest is just a way to work with a coroutine in main thread
	 *  we don't want multithreading here
	 */
	@Test
	fun insertShoppingItem() = runBlockingTest {
		val shoppingItem = ShoppingItem(
			name = "name",
			amount = 1,
			price = 1f,
			imageUrl = "url",
			id = 1
		)

		dao.insertShoppingItem(shoppingItem)

		/**
		 * this method returns liveData which is not good
		 * for this case. For this reason we have to use another
		 * implementation called LiveDataUtilAndroidTest. This class
		 * waits 2 seconds for your live data result. If live data does
		 * not returns a value in 2 seconds it throws an exception
		 */
		val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

		/**
		 * check if our shopping item contains in allShoppingItems
		 */
		assertThat(allShoppingItems).contains(shoppingItem)

	}

	@Test
	fun deleteShoppingItem() = runBlockingTest {
		val shoppingItem = ShoppingItem(
			name = "name",
			amount = 1,
			price = 1f,
			imageUrl = "url",
			id = 1
		)
		dao.insertShoppingItem(shoppingItem)
		dao.deleteShoppingItem(shoppingItem)

		val allShoppingItemList = dao.observeAllShoppingItems().getOrAwaitValue()

		assertThat(allShoppingItemList).doesNotContain(shoppingItem)
	}

	@Test
	fun observeTotalPriceSum() = runBlockingTest {
		val shoppingItemOne = ShoppingItem(
			name = "name",
			amount = 2,
			price = 10f,
			imageUrl = "url",
			id = 1
		)

		val shoppingItemTwo = ShoppingItem(
			name = "name",
			amount = 4,
			price = 5.5f,
			imageUrl = "url",
			id = 2
		)

		val shoppingItemThree = ShoppingItem(
			name = "name",
			amount = 0,
			price = 100f,
			imageUrl = "url",
			id = 3
		)

		dao.insertShoppingItem(shoppingItemOne)
		dao.insertShoppingItem(shoppingItemTwo)
		dao.insertShoppingItem(shoppingItemThree)

		val totalPriceSum = dao.observeTotalPrice().getOrAwaitValue()

		assertThat(totalPriceSum).isEqualTo(2 * 10f + 4 * 5.5f)

	}

}