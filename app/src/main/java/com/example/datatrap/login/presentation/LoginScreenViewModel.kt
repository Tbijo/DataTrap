package com.example.datatrap.login.presentation

import android.widget.Toast
import com.example.datatrap.R
import com.example.datatrap.core.util.EnumTeam
import java.util.Calendar

class LoginScreenViewModel {

//    private val userViewModel: UserViewModel by viewModels()
//    private val prefViewModel: PrefViewModel by viewModels()

    private var team: Int? = null

    init {
        prefViewModel.readLastSyncDatePref.observe(viewLifecycleOwner) {
            if (it == -1L) prefViewModel.saveLastSyncDatePref(Calendar.getInstance().time.time)
        }

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

        userViewModel.userId.observe(viewLifecycleOwner) {
            if (it != null) {
                prefViewModel.saveUserIdPref(it)
                prefViewModel.saveUserTeamPref(team!!)
                Toast.makeText(requireContext(), "Log in successful.", Toast.LENGTH_SHORT).show()
                val action = LoginFragmentDirections.actionLoginFragmentToProjectActivity()
                findNavController().navigate(action)

            } else {
                Toast.makeText(requireContext(), "Wrong user name or password.", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    fun onEvent(event: LoginScreenEvent) {
        when(event) {
            LoginScreenEvent.LogIn -> TODO()
            is LoginScreenEvent.OnPasswordChanged -> TODO()
            is LoginScreenEvent.OnSelectTeam -> TODO()
            is LoginScreenEvent.OnUserNameChanged -> TODO()
        }
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