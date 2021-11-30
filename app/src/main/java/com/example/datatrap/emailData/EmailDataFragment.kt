package com.example.datatrap.emailData

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.datatrap.databinding.FragmentEmailDataBinding
import com.example.datatrap.models.Mouse
import com.example.datatrap.models.Occasion
import com.example.datatrap.models.Project
import com.example.datatrap.models.Session
import com.example.datatrap.viewmodels.*
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import java.io.File
import java.io.IOException

class EmailDataFragment : Fragment() {

    private var _binding: FragmentEmailDataBinding? = null
    private val binding get() = _binding!!

    private lateinit var mouseViewModel: MouseViewModel
    private lateinit var projectViewModel: ProjectViewModel
    private lateinit var occasionViewModel: OccasionViewModel
    private lateinit var sessionViewModel: SessionViewModel

    private var mouseUri: Uri? = null
    private var occasionUri: Uri? = null
    private var sessionUri: Uri? = null
    private var projectUri: Uri? = null

    private var mouseFile: File? = null
    private var occasionFile: File? = null
    private var sessionFile: File? = null
    private var projectFile: File? = null

    private lateinit var mouseList: List<Mouse>
    private lateinit var projectList: List<Project>
    private lateinit var occasionList: List<Occasion>
    private lateinit var sessionList: List<Session>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentEmailDataBinding.inflate(inflater, container, false)

        mouseViewModel = ViewModelProvider(this).get(MouseViewModel::class.java)
        projectViewModel = ViewModelProvider(this).get(ProjectViewModel::class.java)
        occasionViewModel = ViewModelProvider(this).get(OccasionViewModel::class.java)
        sessionViewModel = ViewModelProvider(this).get(SessionViewModel::class.java)

        mouseViewModel.getMiceForEmail().observe(viewLifecycleOwner, {
            mouseList = it
        })
        projectViewModel.projectList.observe(viewLifecycleOwner, {
            projectList = it
        })
        sessionViewModel.getSessionsForEmail().observe(viewLifecycleOwner, {
            sessionList = it
        })
        occasionViewModel.getOccasionsForEmail().observe(viewLifecycleOwner, {
            occasionList = it
        })

        binding.btnGenFile.setOnClickListener {
           if (mouseList.isNotEmpty() && projectList.isNotEmpty() && sessionList.isNotEmpty() && occasionList.isNotEmpty()) {
               binding.progressBar.isVisible = true
               saveCSVFiles()
               Toast.makeText(requireContext(), "CSV files generated.", Toast.LENGTH_LONG).show()
               binding.progressBar.isVisible = false
           } else {
               Toast.makeText(requireContext(), "Empty list.", Toast.LENGTH_LONG).show()
           }
        }

