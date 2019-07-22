package com.example.grocerylist

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.grocerylist.model.Product
import com.example.grocerylist.model.ProductDao
import com.example.grocerylist.model.Suggestion
import com.example.grocerylist.model.SuggestionDao

class ProductRepository(private val productDao: ProductDao,
                        suggestionDao: SuggestionDao) {
    val allProducts: LiveData<List<Product>> = productDao.getAllProducts()
    val allSuggestions: LiveData<List<Suggestion>> = suggestionDao.getAllProductSuggestions()

    @WorkerThread
    fun insert(product: Product){
        productDao.insert(product)
    }

    @WorkerThread
    fun update(checked: Boolean, name: String){
        productDao.updateProduct(checked, name)
    }

    @WorkerThread
    fun removeAllProducts(){
        productDao.removeAllProducts()
    }

    @WorkerThread
    fun removeCheckedProducts(){
        productDao.removeCheckedProducts()
    }

    @WorkerThread
    fun updateProductState(checked: Boolean){
        productDao.updateAllProductsState(checked)
    }

    @WorkerThread
    fun removeProductById(id: Int){
        productDao.removeProductById(id)
    }
}