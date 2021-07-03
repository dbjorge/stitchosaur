package net.dbjorge.stitchosaur.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Project::class], version = 1, exportSchema = false)
abstract class StitchosaurDatabase : RoomDatabase() {

    abstract fun projectDao(): ProjectDao

    companion object {
        @Volatile private var singletonInstance: StitchosaurDatabase? = null

        fun getDatabase(context: Context): StitchosaurDatabase {
            return singletonInstance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StitchosaurDatabase::class.java,
                    "net.dbjorge.stitchosaur.database"
                ).build()
                singletonInstance = instance
                instance
            }
        }
    }
}