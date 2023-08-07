package com.example.datatrap.session.presentation.session_edit

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentUpdateSessionBinding
import com.example.datatrap.locality.data.LocalitySessionCrossRef
import com.example.datatrap.locality.LocalitySessionViewModel
import com.example.datatrap.session.presentation.session_prj_list.SessionListViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class UpdateSessionFragment : Fragment() {

    private var _binding: FragmentUpdateSessionBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<UpdateSessionFragmentArgs>()

    private val sessionListViewModel: SessionListViewModel by viewModels()
    private val localitySessionViewModel: LocalitySessionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentUpdateSessionBinding.inflate(inflater, container, false)

        initSessionValuesToView()

        binding.btnDelAssoc.isEnabled = false
        binding.btnDelAssoc.setOnClickListener {
            deleteAssociationWithLocality()
        }

        localitySessionViewModel.existsLocalSessCrossRef(args.locList.localityId, args.session.sessionId).observe(viewLifecycleOwner) {
            binding.btnDelAssoc.isEnabled = it
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
            R.id.menu_save -> updateSession()
            R.id.menu_delete -> deleteSession()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initSessionValuesToView(){
        binding.etSession.setText(args.session.session.toString())
        binding.etNumOcc.setText(args.session.numOcc.toString())
    }

    private fun deleteAssociationWithLocality() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->
            // vymazat session
            val localSessCrossRef = LocalitySessionCrossRef(args.locList.localityId, args.session.sessionId)
            localitySessionViewModel.deleteLocalitySessionCrossRef(localSessCrossRef)

            Toast.makeText(requireContext(),"Association deleted.", Toast.LENGTH_LONG).show()
        }
            .setNegativeButton("No"){_, _ -> }
            .setTitle("Delete Association?")
            .setMessage("Are you sure you want to delete this association?")
            .create().show()
    }

    private fun deleteSession() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->
            // vymazat session
            sessionListViewModel.deleteSession(args.session)

            Toast.makeText(requireContext(),"Session deleted.", Toast.LENGTH_LONG).show()

            findNavController().navigateUp()
        }
            .setNegativeButton("No"){_, _ -> }
            .setTitle("Delete Session?")
            .setMessage("Are you sure you want to delete this session?")
            .create().show()
    }

    private fun updateSession() {
        val sessionNum = binding.etSession.text.toString()
        val numOcc = binding.etNumOcc.text.toString()
        if (checkInput(sessionNum, numOcc)){

            val session = args.session.copy()
            session.session = Integer.parseInt(sessionNum)
            session.numOcc = Integer.parseInt(numOcc)
            session.sessionDateTimeUpdated = Calendar.getInstance().time

            sessionListViewModel.updateSession(session)

            Toast.makeText(requireContext(), "Session Updated.", Toast.LENGTH_SHORT).show()

            findNavController().navigateUp()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun checkInput(session: String, numOcc: String): Boolean {
        return session.isNotEmpty() && numOcc.isNotEmpty()
    }

}