package com.example.grocerylist.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(val name: String,
                   val checked: Boolean,
                   @PrimaryKey (autoGenerate = true)
                   val id: Int = 0)