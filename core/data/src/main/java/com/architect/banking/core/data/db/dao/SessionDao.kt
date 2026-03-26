package com.architect.banking.core.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.architect.banking.core.data.db.entity.SessionEntity
import kotlinx.coroutines.flow.Flow

/**
 * Room DAO for [SessionEntity] operations.
 * All DB access goes through this interface — never call it directly from ViewModels.
 */
@Dao
interface SessionDao {

    /**
     * Inserts or replaces the session for [entity.userId].
     * Replaces any existing session for the same user.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: SessionEntity)

    /**
     * Returns the most recently created active session, or null if none exists.
     */
    @Query("SELECT * FROM session_table ORDER BY createdAt DESC LIMIT 1")
    suspend fun getActiveSession(): SessionEntity?

    /**
     * Observes the active session reactively. Emits null when signed out.
     */
    @Query("SELECT * FROM session_table ORDER BY createdAt DESC LIMIT 1")
    fun observeActiveSession(): Flow<SessionEntity?>

    /**
     * Returns the session for a specific [userId], or null if not found.
     */
    @Query("SELECT * FROM session_table WHERE userId = :userId")
    suspend fun getByUserId(userId: String): SessionEntity?

    /**
     * Deletes all sessions — used on logout or token revocation.
     */
    @Query("DELETE FROM session_table")
    suspend fun deleteAll()
}
