package com.grupo2.speakr.ui.users.email

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.grupo2.speakr.data.MailAuth
import com.grupo2.speakr.data.PasswordAuth
import com.grupo2.speakr.data.User
import com.grupo2.speakr.data.repository.CommonUserRepository
import com.grupo2.speakr.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EmailViewModel(
    private val userRepository: CommonUserRepository
) : ViewModel() {

    private val _emails = MutableLiveData<Resource<Void>?>()
    val emails : MutableLiveData<Resource<Void>?> get() = _emails

    fun updateMail(emailAuth: MailAuth){
        viewModelScope.launch {
            _emails.value = changeEmail(emailAuth)
        }
    }

    private suspend fun changeEmail(emailAuth: MailAuth): Resource<Void>? {
        return withContext(Dispatchers.IO) {
            userRepository.changeEmail(emailAuth)
        }
    }


}
class EmailViewModelFactory(
    private val userRepository: CommonUserRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return EmailViewModel(userRepository) as T
    }
}
