package net.dbjorge.stitchosaurus.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects ORDER BY id ASC")
    fun getAll(): Flow<List<Project>>

    @Query("SELECT * FROM projects WHERE id = :projectId LIMIT 1")
    fun getById(projectId: Int): Flow<Project>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(project: Project)

    @Update
    suspend fun update(project: Project)

    @Delete
    suspend fun delete(project: Project)
}