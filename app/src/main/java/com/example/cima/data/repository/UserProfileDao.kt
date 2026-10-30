package com.example.cima.data.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.cima.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profiles ORDER BY nombre COLLATE NOCASE")
    fun observeProfiles(): Flow<List<UserProfile>>

    @Insert
    suspend fun insert(profile: UserProfile): Long

    @Query("DELETE FROM user_profiles WHERE id = :profileId")
    suspend fun deleteById(profileId: Int)
}
