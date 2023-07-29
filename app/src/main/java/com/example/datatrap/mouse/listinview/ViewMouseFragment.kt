package com.example.datatrap.mouse.listinview

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datatrap.databinding.FragmentViewMouseBinding
import com.example.datatrap.mouse.data.MouseLog
import com.example.datatrap.mouse.data.MouseView
import com.example.datatrap.mouse.presentation.MouseViewModel
import com.example.datatrap.core.util.EnumSex
import com.example.datatrap.core.util.OnActiveFragment
import com.example.datatrap.picture.presentation.MouseImageViewModel
import com.example.datatrap.picture.ViewImageFragment
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ViewMouseFragment : Fragment() {

    private var _binding: FragmentViewMouseBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<ViewMouseFragmentArgs>()
    private val mouseViewModel: MouseViewModel by viewModels()
    private val mouseImageViewModel: MouseImageViewModel by viewModels()
    private lateinit var adapter: MouseHistRecyclerAdapter

    private val logList = mutableListOf<String>()
    private var path: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewMouseBinding.inflate(inflater, container, false)

        adapter = MouseHistRecyclerAdapter()
        binding.recyclerMouse.adapter = adapter
        binding.recyclerMouse.layoutManager = LinearLayoutManager(requireContext())

        val dividerItemDecoration = DividerItemDecoration(
            binding.recyclerMouse.context,
            LinearLayoutManager.VERTICAL
        )
        binding.recyclerMouse.addItemDecoration(dividerItemDecoration)

        mouseViewModel.getMouseForView(args.mouseOccTuple.mouseId).observe(viewLifecycleOwner) { mouseView ->
            initMouseValues(mouseView)

            val id = if (args.mouseOccTuple.primeMouseID == null) mouseView.mouseIid else args.mouseOccTuple.primeMouseID
            mouseViewModel.getMiceForLog(id!!, args.mouseOccTuple.deviceID).observe(viewLifecycleOwner) { mouseLog ->
                fillLogs(mouseLog)
            }

            mouseImageViewModel.getImageForMouse(mouseView.mouseIid, args.mouseOccTuple.deviceID).observe(viewLifecycleOwner) {
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

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private lateinit var listener: OnActiveFragment

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnActiveFragment
    }

    override fun onResume() {
        super.onResume()
        listener.setTitle("Individual Code: ${args.mouseOccTuple.mouseCode}")
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