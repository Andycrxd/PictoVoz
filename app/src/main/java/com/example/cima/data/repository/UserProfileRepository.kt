package com.example.cima.data.repository

import com.example.cima.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository {
    fun observeProfiles(): Flow<List<UserProfile>>
    suspend fun createProfile(name: String, avatarResId: Int): Int
}

class RoomUserProfileRepository(
    private val dao: UserProfileDao
) : UserProfileRepository {
    override fun observeProfiles(): Flow<List<UserProfile>> = dao.observeProfiles()

    override suspend fun createProfile(name: String, avatarResId: Int): Int =
        dao.insert(UserProfile(nombre = name.trim(), avatarResId = avatarResId)).toInt()
}
