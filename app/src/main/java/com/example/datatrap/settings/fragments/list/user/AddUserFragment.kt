package com.example.datatrap.settings.fragments.list.user

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentAddUserBinding
import com.example.datatrap.models.User
import com.example.datatrap.myenums.EnumTeam
import com.example.datatrap.viewmodels.UserViewModel

class AddUserFragment : Fragment() {

    private var _binding: FragmentAddUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel

    private var team: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentAddUserBinding.inflate(inflater, container, false)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        binding.rgTeamAdd.setOnCheckedChangeListener { radioGroup, radioButtonId ->
            when(radioButtonId){
                binding.rbEven.id -> team = EnumTeam.EVEN_TEAM.numTeam
                binding.rbOdd.id -> team = EnumTeam.ODD_TEAM.numTeam
                binding.rbSing.id -> team = EnumTeam.SINGLE.numTeam
            }
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_save -> insertUser()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertUser() {
        val userName = binding.etUserNameAdd.text.toString()
        val password = binding.etPasswordAdd.text.toString()

        if (checkInput(userName, password)){
            val user: User = User(0, userName, password, team, 0)
            userViewModel.insertUser(user)

            Toast.makeText(requireContext(), "New user added.", Toast.LENGTH_SHORT).show()

            findNavController().navigateUp()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun checkInput(userName: String, password: String): Boolean{
        return userName.isNotEmpty() && password.isNotEmpty()
    }

}