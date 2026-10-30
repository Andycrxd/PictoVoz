package com.example.cima.data.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cima.data.model.UserProfile

@Database(
    entities = [UserProfile::class],
    version = 1,
    exportSchema = false
)
abstract class PictoVozDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile
        private var instance: PictoVozDatabase? = null

        fun getInstance(context: Context): PictoVozDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                PictoVozDatabase::class.java,
                "pictovoz.db"
            ).build().also { instance = it }
        }
    }
}
