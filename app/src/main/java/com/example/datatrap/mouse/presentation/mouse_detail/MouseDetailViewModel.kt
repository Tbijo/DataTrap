package com.example.datatrap.mouse.presentation.mouse_detail

import androidx.lifecycle.ViewModel
import com.example.datatrap.core.util.EnumSex
import com.example.datatrap.mouse.domain.model.MouseLog
import com.example.datatrap.mouse.domain.model.MouseView
import com.example.datatrap.mouse.presentation.mouse_list.MouseListViewModel
import com.example.datatrap.core.presentation.components.ViewImageFragment
import com.example.datatrap.camera.presentation.CameraViewModel
import java.text.SimpleDateFormat
import java.util.Date

class MouseDetailViewModel: ViewModel() {

    private val mouseListViewModel: MouseListViewModel by viewModels()
    private val cameraViewModel: CameraViewModel by viewModels()

    private val logList = mutableListOf<String>()
    private var path: String? = null

    init {
        mouseListViewModel.getMouseForView(args.mouseOccTuple.mouseId).observe(viewLifecycleOwner) { mouseView ->
            initMouseValues(mouseView)

            val id = if (args.mouseOccTuple.primeMouseID == null) mouseView.mouseIid else args.mouseOccTuple.primeMouseID
            mouseListViewModel.getMiceForLog(id!!, args.mouseOccTuple.deviceID).observe(viewLifecycleOwner) { mouseLog ->
                fillLogs(mouseLog)
            }

            cameraViewModel.getImageForMouse(mouseView.mouseIid, args.mouseOccTuple.deviceID).observe(viewLifecycleOwner) {
                if (it != null) {
                    binding.ivMouse.setImageURI(it.path.toUri())
                    path = it.path
                }
            }
        }

        binding.ivMouse.setOnClickListener {
            if (path != null) {
                val fragman = requireActivity().supportFragmentManager
                val floatFrag = ViewImageFragment(path!!)
                floatFrag.show(fragman, "FloatFragViewImage")
            }
        }

        var historyList = emptyList<String>()
        holder.binding.tvLog.text = historyList[1]
    }

    private fun initMouseValues(mouseView: MouseView) {
        binding.tvBodyLength.text = mouseView.body.toString()
        binding.tvTailLength.text = mouseView.tail.toString()
        binding.tvFeetLength.text = mouseView.feet.toString()
        binding.tvCatchTime.text = SimpleDateFormat.getDateTimeInstance().format(Date(mouseView.mouseCaught))
        binding.tvEar.text = mouseView.ear.toString()
        binding.tvGravidity.text = if (mouseView.gravidity == true) "Yes" else "No"
        binding.tvLactating.text = if (mouseView.lactating == true) "Yes" else "No"
        binding.tvSexActive.text = if (mouseView.sexActive) "Yes" else "No"
        binding.tvMouseAge.text = mouseView.age.toString()
        binding.tvMouseSex.text = mouseView.sex.toString()
        binding.tvMouseWeight.text = mouseView.weight.toString()
        binding.tvMouseNote.text = mouseView.note.toString()
        binding.tvTestesLength.text = mouseView.testesLength.toString()
        binding.tvTestesWidth.text = mouseView.testesWidth.toString()
        binding.tvEmbryoRight.text = mouseView.embryoRight.toString()
        binding.tvEmbryoLeft.text = mouseView.embryoLeft.toString()
        binding.tvEmbryoDiam.text = mouseView.embryoDiameter.toString()
        binding.tvMc.text = when(mouseView.mc) {
            true -> "Yes"
            false -> "No"
            else -> "null"
        }
        binding.tvMcLeft.text = mouseView.mcLeft.toString()
        binding.tvMcRight.text = mouseView.mcRight.toString()
        binding.tvMouseFullName.text = mouseView.specieFullName.toString()
        binding.tvSpecie.text = mouseView.specieCode
        binding.tvMouseLeg.text = mouseView.legit
        binding.tvMouseProjectName.text = mouseView.projectName
    }

    private fun fillLogs(mouseLog: List<MouseLog>) {
        if (args.mouseOccTuple.sex == EnumSex.FEMALE.myName) {
            mouseLog.forEach { mouse ->
                val dateFormated = SimpleDateFormat.getDateTimeInstance().format(Date(mouse.mouseCaught))
                val gravidity = if (mouse.gravidity == true) "Yes" else "No"
                val lactating = if (mouse.lactating == true) "Yes" else "No"
                val sexActive = if (mouse.sexActive) "Yes" else "No"
                val log = "Catch DateTime - $dateFormated, Locality - ${mouse.localityName}, Trap Number - ${mouse.trapID}, Weight - ${mouse.weight}, Gravidity - $gravidity, Lactating - $lactating, Sex. Active - $sexActive"
                logList.add(log)
            }
        } else {
            mouseLog.forEach { mouse ->
                val dateFormated = SimpleDateFormat.getDateTimeInstance().format(Date(mouse.mouseCaught))
                val sexActive = if (mouse.sexActive) "Yes" else "No"
                val log = "Catch DateTime - $dateFormated, Locality - ${mouse.localityName}, Trap Number - ${mouse.trapID}, Weight - ${mouse.weight}, Sex. Active - $sexActive"
                logList.add(log)
            }
        }
        adapter.setData(logList)
    }
}