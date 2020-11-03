package com.okan.market

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

class ResourceCompareTest {

	/**
	 * All test cases run independently from each other.
	 * However here it is not possible because both test
	 * cases use the same variable resourceCompare
	 */

	private lateinit var resourceCompare: ResourceCompare


	/**
	 * sets up the variables before all test cases
	 */
	@Before
	fun setup() {
		resourceCompare = ResourceCompare()
	}

	/**
	 * removes unnecessary variables after test finishes
	 * for example you can close your room database
	 * connection here
	 */
	@After
	fun tearDown() {

	}

	@Test
	fun stringResourceSameAsGivenString_returnsTrue() {
		val context = ApplicationProvider.getApplicationContext<Context>()
		val result = resourceCompare.isEqual(context, R.string.app_name, "Market")
		assertThat(result).isTrue()
	}

	@Test
	fun stringResourceDifferentAsGivenString_returnsFalse() {
		val context = ApplicationProvider.getApplicationContext<Context>()
		val result = resourceCompare.isEqual(context, R.string.app_name, "hello")
		assertThat(result).isFalse()
	}

}