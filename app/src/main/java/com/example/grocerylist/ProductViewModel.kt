package com.example.grocerylist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.grocerylist.app.App
import com.example.grocerylist.model.Product
import com.example.grocerylist.model.Suggestion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel(application: Application): AndroidViewModel(application) {
    private val repository: ProductRepository
    val allProducts: LiveData<List<Product>>
    val allSuggestions: LiveData<List<Suggestion>>

    init {
        val productDao = App.database.productDao()
        val productSuggestionDao = App.database.productSuggestionDao()
        repository = ProductRepository(productDao, productSuggestionDao)
        allProducts = repository.allProducts
        allSuggestions = repository.allSuggestions
    }

    fun insert(product: Product) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(product)
    }

    fun update(checked: Boolean, name: String) = viewModelScope.launch(Dispatchers.IO){
        repository.update(checked, name)
    }

    fun removeAllProducts() = viewModelScope.launch(Dispatchers.IO){
        repository.removeAllProducts()
    }

    fun removeCheckedProducts() = viewModelScope.launch(Dispatchers.IO){
        repository.removeCheckedProducts()
    }

    fun updateAllProductsState(checked: Boolean) = viewModelScope.launch(Dispatchers.IO){
        repository.updateProductState(checked)
    }

    fun removeProductById(id: Int) = viewModelScope.launch(Dispatchers.IO){
        repository.removeProductById(id)
    }
}