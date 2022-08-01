package com.example.datatrap.sync

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.datatrap.databinding.FragmentSynchronizeBinding
import com.example.datatrap.models.sync.*
import com.example.datatrap.viewmodels.*
import com.example.datatrap.viewmodels.datastore.PrefViewModel
import com.example.datatrap.www.DataTrapViewModel
import com.example.datatrap.www.ResultWrapper
import com.example.datatrap.www.SyncClassViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.*

@AndroidEntryPoint
class SynchronizeFragment : Fragment() {

    private var _binding: FragmentSynchronizeBinding? = null
    private val binding get() = _binding!!

    private val mouseViewModel: MouseViewModel by viewModels()
    private val occasionViewModel: OccasionViewModel by viewModels()
    private val localityViewModel: LocalityViewModel by viewModels()
    private val sessionViewModel: SessionViewModel by viewModels()
    private val projectViewModel: ProjectViewModel by viewModels()
    private val dataTrapViewModel: DataTrapViewModel by viewModels()
    private val mouseImageViewmodel: MouseImageViewModel by viewModels()
    private val occasionImageViewmodel: OccasionImageViewModel by viewModels()
    private val specieImageViewmodel: SpecieImageViewModel by viewModels()
    private val syncClassViewModel: SyncClassViewModel by viewModels()

    private var mouseSyncList: List<MouseSync> = emptyList()
    private var occasionSyncList: List<OccasionSync> = emptyList()
    private var localitySyncList: List<LocalitySync> = emptyList()
    private var sessionSyncList: List<SessionSync> = emptyList()
    private var projectSyncList: List<ProjectSync> = emptyList()

    private var listMouseImageSync: List<MouseImageSync> = emptyList()
    private var listOccasionImageSync: List<OccasionImageSync> = emptyList()
    private var listSpecieImageSync: List<SpecieImageSync> = emptyList()

    private val prefViewModel: PrefViewModel by viewModels()
    private var lastSyncDate: Long? = null

    private var isDataDone = false
    private var isImageDone = false
    private var isMouseFileDone = false
    private var isOccasionFileDone = false
    private var isSpecieFileDone = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentSynchronizeBinding.inflate(inflater, container, false)

        prefViewModel.readLastSyncDatePref.observe(viewLifecycleOwner) { date ->
            lastSyncDate = date
            syncClassViewModel.retrieveData(date)
        }

        syncClassViewModel.dataRetrievedLive.observe(viewLifecycleOwner) { syncData ->
            // TODO data su ziskane spustit stahovanie
        }

        binding.btnSync.setOnClickListener {
            if (isOnline(requireContext())) {
                startSync("Downloading Data")
                // zohnat nove data
                dataTrapViewModel.getData(lastSyncDate!!/1000)
            } else {
                Toast.makeText(requireContext(), "Turn on WIFI.", Toast.LENGTH_LONG).show()
            }
        }

