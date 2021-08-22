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
import com.example.datatrap.viewmodels.LocalityViewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var localityViewModel: LocalityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        localityViewModel = ViewModelProvider(this).get(LocalityViewModel::class.java)

        binding.btnLogin.setOnClickListener {
            logIn()
        }

        return binding.root
    }

    private fun logIn() {
        val userName = binding.etUserName.text.toString()
        val pass = binding.etPassword.text.toString()
        if (checkInput(userName, pass)){
            if (checkAuthority(userName, pass)){
                Toast.makeText(requireContext(), "Log in successful.", Toast.LENGTH_SHORT).show()
                val action = LoginFragmentDirections.actionLoginFragmentToProjectActivity()
                findNavController().navigate(action)
            }else{
                Toast.makeText(requireContext(), "Wrong user name of password.", Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun checkAuthority(userName: String, pass: String): Boolean {
        return userName == "User" && pass == "Password"
    }

    private fun checkInput(userName: String, pass: String): Boolean {
        return userName.isNotEmpty() && pass.isNotEmpty()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}