package com.artem.records.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artem.records.model.repository.UserRepository
import com.artem.records.utils.PasswordUtils
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository,
): ViewModel() {

    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> = _loginState

    private val _navigationToRegistration = MutableLiveData<Boolean>()
    val navigationToRegistration: LiveData<Boolean> get() = _navigationToRegistration

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    sealed class LoginState {
        data object Idle : LoginState()
        data class Success(val userId: Long) : LoginState()
        data class Error(val message: String) : LoginState()
    }

    private fun login(email: String, password: String) {
        viewModelScope.launch {
            val user = userRepository.getUserByEmail(email)
            if (user != null && PasswordUtils.checkPassword(password, user.passwordHash)) {
                _loginState.value = LoginState.Success(user.userId)
            } else {
                _loginState.value = LoginState.Error("Invalid email or password")
            }
        }
    }

    fun onLoginButtonClicked() {
        val emailValue = email.value ?: ""
        val passwordValue = password.value ?: ""
        login(emailValue, passwordValue)
    }

    fun resetLoginState() {
        _loginState.value = LoginState.Idle
    }

    fun onRegistrationButtonClicked() {
        _navigationToRegistration.value = true
    }

    fun navigatedToRegistration() {
        _navigationToRegistration.value = false
    }

}
