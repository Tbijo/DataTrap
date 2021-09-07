package com.example.datatrap.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentLoginBinding
import com.example.datatrap.models.User
import com.example.datatrap.viewmodels.UserViewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel
    private var team: Int? = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        binding.btnLogin.setOnClickListener {
            logIn()
        }

        binding.rgTeam.setOnCheckedChangeListener { radioGroup, radioButtonId ->
            team = when(radioButtonId){
                binding.rbEven.id -> 0
                binding.rbOdd.id -> 1
                else -> null
            }
        }

        return binding.root
    }

    private fun logIn() {
        val userName = binding.etUserName.text.toString()
        val pass = binding.etPassword.text.toString()
        if (checkInput(userName, pass, team)){
            checkAuthority(userName, pass, team)
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun checkAuthority(userName: String, pass: String, team: Int?) {
        val user: User? = userViewModel.checkUser(userName, pass).value
        if (user != null){
            user.team = team!!
            userViewModel.updateUser(user)
            Toast.makeText(requireContext(), "Log in successful.", Toast.LENGTH_SHORT).show()
            val action = LoginFragmentDirections.actionLoginFragmentToProjectActivity()
            findNavController().navigate(action)
        }else{
            Toast.makeText(requireContext(), "Wrong user name or password.", Toast.LENGTH_LONG).show()
        }
    }

    private fun checkInput(userName: String, pass: String, team: Int?): Boolean {
        return userName.isNotEmpty() && pass.isNotEmpty() && team.toString().isNotEmpty()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}