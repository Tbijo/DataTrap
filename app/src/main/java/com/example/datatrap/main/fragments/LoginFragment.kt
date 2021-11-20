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
import com.example.datatrap.myenums.EnumTeam
import com.example.datatrap.viewmodels.UserViewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel
    private var team: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        // skontrolovat ci uz nie su iny pouzivatelia aktivny
        // ak su treba nastavit aktivitu na 0
        userViewModel.inactiveAllUsers()

        binding.btnLogin.setOnClickListener {
            logIn()
        }

        binding.rgTeam.setOnCheckedChangeListener { radioGroup, radioButtonId ->
            when (radioButtonId) {
                binding.rbEven.id -> team = EnumTeam.EVEN_TEAM.numTeam
                binding.rbOdd.id -> team = EnumTeam.ODD_TEAM.numTeam
                binding.rbSing.id -> team = EnumTeam.SINGLE.numTeam
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        // skontrolovat ci uz nie su iny pouzivatelia aktivny
        // ak su treba nastavit aktivitu na 0
        userViewModel.inactiveAllUsers()
    }

    private fun logIn() {
        val userName = binding.etUserName.text.toString()
        val pass = binding.etPassword.text.toString()
        if (checkInput(userName, pass, team)) {
            userViewModel.checkUser(userName, pass).observe(viewLifecycleOwner, {
                if (it != null) {

                    userViewModel.setActiveUser(team!!, it)
                    Toast.makeText(requireContext(), "Log in successful.", Toast.LENGTH_SHORT).show()
                    val action = LoginFragmentDirections.actionLoginFragmentToProjectActivity()
                    findNavController().navigate(action)

                } else {
                    Toast.makeText(requireContext(), "Wrong user name or password.", Toast.LENGTH_LONG)
                        .show()
                }
            })
        } else {
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun checkInput(userName: String, pass: String, team: Int?): Boolean {
        return userName.isNotEmpty() && pass.isNotEmpty() && team != null
    }
}