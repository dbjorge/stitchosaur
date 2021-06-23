package net.dbjorge.stitchosaurus.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey(autoGenerate = true) val id: Int,
    var label: String,
    var counter: Int = 0,
    var notes: String = "")
