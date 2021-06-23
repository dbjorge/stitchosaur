package net.dbjorge.stitchosaurus

import android.app.Application
import net.dbjorge.stitchosaurus.data.StitchosaurusDatabase

class StitchosaurusApplication : Application() {
    val database by lazy { StitchosaurusDatabase.getDatabase(this) }
}