package com.okan.market.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okan.market.data.local.ShoppingItem
import com.okan.market.data.remote.responses.ImageResponse
import com.okan.market.other.Event
import com.okan.market.other.Resource
import com.okan.market.repositories.ShoppingRepository
import kotlinx.coroutines.launch

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

    fun setCurrentImageUrl(url: String) {
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

    }

    fun searchForImage(
            imageQuery: String
    ) {

    }

}