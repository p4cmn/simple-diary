package com.artem.records.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.artem.records.databinding.FragmentLoginBinding
import com.artem.records.factory.LoginViewModelFactory
import com.artem.records.model.database.DiaryDatabase
import com.artem.records.model.repository.UserRepository
import com.artem.records.viewmodel.LoginViewModel
import com.artem.records.R

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        val application = requireNotNull(this.activity).application
        val dao = DiaryDatabase.getInstance(application).userDao
        val userRepository = UserRepository.getInstance(dao)

        val viewModelFactory = LoginViewModelFactory(userRepository)
        val viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[LoginViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.navigationToRegistration.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate) {
                findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
                viewModel.navigatedToRegistration()
            }
        }

        viewModel.loginState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LoginViewModel.LoginState.Success -> {
                    saveUserIdToPreferences(state.userId)
                    findNavController().navigate(R.id.action_loginFragment_to_noteListFragment)
                    viewModel.resetLoginState()
                }
                is LoginViewModel.LoginState.Error -> {
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                    viewModel.resetLoginState()
                }
                is LoginViewModel.LoginState.Idle -> {}
            }
        }

        return view
    }

    private fun saveUserIdToPreferences(userId: Long) {
        val sharedPreferences = requireActivity()
            .getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putLong("user_id", userId).apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
