package com.example.grocerylist.app

import android.app.Application
import androidx.room.Room
import com.example.grocerylist.model.ProductsDatabase
import java.io.File
import java.io.FileOutputStream

class App: Application() {

    companion object {
        lateinit var database: ProductsDatabase
        const val DB_NAME = "products.db"
    }

    override fun onCreate() {
        super.onCreate()
        copyDatabaseFromAssets()
        database = Room.databaseBuilder(this, ProductsDatabase::class.java, DB_NAME).
            fallbackToDestructiveMigration().
            build()
    }

    private fun copyDatabaseFromAssets(){
        val dbPath: File = this.getDatabasePath(DB_NAME)

        if (dbPath.exists()){
            return
        }

        val inputStream = this.assets.open(DB_NAME)
        val os = FileOutputStream(dbPath)

        val buffer = ByteArray(1024)

        while (inputStream.read(buffer) > 0) {
            os.write(buffer)
        }

        os.flush()
        os.close()
    }
}