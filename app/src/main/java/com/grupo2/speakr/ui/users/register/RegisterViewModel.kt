package com.grupo2.speakr.ui.users.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.grupo2.speakr.data.User
import com.grupo2.speakr.data.repository.CommonUserRepository
import com.grupo2.speakr.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel(
    private val userRepository: CommonUserRepository
) : ViewModel() {

    private val _created = MutableLiveData<Resource<Int>>()
    val created : LiveData<Resource<Int>> get() = _created
    fun registerUser(user: User) {
        viewModelScope.launch {
            _created.value =  createNewUser(user)
        }
    }
    suspend fun createNewUser(user: User): Resource<Int> {
        return withContext(Dispatchers.IO) {
            userRepository.createUser(user)
        }
    }
}
class RegisterViewModelFactory(
    private val userRepository: CommonUserRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return RegisterViewModel(userRepository) as T
    }
}