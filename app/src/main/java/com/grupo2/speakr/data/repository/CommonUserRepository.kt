package com.grupo2.speakr.data.repository

import com.grupo2.speakr.data.User
import com.grupo2.speakr.utils.Resource

interface CommonUserRepository {
    suspend fun createUser(user: User) : Resource<Int>
}