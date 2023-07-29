package com.example.datatrap.login

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
import com.example.datatrap.core.util.EnumTeam
import com.example.datatrap.settings.user.presentation.UserViewModel
import com.example.datatrap.core.data.pref.PrefViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by viewModels()
    private val prefViewModel: PrefViewModel by viewModels()
    private var team: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

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

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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