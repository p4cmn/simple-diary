package com.artem.records.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artem.records.model.repository.UserRepository
import com.artem.records.viewmodel.LoginViewModel

@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory(
    private val userRepository: UserRepository,
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
