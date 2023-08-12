package com.example.datatrap.settings.user.presentation

import android.app.AlertDialog
import android.widget.Toast
import androidx.lifecycle.*
import com.example.datatrap.R
import com.example.datatrap.settings.user.data.UserEntity
import com.example.datatrap.settings.user.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val userEntityList: LiveData<List<UserEntity>> = userRepository.userEntityList
    var userId = MutableLiveData<Long?>()
//    private val userViewModel: UserViewModel by viewModels()
//    private val prefViewModel: PrefViewModel by viewModels()
    private lateinit var userEntityList: List<UserEntity>

    fun insertUser(userEntity: UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.insertUser(userEntity)
        }
    }

    fun updateUser(userEntity: UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateUser(userEntity)
        }
    }

    fun deleteUser(userEntity: UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.deleteUser(userEntity)
        }
    }

    fun getActiveUser(userId: Long): LiveData<UserEntity> {
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
            userEntityList = it
        }

        adapter.setOnItemClickListener(object : UserRecyclerView.MyClickListener {
            override fun useClickListener(position: Int) {
                // nastavit vybraneho pouzivatela ako aktivneho
                // nepovolit manipulaciu s rootom
                if (userEntityList[position].userName != "root" && userEntityList[position].password != "toor"){
                    val userEntity: UserEntity = userEntityList[position]
                    prefViewModel.saveUserIdPref(userEntity.userId)
                    Toast.makeText(requireContext(), "Selected user is now active.", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(), "Unable to access root.", Toast.LENGTH_LONG).show()
                }
            }

            override fun useLongClickListener(position: Int) {
                // update pouzivatela
                // nepovolit manipulaciu s rootom
                val userEntity: UserEntity = userEntityList[position]
                if (userEntity.userName != "root" && userEntity.password != "toor"){
                    val action = ListUsersFragmentDirections.actionListUsersFragmentToUpdateUserFragment(userEntity)
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
            val userEntity: UserEntity = UserEntity(0, userName, password)
            userViewModel.insertUser(userEntity)

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
            val userEntity: UserEntity = args.user.copy()
            userEntity.userName = userName
            userEntity.password = password

            userViewModel.updateUser(userEntity)

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