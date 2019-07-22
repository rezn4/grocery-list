package com.example.grocerylist.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Product::class, Suggestion::class], version = 6, exportSchema = false)
abstract class ProductsDatabase: RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun productSuggestionDao(): SuggestionDao
}