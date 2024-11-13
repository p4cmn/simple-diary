package com.artem.records.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.artem.records.databinding.FragmentRegistrationBinding
import com.artem.records.factory.RegistrationViewModelFactory
import com.artem.records.model.database.DiaryDatabase
import com.artem.records.model.repository.UserRepository
import com.artem.records.viewmodel.RegistrationViewModel
import com.artem.records.R

class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        val view = binding.root

        val application = requireNotNull(this.activity).application
        val dao = DiaryDatabase.getInstance(application).userDao
        val userRepository = UserRepository.getInstance(dao)
        val viewModelFactory = RegistrationViewModelFactory(userRepository)
        val viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[RegistrationViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.navigationToLogin.observe(viewLifecycleOwner) { shouldNavigate ->
            if(shouldNavigate) {
                findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
                viewModel.navigatedToLogin()
            }
        }

        viewModel.registrationState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is RegistrationViewModel.RegistrationState.Success -> {
                    findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
                    viewModel.resetRegistrationState()
                }
                is RegistrationViewModel.RegistrationState.Error -> {
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                    viewModel.resetRegistrationState()
                }
                is RegistrationViewModel.RegistrationState.Idle -> {}
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
