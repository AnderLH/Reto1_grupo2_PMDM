package com.grupo2.speakr.ui.users.password


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.grupo2.speakr.data.PasswordAuth
import com.grupo2.speakr.data.repository.CommonUserRepository
import com.grupo2.speakr.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PasswordViewModel(
    private val userRepository: CommonUserRepository
) : ViewModel() {

    private val _passwords = MutableLiveData<Resource<Void>?>()
    val passwords : MutableLiveData<Resource<Void>?> get() = _passwords

    fun updatePassword(passwordAuth: PasswordAuth){
        viewModelScope.launch {
            _passwords.value = changePassword(passwordAuth)
        }
    }

    private suspend fun changePassword(passwordAuth: PasswordAuth): Resource<Void>? {
        return withContext(Dispatchers.IO) {
            userRepository.changePassword(passwordAuth)
        }
    }


}
class PasswordViewModelFactory(
    private val userRepository: CommonUserRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return PasswordViewModel(userRepository) as T
    }
}
