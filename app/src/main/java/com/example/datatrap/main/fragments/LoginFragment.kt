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
import com.example.datatrap.myenums.EnumTeam
import com.example.datatrap.viewmodels.UserViewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel
    private var team: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        binding.btnLogin.setOnClickListener {
            logIn()
        }

        binding.rgTeam.setOnCheckedChangeListener { radioGroup, radioButtonId ->
            when(radioButtonId){
                binding.rbEven.id -> team = EnumTeam.EVEN_TEAM.numTeam
                binding.rbOdd.id -> team =  EnumTeam.ODD_TEAM.numTeam
                binding.rbSing.id -> team = EnumTeam.SINGLE.numTeam
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
        if (checkInput(userName, pass, team)){
            checkAuthority(userName, pass, team)
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun checkAuthority(userName: String, pass: String, team: Int) {
        val user: User? = userViewModel.checkUser(userName, pass).value
        if (user != null){
            // skontrolovat ci uz nie su iny pouzivatelia aktivny
            // ak su treba nastavit aktivitu na 0
            inactiveAllUsers()
            // nastavit team
            user.team = team
            // tento pouzivatel je teraz aktivny vzdy moze byt len jeden
            user.isActive = 1
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

    private fun inactiveAllUsers(){
        val activeUserList: List<User>? = userViewModel.getActiveUsers().value
        if (activeUserList?.isNotEmpty() == true){
            activeUserList.forEach {
                it.isActive = 0
                userViewModel.updateUser(it)
            }
        }
    }
}