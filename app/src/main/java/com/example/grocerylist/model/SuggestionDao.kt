package com.example.grocerylist.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SuggestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(suggestion: Suggestion)

    @Delete
    fun clearProductSuggestions(vararg suggestion: Suggestion)

    @Query("SELECT * FROM product_suggestion_list")
    fun getAllProductSuggestions(): LiveData<List<Suggestion>>
}