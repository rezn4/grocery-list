package com.example.grocerylist.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_suggestion_list")
data class Suggestion(
    @ColumnInfo (name = "name_rus")
    val name_rus: String,
    @PrimaryKey val id: Int){

    override fun toString(): String {
        return name_rus
    }
}

