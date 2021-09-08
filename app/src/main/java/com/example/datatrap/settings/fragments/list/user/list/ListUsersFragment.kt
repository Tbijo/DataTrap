package com.example.datatrap.settings.fragments.list.user.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datatrap.databinding.FragmentListUsersBinding
import com.example.datatrap.models.User
import com.example.datatrap.viewmodels.UserViewModel

class ListUsersFragment : Fragment() {

    private var _binding: FragmentListUsersBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: UserRecyclerView
    private lateinit var userViewModel: UserViewModel

    private lateinit var userList: List<User>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentListUsersBinding.inflate(inflater, container, false)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        adapter = UserRecyclerView()
        binding.userRecyclerview.adapter = adapter
        binding.userRecyclerview.layoutManager = LinearLayoutManager(requireContext())

        userViewModel.userList.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
            userList = it
        })

        adapter.setOnItemClickListener(object : UserRecyclerView.MyClickListener{
            override fun useClickListener(position: Int) {
                // nastavit vybraneho pouzivatela ako aktivneho
                // a predchadzajuceho neaktivneho
                inactiveAllUsers()
                val user: User = userList[position]
                user.isActive = 1
                userViewModel.updateUser(user)
            }

            override fun useLongClickListener(position: Int) {
                // update pouzivatela
                val user: User = userList[position]
                val action = ListUsersFragmentDirections.actionListUsersFragmentToUpdateUserFragment(user)
                findNavController().navigate(action)
            }

        })

        binding.addUserFloatButton.setOnClickListener {
            val action = ListUsersFragmentDirections.actionListUsersFragmentToAddUserFragment()
            findNavController().navigate(action)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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