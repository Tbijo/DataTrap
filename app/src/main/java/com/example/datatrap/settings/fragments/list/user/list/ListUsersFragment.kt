package com.example.datatrap.settings.fragments.list.user.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datatrap.databinding.FragmentListUsersBinding
import com.example.datatrap.models.User
import com.example.datatrap.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListUsersFragment : Fragment() {

    private var _binding: FragmentListUsersBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: UserRecyclerView
    private val userViewModel: UserViewModel by viewModels()

    private lateinit var userList: List<User>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentListUsersBinding.inflate(inflater, container, false)

        adapter = UserRecyclerView()
        binding.userRecyclerview.adapter = adapter
        binding.userRecyclerview.layoutManager = LinearLayoutManager(requireContext())

        val dividerItemDecoration = DividerItemDecoration(
            binding.userRecyclerview.context,
            LinearLayoutManager.VERTICAL
        )
        binding.userRecyclerview.addItemDecoration(dividerItemDecoration)

        userViewModel.userList.observe(viewLifecycleOwner) {
            adapter.setData(it)
            userList = it
        }

        adapter.setOnItemClickListener(object : UserRecyclerView.MyClickListener{
            override fun useClickListener(position: Int) {
                // nastavit vybraneho pouzivatela ako aktivneho
                // a predchadzajuceho neaktivneho
                // nepovolit manipulaciu s rootom
                if (userList[position].userName != "root" && userList[position].password != "toor"){
                    userViewModel.inactiveAllUsers()
                    val user: User = userList[position]
                    user.isActive = 1
                    userViewModel.updateUser(user)
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

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}