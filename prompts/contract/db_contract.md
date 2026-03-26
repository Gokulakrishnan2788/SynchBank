# Database Contract
# Room DB rules for all features

## AppDatabase (in :core:data — DO NOT regenerate)
Only add new DAO references here when creating new entities
Never recreate the AppDatabase class

## Adding a new Entity
1. Create XEntity.kt in :feature:x or :core:data
2. Create XDao.kt
3. Add entity to @Database entities list in AppDatabase.kt (one line change only)
4. Add dao abstract function to AppDatabase.kt (one line change only)
5. Increment database version and add migration

## Entity Pattern
@Entity(tableName = "table_name")
data class XEntity(
    @PrimaryKey val id: String,
    val field: String,
    val createdAt: Long = System.currentTimeMillis()
)

## DAO Pattern
@Dao
interface XDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: XEntity)

    @Query("SELECT * FROM table_name WHERE id = :id")
    suspend fun getById(id: String): XEntity?

    @Query("SELECT * FROM table_name ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<XEntity>>

    @Delete
    suspend fun delete(entity: XEntity)

    @Query("DELETE FROM table_name")
    suspend fun deleteAll()
}

## Existing Tables (DO NOT recreate)
session_table: userId, token, refreshToken, expiresAt → SessionEntity, SessionDao

## Migration Pattern
val MIGRATION_X_Y = object : Migration(X, Y) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE ... ADD COLUMN ...")
    }
}

## Rules
- No raw SQL except in @Query — use typed DAOs
- Use Flow for reactive queries, suspend for one-shot operations
- All DB operations via Repository — never call DAO from ViewModel
- Entity ↔ Domain model mapping via mapper functions, never mix types
