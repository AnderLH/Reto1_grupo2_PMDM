package com.grupo2.speakr.data.repository

import com.grupo2.speakr.data.LoginUser
import com.grupo2.speakr.data.MailAuth
import com.grupo2.speakr.data.PasswordAuth
import com.grupo2.speakr.data.User
import com.grupo2.speakr.utils.Resource

interface CommonUserRepository {
    suspend fun createUser(user: User) : Resource<Int>

    suspend fun loginUser(user: LoginUser) : Resource<AuthenticationResponse>

    suspend fun changePassword(passwordAuth : PasswordAuth) : Resource<Void>

    suspend fun changeEmail(mailAuth : MailAuth) : Resource<Void>
}