package com.example.datatrap.mouse.fragments.listinview

import android.content.Context
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datatrap.databinding.FragmentViewMouseBinding
import com.example.datatrap.myenums.EnumSex
import com.example.datatrap.myinterfaces.OnActiveFragment
import com.example.datatrap.viewmodels.*
import java.text.SimpleDateFormat

class ViewMouseFragment : Fragment() {

    private var _binding: FragmentViewMouseBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<ViewMouseFragmentArgs>()
    private lateinit var mouseViewModel: MouseViewModel
    private lateinit var pictureViewModel: PictureViewModel
    private lateinit var adapter: MouseHistRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewMouseBinding.inflate(inflater, container, false)
        mouseViewModel = ViewModelProvider(this).get(MouseViewModel::class.java)
        pictureViewModel = ViewModelProvider(this).get(PictureViewModel::class.java)

        val deviceID = Settings.Secure.getString(
            requireContext().contentResolver,
            Settings.Secure.ANDROID_ID
        )

        adapter = MouseHistRecyclerAdapter()
        binding.recyclerMouse.adapter = adapter
        binding.recyclerMouse.layoutManager = LinearLayoutManager(requireContext())

        val dividerItemDecoration = DividerItemDecoration(
            binding.recyclerMouse.context,
            LinearLayoutManager.VERTICAL
        )
        binding.recyclerMouse.addItemDecoration(dividerItemDecoration)

        if (args.mouseOccTuple.imgName != null) {
            pictureViewModel.getPictureById(args.mouseOccTuple.imgName!!)
        }
        pictureViewModel.gotPicture.observe(viewLifecycleOwner, {
            binding.ivMouse.setImageURI(it.path.toUri())
        })

        mouseViewModel.getMouseForView(args.mouseOccTuple.mouseId, deviceID).observe(viewLifecycleOwner, { mouseView ->
            binding.tvBodyLength.text = mouseView.body.toString()
            binding.tvTailLength.text = mouseView.tail.toString()
            binding.tvFeetLength.text = mouseView.feet.toString()
            binding.tvCatchTime.text = SimpleDateFormat.getDateTimeInstance().format(mouseView.mouseDateTime)
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
        })

        val logList = mutableListOf<String>()
        val id = if (args.mouseOccTuple.primeMouseID == null) args.mouseOccTuple.mouseId else args.mouseOccTuple.primeMouseID
        mouseViewModel.getMiceForLog(id!!, deviceID).observe(viewLifecycleOwner, { mouseLog ->
            if (args.mouseOccTuple.sex == EnumSex.FEMALE.myName) {
                mouseLog.forEach { mouse ->
                    val dateFormated = SimpleDateFormat.getDateTimeInstance().format(mouse.mouseDateTimeCreated)
                    val gravidity = if (mouse.gravidity == true) "Yes" else "No"
                    val lactating = if (mouse.lactating == true) "Yes" else "No"
                    val sexActive = if (mouse.sexActive) "Yes" else "No"
                    val log = "Catch DateTime - $dateFormated, Locality - ${mouse.localityName}, Trap Number - ${mouse.trapID}, Weight - ${mouse.weight}, Gravidity - $gravidity, Lactating - $lactating, Sex. Active - $sexActive"
                    logList.add(log)
                }
            } else {
                mouseLog.forEach { mouse ->
                    val dateFormated = SimpleDateFormat.getDateTimeInstance().format(mouse.mouseDateTimeCreated)
                    val sexActive = if (mouse.sexActive) "Yes" else "No"
                    val log = "Catch DateTime - $dateFormated, Locality - ${mouse.localityName}, Trap Number - ${mouse.trapID}, Weight - ${mouse.weight}, Sex. Active - $sexActive"
                    logList.add(log)
                }
            }
            adapter.setData(logList)
        })

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
}