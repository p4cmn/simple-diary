package com.artem.records.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artem.records.model.repository.UserRepository
import com.artem.records.viewmodel.RegistrationViewModel

@Suppress("UNCHECKED_CAST")
class RegistrationViewModelFactory(
    private val repository: UserRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(RegistrationViewModel::class.java)) {
            return RegistrationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
