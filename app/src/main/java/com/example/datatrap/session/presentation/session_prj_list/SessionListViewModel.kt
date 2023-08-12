package com.example.datatrap.session.presentation.session_prj_list

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.core.data.pref.PrefViewModel
import com.example.datatrap.session.data.Session
import com.example.datatrap.session.data.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class SessionListViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val sessionListViewModel: SessionListViewModel by viewModels()
    private val localitySessionViewModel: LocalitySessionViewModel by viewModels()
    private val prefViewModel: PrefViewModel by viewModels()

    private lateinit var sessionList: List<Session>

    init {
        holder.binding.tvSessionDate.text = SimpleDateFormat.getDateTimeInstance().format(Date(currenItem.sessionStart))

        binding.tvSesPathPrjName.text = args.project.projectName
        binding.tvPathLocName.text = args.locList.localityName

        sessionListViewModel.getSessionsForProject(args.project.projectId)
            .observe(viewLifecycleOwner) { sessions ->
                adapter.setData(sessions)
                sessionList = sessions
            }

        adapter.setOnItemClickListener(object : PrjSessionRecyclerAdapter.MyClickListener {
            override fun useClickListener(position: Int) {
                // vytvorit spojenie medzi vybranou locality a session
                val session: Session = sessionList[position]

                // nastavit vybranu session
                prefViewModel.saveSesNumPref(session.session)

                val kombLocSess =
                    LocalitySessionCrossRef(args.locList.localityId, session.sessionId)
                localitySessionViewModel.insertLocalitySessionCrossRef(kombLocSess)

                // ideme do occasion
                val action =
                    ListPrjSessionFragmentDirections.actionListPrjSessionFragmentToListSesOccasionFragment(
                        session, args.locList
                    )
                findNavController().navigate(action)
            }

            override fun useLongClickListener(position: Int) {
                // uprava vybranej session
                val session: Session = sessionList[position]
                val action =
                    ListPrjSessionFragmentDirections.actionListPrjSessionFragmentToUpdateSessionFragment(
                        session, args.locList
                    )
                findNavController().navigate(action)
            }
        })

        binding.addSessionFloatButton.setOnClickListener {
            // pridanie novej session
            showAddDialog("New Session", "Add new session?")
        }
    }

    private fun insertSession() {
        val session: Session =
            Session(
                0,
                (sessionList.size + 1),
                args.project.projectId,
                0,
                Calendar.getInstance().time,
                null,
                Calendar.getInstance().time.time
            )

        // ulozit session
        sessionListViewModel.insertSession(session)

        Toast.makeText(requireContext(), "New session added.", Toast.LENGTH_SHORT).show()

    }

    fun insertSession(session: Session) {
        viewModelScope.launch(Dispatchers.IO) {
            sessionRepository.insertSession(session)
        }
    }

    fun updateSession(session: Session) {
        viewModelScope.launch(Dispatchers.IO) {
            sessionRepository.updateSession(session)
        }
    }

    fun deleteSession(session: Session) {
        viewModelScope.launch(Dispatchers.IO) {
            sessionRepository.deleteSession(session)
        }
    }

    fun getSessionsForProject(projectId: Long): LiveData<List<Session>> {
        return sessionRepository.getSessionsForProject(projectId)
    }

}