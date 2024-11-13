package com.artem.records.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artem.records.model.entity.User
import com.artem.records.model.repository.UserRepository
import com.artem.records.utils.PasswordUtils
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val userRepository: UserRepository
): ViewModel() {

    private val _registrationState = MutableLiveData<RegistrationState>()
    val registrationState: LiveData<RegistrationState> = _registrationState

    private val _navigationToLogin = MutableLiveData<Boolean>()
    val navigationToLogin: LiveData<Boolean> get() = _navigationToLogin

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val confirmPassword = MutableLiveData<String>()

    sealed class RegistrationState {
        data object Idle : RegistrationState()
        data object Success : RegistrationState()
        data class Error(val message: String) : RegistrationState()
    }

    private fun register(email: String, password: String) {
        viewModelScope.launch {
            val user = userRepository.getUserByEmail(email)
            if (user == null) {
                val hashedPassword = PasswordUtils.hashPassword(password)
                val newUser = User(email = email, passwordHash = hashedPassword)
                userRepository.registerUser(newUser)
                _registrationState.value = RegistrationState.Success
            } else {
                _registrationState.value = RegistrationState.Error("User already exists")
            }
        }
    }

    fun onRegistrationButtonClicked() {
        val emailValue = email.value ?: ""
        val passwordValue = password.value ?: ""
        val confirmPasswordValue = confirmPassword.value ?: ""

        if (passwordValue != confirmPasswordValue) {
            _registrationState.value = RegistrationState.Error("Passwords do not match")
        } else {
            register(emailValue, passwordValue)
        }
    }

    fun resetRegistrationState() {
        _registrationState.value = RegistrationState.Idle
    }

    fun onLoginButtonClicked() {
        _navigationToLogin.value = true
    }

    fun navigatedToLogin() {
        _navigationToLogin.value = false
    }

}
