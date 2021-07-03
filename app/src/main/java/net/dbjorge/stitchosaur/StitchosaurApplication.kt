package net.dbjorge.stitchosaur

import android.app.Application
import net.dbjorge.stitchosaur.data.StitchosaurDatabase

class StitchosaurApplication : Application() {
    val database by lazy { StitchosaurDatabase.getDatabase(this) }
}