        dataTrapViewModel.syncClassLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.MySuccess -> {
                    if (it.value.isSuccessful) {
                        // Ulozit to co pride do databazy
                        syncClassViewModel.saveData(it.value.body()!!)
                    } else {
                        Toast.makeText(requireContext(), "No Response.", Toast.LENGTH_LONG).show()
                    }
                }
                is ResultWrapper.MyError -> {
                    Toast.makeText(requireContext(), "${it.errorM} ${it.code} MainActivity", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

        syncClassViewModel.dataSavedLive.observe(viewLifecycleOwner) {
            // TODO Spravit Upload az ked sa doukladaju nove data
            // TODO nebude treba booleany
            // TODO spravi sa LiveData ked postne potom spustit upload
            startSync("Uploading Data")
            dataTrapViewModel.insertData(getSyncClassList())
            dataTrapViewModel.insertImageInfo(getImageSync())

            // images
            if (!isMouseFileDone) {
                dataTrapViewModel.insertMouseFiles(getMouseFileList())
            }
            if (!isOccasionFileDone) {
                dataTrapViewModel.insertOccasionFiles(getOccasionFileList())
            }
            if (!isSpecieFileDone) {
                dataTrapViewModel.insertSpecieFiles(getSpecieFileList())
            }
        }

        dataTrapViewModel.dataInsertedLive.observe(viewLifecycleOwner) {
            isDataDone = true
            if (isDataDone && isImageDone && isMouseFileDone && isOccasionFileDone && isSpecieFileDone) endSync()
        }

        dataTrapViewModel.imageInsertedLive.observe(viewLifecycleOwner) {
            isImageDone = true
            if (isDataDone && isImageDone && isMouseFileDone && isOccasionFileDone && isSpecieFileDone) endSync()
        }

        dataTrapViewModel.mouseFileInsertedLive.observe(viewLifecycleOwner) {
            isMouseFileDone = true
            if (isDataDone && isImageDone && isMouseFileDone && isOccasionFileDone && isSpecieFileDone) endSync()
        }

        dataTrapViewModel.occasionFileInsertedLive.observe(viewLifecycleOwner) {
            isOccasionFileDone = true
            if (isDataDone && isImageDone && isMouseFileDone && isOccasionFileDone && isSpecieFileDone) endSync()
        }

        dataTrapViewModel.specieFileInsertedLive.observe(viewLifecycleOwner) {
            isSpecieFileDone = true
            if (isDataDone && isImageDone && isMouseFileDone && isOccasionFileDone && isSpecieFileDone) endSync()
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun startSync(work: String) {
        // start
        binding.tvLoadMessage.text = work
        binding.tvLoadMessage.isVisible = true
        binding.loadingBar.isVisible = true
    }

    private fun endSync() {
        // end
        binding.tvLoadMessage.isVisible = false
        binding.loadingBar.isVisible = false
        // update lastDate Sync
        prefViewModel.saveLastSyncDatePref(Calendar.getInstance().time.time)
    }

    // TODO Preniest do viewModelu mozno sa budu dat odstranit Id-cka
    private fun getSyncClassList(): List<SyncClass> {

        val sortedMice: Map<Long, List<MouseSync>> = mouseSyncList.groupBy { it.occasionID }

        val listLocOccMice: MutableList<LocOccLMouse> = mutableListOf()

        sortedMice.forEach { (occasionID, mouseList) ->
            val occasionSync: OccasionSync? = occasionSyncList.find { it.occasionId == occasionID }
            val localitySync: LocalitySync? = localitySyncList.find { it.localityId == occasionSync?.localityID!! }

            val locOccLMouse = LocOccLMouse(localitySync!!, occasionSync!!, mouseList)
            listLocOccMice.add(locOccLMouse)
        }

        val sortedLocOccMice: Map<OccasionSync, List<LocOccLMouse>> = listLocOccMice.groupBy { it.occ }

        val listSesLOLM: MutableList<SesLOLM> = mutableListOf()

        sortedLocOccMice.forEach { (occasion, list) ->
            val sessionSync: SessionSync? = sessionSyncList.find { it.sessionId ==  occasion.sessionID }

            val sesLOLM = SesLOLM(sessionSync!!, list)
            listSesLOLM.add(sesLOLM)
        }

        val sortedSesLOLM: Map<SessionSync, List<SesLOLM>> = listSesLOLM.groupBy { it.ses }

        val syncClassList: MutableList<SyncClass> = mutableListOf()

        sortedSesLOLM.forEach { (sessionSync, list) ->
            val projectSync: ProjectSync? = projectSyncList.find { it.projectId == sessionSync.projectID }

            val syncClass = SyncClass(projectSync!!, list)
            syncClassList.add(syncClass)
        }

        return syncClassList
    }

    private fun getImageSync(): ImageSync {
        val imageSync = ImageSync(listMouseImageSync, listOccasionImageSync, listSpecieImageSync)
        return imageSync
    }

    private fun getMouseFileList(): List<File> {
        val files: List<File> = listMouseImageSync.map { File(it.path) }
        return files
    }

    private fun getOccasionFileList(): List<File> {
        val fileList: List<File> = listOccasionImageSync.map { File(it.path) }
        return fileList
    }

    private fun getSpecieFileList(): List<File> {
        val fileList: List<File> = listSpecieImageSync.map { File(it.path) }
        return fileList
    }

    private fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val n = cm.activeNetwork
        if (n != null) {
            val nc = cm.getNetworkCapabilities(n)
            return nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(
                NetworkCapabilities.TRANSPORT_WIFI
            )
        }
        return false
    }

}