package net.dbjorge.stitchosaurus.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Project::class], version = 1, exportSchema = false)
abstract class StitchosaurusDatabase : RoomDatabase() {

    abstract fun projectDao(): ProjectDao

    companion object {
        @Volatile private var SINGLETON_INSTANCE: StitchosaurusDatabase? = null

        fun getDatabase(context: Context): StitchosaurusDatabase {
            return SINGLETON_INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StitchosaurusDatabase::class.java,
                    "net.dbjorge.stitchosaurus.database"
                ).build()
                SINGLETON_INSTANCE = instance
                instance
            }
        }
    }
}