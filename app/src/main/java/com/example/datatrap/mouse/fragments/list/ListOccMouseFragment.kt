package com.example.datatrap.mouse.fragments.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentListOccMouseBinding
import com.example.datatrap.models.Mouse
import com.example.datatrap.viewmodels.MouseViewModel

class ListOccMouseFragment : Fragment() {

    private var _binding: FragmentListOccMouseBinding? = null
    private val binding get() = _binding!!
    private lateinit var mouseViewModel: MouseViewModel
    private lateinit var adapter: MouseRecyclerAdapter
    private val args by navArgs<ListOccMouseFragmentArgs>()
    private lateinit var mouseList: List<Mouse>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentListOccMouseBinding.inflate(inflater, container, false)
        mouseViewModel = ViewModelProvider(this).get(MouseViewModel::class.java)

        adapter = MouseRecyclerAdapter(this)
        val recyclerView = binding.mouseRecyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mouseViewModel.getMiceForOccasion(args.occasion.occasionId).observe(viewLifecycleOwner, Observer { mice ->
            adapter.setData(mice)
            mouseList = mice
        })

        adapter.setOnItemClickListener(object : MouseRecyclerAdapter.MyClickListener{
            override fun useClickListener(position: Int) {
                //view
                val action = ListOccMouseFragmentDirections.actionListOccMouseFragmentToViewMouseFragment(mouseList[position])
                findNavController().navigate(action)
            }

            override fun useLongClickListener(position: Int) {
                //update
                val action = ListOccMouseFragmentDirections.actionListOccMouseFragmentToUpdateMouseFragment(mouseList[position])
                findNavController().navigate(action)
            }

        })

        binding.addMouseFloatButton.setOnClickListener {
            // pridat novu mys
            val action = ListOccMouseFragmentDirections.actionListOccMouseFragmentToAddNewMouseFragment(args.occasion)
            findNavController().navigate(action)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.mouse_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_recapture -> goToRecapture()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun goToRecapture(){
        val action = ListOccMouseFragmentDirections.actionListOccMouseFragmentToRecaptureMouseFragment()
        findNavController().navigate(action)
    }

}