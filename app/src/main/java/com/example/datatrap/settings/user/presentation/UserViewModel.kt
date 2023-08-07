package com.example.datatrap.settings.user.presentation

import android.app.AlertDialog
import android.widget.Toast
import androidx.lifecycle.*
import com.example.datatrap.R
import com.example.datatrap.core.data.pref.PrefViewModel
import com.example.datatrap.settings.user.data.User
import com.example.datatrap.settings.user.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val userList: LiveData<List<User>> = userRepository.userList
    var userId = MutableLiveData<Long?>()
//    private val userViewModel: UserViewModel by viewModels()
//    private val prefViewModel: PrefViewModel by viewModels()
    private lateinit var userList: List<User>

    fun insertUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.insertUser(user)
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateUser(user)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.deleteUser(user)
        }
    }

    fun getActiveUser(userId: Long): LiveData<User> {
        return userRepository.getActiveUser(userId)
    }

    fun checkUser(userName: String, password: String) {
        // ak volam nieco cez suspend a ukladam to do premennej MutableLiveData
        // cez .value nesmie sa pouzit Dispatchers.IO chyba: Can not use .value on Background Thread
        viewModelScope.launch {
            val value = userRepository.checkUser(userName, password)
            userId.value = value
        }
    }

    init {
        userViewModel.userList.observe(viewLifecycleOwner) {
            adapter.setData(it)
            userList = it
        }

        adapter.setOnItemClickListener(object : UserRecyclerView.MyClickListener {
            override fun useClickListener(position: Int) {
                // nastavit vybraneho pouzivatela ako aktivneho
                // nepovolit manipulaciu s rootom
                if (userList[position].userName != "root" && userList[position].password != "toor"){
                    val user: User = userList[position]
                    prefViewModel.saveUserIdPref(user.userId)
                    Toast.makeText(requireContext(), "Selected user is now active.", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(), "Unable to access root.", Toast.LENGTH_LONG).show()
                }
            }

            override fun useLongClickListener(position: Int) {
                // update pouzivatela
                // nepovolit manipulaciu s rootom
                val user: User = userList[position]
                if (user.userName != "root" && user.password != "toor"){
                    val action = ListUsersFragmentDirections.actionListUsersFragmentToUpdateUserFragment(user)
                    findNavController().navigate(action)
                }else{
                    Toast.makeText(requireContext(), "Unable to access root.", Toast.LENGTH_LONG).show()
                }
            }
        })

        binding.addUserFloatButton.setOnClickListener {
            val action = ListUsersFragmentDirections.actionListUsersFragmentToAddUserFragment()
            findNavController().navigate(action)
        }
    }

    fun onEvent(event: UserScreenEvent) {
        when(event) {
            is UserScreenEvent.OnDeleteClick -> TODO()
            is UserScreenEvent.OnInsertClick -> TODO()
            is UserScreenEvent.OnItemClick -> TODO()
            is UserScreenEvent.OnSaveUser -> TODO()
        }
    }

    private fun insertUser() {
        val userName = binding.etUserNameAdd.text.toString()
        val password = binding.etPasswordAdd.text.toString()

        if (checkInput(userName, password)){
            val user: User = User(0, userName, password)
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

            userViewModel.updateUser(user)

            Toast.makeText(requireContext(), "User updated.", Toast.LENGTH_SHORT).show()

            findNavController().navigateUp()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun initUserValuseToView() {
        binding.etUserNameUpdate.setText(args.user.userName)
        binding.etPasswordUpdate.setText(args.user.password)
    }

}