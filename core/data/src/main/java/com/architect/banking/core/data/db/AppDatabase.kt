package com.architect.banking.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.architect.banking.core.data.db.dao.SessionDao
import com.architect.banking.core.data.db.entity.SessionEntity

/**
 * Single Room database instance for the entire application.
 *
 * ## Adding a new entity
 * 1. Create the `@Entity` data class in the appropriate module.
 * 2. Create a `@Dao` interface for it.
 * 3. Add the entity to the [entities] list below (one line).
 * 4. Add an abstract fun returning the new DAO (one line).
 * 5. Increment [version] and add a [Migration].
 *
 * Never recreate or re-annotate this class — only extend it.
 */
@Database(
    entities = [SessionEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {

    /** Returns the DAO for session persistence operations. */
    abstract fun sessionDao(): SessionDao
}
