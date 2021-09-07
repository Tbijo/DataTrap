package com.example.datatrap.session.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentUpdateSessionBinding
import com.example.datatrap.models.Locality
import com.example.datatrap.viewmodels.LocalityViewModel
import com.example.datatrap.viewmodels.SessionViewModel

class UpdateSessionFragment : Fragment() {

    private var _binding: FragmentUpdateSessionBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionViewModel: SessionViewModel
    private lateinit var localityViewModel: LocalityViewModel
    private val args by navArgs<UpdateSessionFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentUpdateSessionBinding.inflate(inflater, container, false)
        sessionViewModel = ViewModelProvider(this).get(SessionViewModel::class.java)
        localityViewModel = ViewModelProvider(this).get(LocalityViewModel::class.java)

        initSessionValuesToView()

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
            R.id.menu_save -> updateSession()
            R.id.menu_delete -> deleteSession()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initSessionValuesToView(){
        binding.etSession.setText(args.session.session)
        binding.etNumOcc.setText(args.session.numOcc)
    }

    private fun deleteSession() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->

            // znizit numSess v lokalite
            updateLocalityNumSes()

            // vymazat session
            sessionViewModel.deleteSession(args.session)

            Toast.makeText(requireContext(),"Session deleted.", Toast.LENGTH_LONG).show()

            findNavController().navigateUp()
        }
            .setNegativeButton("No"){_, _ -> }
            .setTitle("Delete Session?")
            .setMessage("Are you sure you want to delete this session?")
            .create().show()
    }

    private fun updateLocalityNumSes(){
        val updatedLocality: Locality = localityViewModel.getLocality(args.locality.localityId).value!!
        updatedLocality.numSessions = (updatedLocality.numSessions - 1)
        localityViewModel.updateLocality(updatedLocality)
    }

    private fun updateSession() {
        val sessionNum = binding.etSession.text.toString()
        val numOcc = binding.etNumOcc.text.toString()
        val date = args.session.date
        if (checkIput(sessionNum, numOcc, date)){

            val session = args.session
            session.session = Integer.parseInt(sessionNum)
            session.numOcc = Integer.parseInt(numOcc)
            session.date = date

            sessionViewModel.updateSession(session)

            Toast.makeText(requireContext(), "Session Updated.", Toast.LENGTH_SHORT).show()

            findNavController().navigateUp()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun checkIput(session: String, numOcc: String, date: String): Boolean {
        return session.isNotEmpty() && numOcc.isNotEmpty() && date.isNotEmpty()
    }

}