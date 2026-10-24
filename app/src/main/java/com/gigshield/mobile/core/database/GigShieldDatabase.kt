package com.gigshield.mobile.core.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    fun getUserFlow(userId: String): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUser(userId: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("DELETE FROM users")
    suspend fun clearUsers()
}

@Dao
interface ClaimDao {
    @Query("SELECT * FROM claims WHERE userId = :userId ORDER BY createdAt DESC")
    fun getClaimsFlow(userId: String): Flow<List<ClaimEntity>>

    @Query("SELECT * FROM claims WHERE userId = :userId ORDER BY createdAt DESC")
    suspend fun getClaims(userId: String): List<ClaimEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClaim(claim: ClaimEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(claims: List<ClaimEntity>)

    @Query("DELETE FROM claims")
    suspend fun clearClaims()
}

@Database(entities = [UserEntity::class, ClaimEntity::class], version = 1, exportSchema = false)
abstract class GigShieldDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun claimDao(): ClaimDao
}
