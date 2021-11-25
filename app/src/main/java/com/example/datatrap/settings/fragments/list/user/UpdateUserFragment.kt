package com.example.datatrap.settings.fragments.list.user

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentUpdateUserBinding
import com.example.datatrap.models.User
import com.example.datatrap.myenums.EnumTeam
import com.example.datatrap.viewmodels.UserViewModel

class UpdateUserFragment : Fragment() {

    private var _binding: FragmentUpdateUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel
    private val args by navArgs<UpdateUserFragmentArgs>()

    private var team: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentUpdateUserBinding.inflate(inflater, container, false)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        team = args.user.team

        initUserValuseToView()

        binding.rgTeamUpdate.setOnCheckedChangeListener { _, radioButtonId ->
            when(radioButtonId){
                binding.rbEven.id -> team =  EnumTeam.EVEN_TEAM.numTeam
                binding.rbOdd.id -> team =  EnumTeam.ODD_TEAM.numTeam
                binding.rbSing.id -> team =  EnumTeam.SINGLE.numTeam
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
        inflater.inflate(R.menu.update_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_save -> updateUser()
            R.id.menu_delete -> deleteUser()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteUser() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->

            userViewModel.deleteUser(args.user)

            Toast.makeText(requireContext(),"User deleted.", Toast.LENGTH_LONG).show()

            findNavController().navigateUp()
        }
            .setNegativeButton("No"){_, _ -> }
            .setTitle("Delete user?")
            .setMessage("Are you sure you want to delete this user?")
            .create().show()
    }

    private fun updateUser() {
        val userName = binding.etUserNameUpdate.text.toString()
        val password = binding.etPasswordUpdate.text.toString()

        if (checkInput(userName, password)){
            val user: User = args.user.copy()
            user.userName = userName
            user.password = password
            user.team = team

            userViewModel.updateUser(user)

            Toast.makeText(requireContext(), "User updated.", Toast.LENGTH_SHORT).show()

            findNavController().navigateUp()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun checkInput(userName: String, password: String): Boolean{
        return userName.isNotEmpty() && password.isNotEmpty()
    }

    private fun initUserValuseToView() {
        binding.etUserNameUpdate.setText(args.user.userName)
        binding.etPasswordUpdate.setText(args.user.password)

        when(args.user.team){
            EnumTeam.EVEN_TEAM.numTeam -> binding.rbEven.isChecked = true
            EnumTeam.ODD_TEAM.numTeam -> binding.rbOdd.isChecked = true
            EnumTeam.SINGLE.numTeam -> binding.rbSing.isChecked = true
        }
    }

}