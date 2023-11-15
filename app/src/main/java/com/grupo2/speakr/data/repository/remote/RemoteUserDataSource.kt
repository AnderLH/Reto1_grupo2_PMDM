package com.grupo2.speakr.data.repository.remote

import com.grupo2.speakr.data.LoginUser
import com.grupo2.speakr.data.MailChange
import com.grupo2.speakr.data.PasswordAuth
import com.grupo2.speakr.data.User
import com.grupo2.speakr.data.repository.CommonUserRepository

class RemoteUserDataSource: BaseDataSource(), CommonUserRepository {
    override suspend fun createUser(user: User) = getResult {
        RetrofitClient.apiInterface.createUser(user)
    }

    override suspend fun loginUser(user: LoginUser) = getResult {
        RetrofitClient.apiInterface.loginUser(user)
    }
    override suspend fun changePassword(passwordAuth: PasswordAuth) = getResult {
        RetrofitClient.apiInterface.changePassword(passwordAuth)
    }

    override suspend fun changeEmail(mailChange: MailChange) = getResult {
        RetrofitClient.apiInterface.changeEmail(mailChange)
    }

}