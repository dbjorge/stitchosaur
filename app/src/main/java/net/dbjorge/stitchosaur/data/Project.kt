package net.dbjorge.stitchosaur.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class Project(
    var label: String,
    var counter: Int = 0,
    var notes: String = "",

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