        binding.btnSendEmail.setOnClickListener {
            if (isOnline(requireContext())) {
                if (projectUri != null && mouseUri != null && sessionUri != null && occasionUri != null) {
                    if (binding.etEmail.text.toString().isNotBlank()) {
                        val recipient = binding.etEmail.text.toString()
                        val subject = if (binding.etSubject.text.toString().isBlank()) "Exported Database" else binding.etSubject.text.toString()
                        val message = if (binding.etMessage.text.toString().isBlank()) "Here is the data from the App" else binding.etMessage.text.toString()

                        sendEmail(recipient, subject, message, arrayListOf(projectUri!!, mouseUri!!, sessionUri!!, occasionUri!!))
                    } else {
                        Toast.makeText(requireContext(), "No email was found.", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Files were not created.", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(requireContext(), "Connect to internet.", Toast.LENGTH_LONG).show()
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        mouseFile?.apply {
            if (exists()) delete()
        }
        projectFile?.apply {
            if (exists()) delete()
        }
        occasionFile?.apply {
            if (exists()) delete()
        }
        sessionFile?.apply {
            if (exists()) delete()
        }
    }

    private fun sendEmail(recipient: String, subject: String, message: String, myUri: ArrayList<Uri>) {
        val mIntent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
            data = Uri.parse("mailto:")
            type = "text/html"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, message)
            putExtra(Intent.EXTRA_STREAM, myUri)
        }
        try {
            startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
        }
        catch (e: Exception) {
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun saveCSVFiles() {
        mouseFile = createCSVFile("Mouse")
        occasionFile = createCSVFile("Occasion")
        sessionFile = createCSVFile("Session")
        projectFile = createCSVFile("Project")

        mouseFile?.apply {
            createNewFile()
            if (exists()) {
                exportMouseTableToCSV(this)
                mouseUri = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.fileprovider", this)
            } else {
                Toast.makeText(requireContext(), "Mouse file was not generated.", Toast.LENGTH_LONG).show()
            }
        } ?: Toast.makeText(requireContext(), "Mouse file was not initialized.", Toast.LENGTH_LONG).show()

        occasionFile?.apply {
            createNewFile()
            if (exists()) {
                exportOccasionTableToCSV(this)
                occasionUri = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.fileprovider", this)
            } else {
                Toast.makeText(requireContext(), "Occasion file was not generated.", Toast.LENGTH_LONG).show()
            }
        } ?: Toast.makeText(requireContext(), "Occasion file was not initialized.", Toast.LENGTH_LONG).show()

        sessionFile?.apply {
            createNewFile()
            if (exists()) {
                exportSessionTableToCSV(this)
                sessionUri = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.fileprovider", this)
            } else {
                Toast.makeText(requireContext(), "Session file was not generated.", Toast.LENGTH_LONG).show()
            }
        } ?: Toast.makeText(requireContext(), "Session file was not initialized.", Toast.LENGTH_LONG).show()

        projectFile?.apply {
            createNewFile()
            if (exists()) {
                exportProjectTableToCSV(this)
                projectUri = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.fileprovider", this)
            } else {
                Toast.makeText(requireContext(), "Project file was not generated.", Toast.LENGTH_LONG).show()
            }
        } ?: Toast.makeText(requireContext(), "Project file was not initialized.", Toast.LENGTH_LONG).show()
    }

    @Throws(IOException::class)
    private fun createCSVFile(name: String): File {
        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        return File.createTempFile(
            name,
            ".csv",
            storageDir
        )
    }

    private fun exportMouseTableToCSV(csvFile: File) {
        csvWriter().open(csvFile, append = false) {
            writeRow(listOf("ID", "code", "deviceID", "primeMouseID", "speciesID", "protocolID",
                "occasionID", "localityID", "trapID", "mouseDateTimeCreated", "mouseDateTimeUpdated",
                "sex", "age", "gravidity", "lactating", "sexActive", "weight", "recapture",
                "captureID", "body", "tail", "feet", "ear", "testesLength", "testesWidth",
                "embryoRight", "embryoLeft", "embryoDiameter", "MC", "MCright", "MCleft", "note"))
            mouseList.forEach {
                writeRow(listOf<String>(
                    it.mouseId.toString(), it.code.toString(), it.deviceID, it.primeMouseID.toString(), it.speciesID.toString(), it.protocolID.toString(),
                    it.occasionID.toString(), it.localityID.toString(), it.trapID.toString(), "${it.mouseDateTimeCreated}", "${it.mouseDateTimeUpdated}",
                    it.sex.toString(), it.age.toString(), it.gravidity.toString(), it.lactating.toString(), it.sexActive.toString(), it.weight.toString(), it.recapture.toString(),
                    it.captureID.toString(), it.body.toString(), it.tail.toString(), it.feet.toString(), it.ear.toString(), it.testesLength.toString(), it.testesWidth.toString(),
                    it.embryoRight.toString(), it.embryoLeft.toString(), it.embryoDiameter.toString(), it.MC.toString(), it.MCright.toString(), it.MCleft.toString(), it.note.toString()
                ))
            }
        }
    }

    private fun exportOccasionTableToCSV(csvFile: File) {
        csvWriter().open(csvFile, append = false) {
            writeRow(listOf(
                "ID", "occasion", "deviceID", "localityID", "sessionID", "methodID", "methodTypeID",
                "trapTypeID", "envTypeID", "vegetTypeID", "occasionDateTimeCreated",
                "occasionDateTimeUpdated", "gotCaught", "numTraps", "numMice", "temperature",
                "weather", "leg", "note"
            ))
            occasionList.forEach {
                writeRow(listOf<String>(
                    it.occasionId.toString(), it.occasion.toString(), it.deviceID, it.localityID.toString(),
                    it.sessionID.toString(), it.methodID.toString(), it.methodTypeID.toString(),
                    it.trapTypeID.toString(), it.envTypeID.toString(), it.vegetTypeID.toString(),
                    "${it.occasionDateTimeCreated}", "${it.occasionDateTimeUpdated}", it.gotCaught.toString(),
                    it.numTraps.toString(), it.numMice.toString(), it.temperature.toString(),
                    it.weather.toString(), it.leg, it.note.toString()
                ))
            }
        }
    }

    private fun exportSessionTableToCSV(csvFile: File) {
        csvWriter().open(csvFile, append = false) {
            writeRow(listOf(
                "ID", "session", "deviceID", "projectID", "numOcc", "sessionDateTimeCreated",
                "sessionDateTimeUpdated"
            ))
            sessionList.forEach {
                writeRow(listOf<String>(
                    it.sessionId.toString(), it.session.toString(), it.deviceID, it.projectID.toString(),
                    it.numOcc.toString(), "${it.sessionDateTimeCreated}", "${it.sessionDateTimeUpdated}"
                ))
            }
        }
    }

    private fun exportProjectTableToCSV(csvFile: File) {
        csvWriter().open(csvFile, append = false) {
            writeRow(listOf(
                "ID", "projectName", "deviceID", "projectDateTimeCreated", "projectDateTimeUpdated",
                "numLocal", "numMice"
            ))
            projectList.forEach {
                writeRow(listOf<String>(
                    it.projectId.toString(), it.projectName, it.deviceID, "${it.projectDateTimeCreated}",
                    "${it.projectDateTimeUpdated}", it.numLocal.toString(), it.numMice.toString()
                ))
            }
        }
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