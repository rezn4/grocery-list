package com.example.grocerylist.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(product: Product)

    @Delete
    fun clearProducts(vararg productSuggestion: Product)

    @Query("SELECT * FROM products")
    fun getAllProducts(): LiveData<List<Product>>

    @Query("UPDATE products SET checked = :checked WHERE name = :name")
    fun updateProduct(checked: Boolean, name: String)

    @Query("DELETE from products")
    fun removeAllProducts()

    @Query("DELETE from products WHERE checked = :checked")
    fun removeCheckedProducts(checked: Boolean = true)

    @Query("UPDATE products SET checked = :checked")
    fun updateAllProductsState(checked: Boolean)

    @Query("DELETE from products WHERE id = :id")
    fun removeProductById(id: Int)
}