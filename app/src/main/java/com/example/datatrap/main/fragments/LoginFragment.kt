package com.example.datatrap.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentLoginBinding
import com.example.datatrap.myenums.EnumTeam
import com.example.datatrap.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by viewModels()
    private var team: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        // skontrolovat ci uz nie su iny pouzivatelia aktivny
        // ak su treba nastavit aktivitu na 0
        userViewModel.inactiveAllUsers()

        binding.btnLogin.setOnClickListener {
            logIn()
        }

        binding.rgTeam.setOnCheckedChangeListener { _, radioButtonId ->
            when (radioButtonId) {
                binding.rbEven.id -> team = EnumTeam.EVEN_TEAM.numTeam
                binding.rbOdd.id -> team = EnumTeam.ODD_TEAM.numTeam
                binding.rbSing.id -> team = EnumTeam.SINGLE.numTeam
            }
        }

        // overenie pouzivatela
        // neda sa urobit priamo lebo ak vykonam zmenu UPDATE v tomto pripade
        // observer sa bude volat tak funguje Room

        // ak volam nieco cez suspend a ukladam to do premennej MutableLiveData
        // cez .value nesmie sa pouzit Dispatchers.IO chyba: Can not use .value on Background Thread
        userViewModel.userId.observe(viewLifecycleOwner) {
            if (it != null) {
                userViewModel.setActiveUser(team!!, it)
                Toast.makeText(requireContext(), "Log in successful.", Toast.LENGTH_SHORT).show()
                val action = LoginFragmentDirections.actionLoginFragmentToProjectActivity()
                findNavController().navigate(action)

            } else {
                Toast.makeText(requireContext(), "Wrong user name or password.", Toast.LENGTH_LONG)
                    .show()
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
            userViewModel.checkUser(userName, pass)
        } else {
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun checkInput(userName: String, pass: String, team: Int?): Boolean {
        return userName.isNotEmpty() && pass.isNotEmpty() && team != null
    }
}