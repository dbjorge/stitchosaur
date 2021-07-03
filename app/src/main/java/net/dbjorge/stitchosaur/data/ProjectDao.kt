package net.dbjorge.stitchosaur.data

import androidx.room.*

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects ORDER BY id ASC")
    suspend fun getAll(): List<Project>

    @Query("SELECT * FROM projects WHERE id = :projectId LIMIT 1")
    suspend fun getById(projectId: Int): Project

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(project: Project)

    @Update
    suspend fun update(project: Project)

    @Delete
    suspend fun delete(project: Project)
}