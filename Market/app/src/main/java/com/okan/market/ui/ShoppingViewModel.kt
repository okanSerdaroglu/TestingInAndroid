package com.okan.market.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okan.market.data.local.ShoppingItem
import com.okan.market.data.remote.responses.ImageResponse
import com.okan.market.other.Constants
import com.okan.market.other.Event
import com.okan.market.other.Resource
import com.okan.market.repositories.ShoppingRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class ShoppingViewModel
@ViewModelInject
constructor(
        private val repository: ShoppingRepository
) : ViewModel() {

    val shoppingItems = repository.observeAllShoppingItems()

    val totalPrice = repository.observeTotalPrice()

    private val _images = MutableLiveData<Event<Resource<ImageResponse>>>()
    val images: LiveData<Event<Resource<ImageResponse>>> = _images

    private val _curImageUrl = MutableLiveData<String>()
    val curImageUrl: LiveData<String> = _curImageUrl

    private val _insertShoppingItemStatus = MutableLiveData<Event<Resource<ShoppingItem>>>()
    val insertShoppingItemStatus: LiveData<Event<Resource<ShoppingItem>>> = _insertShoppingItemStatus

    private fun setCurrentImageUrl(url: String) {
        _curImageUrl.postValue(url)
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.deleteShoppingItem(shoppingItem = shoppingItem)
    }

    fun insertShoppingItemIntoDB(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.insertShoppingItem(shoppingItem = shoppingItem)
    }

    fun insertShoppingItem(
            name: String,
            amount: String,
            riceString: String
    ) {

        if (name.isEmpty() || amount.isEmpty() || riceString.isEmpty()) {
            _insertShoppingItemStatus.postValue(Event(Resource.error("The fields must not be empty", null)))
            return
        }

        if (name.length > Constants.MAX_NAME_LENGTH) {
            _insertShoppingItemStatus.postValue(Event(Resource.error("The name of the item"
                    + "must not exceed ${Constants.MAX_NAME_LENGTH} characters", null)))
            return
        }

        if (riceString.length > Constants.MAX_PRICE_LENGTH) {
            _insertShoppingItemStatus.postValue(Event(Resource.error("The price of the item"
                    + "must not exceed ${Constants.MAX_PRICE_LENGTH} characters", null)))
            return
        }

        val amount = try {
            amount.toInt()
        } catch (e: Exception) {
            _insertShoppingItemStatus.postValue(Event(Resource.error(
                    msg = "Please enter a valid amount",
                    data = null
            )))
        }

        val shoppingItem = ShoppingItem(
                name = name,
                amount as Int,
                price = riceString.toFloat(),
                imageUrl = _curImageUrl.value ?: ""
        )

        insertShoppingItemIntoDB(shoppingItem)
        setCurrentImageUrl("")
        _insertShoppingItemStatus.postValue(Event(Resource.success(shoppingItem)))

    }

    fun searchForImage(
            imageQuery: String
    ) {
        if (imageQuery.isEmpty()) {
            return
        }

        _images.value = Event(Resource.loading(null))

        viewModelScope.launch {
            val response = repository.searchForImage(imageQuery)
            _images.value = Event(response)
        }

    }

}