package com.example.datatrap.mouse.fragments.listinview

import android.content.Context
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datatrap.databinding.FragmentViewMouseBinding
import com.example.datatrap.models.Locality
import com.example.datatrap.models.Mouse
import com.example.datatrap.models.Occasion
import com.example.datatrap.models.Session
import com.example.datatrap.myenums.EnumSex
import com.example.datatrap.myinterfaces.OnActiveFragment
import com.example.datatrap.viewmodels.*
import java.text.SimpleDateFormat

class ViewMouseFragment : Fragment() {

    private var _binding: FragmentViewMouseBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<ViewMouseFragmentArgs>()
    private lateinit var specieViewModel: SpecieViewModel
    private lateinit var occasionViewModel: OccasionViewModel
    private lateinit var sessionViewModel: SessionViewModel
    private lateinit var projectViewModel: ProjectViewModel
    private lateinit var mouseViewModel: MouseViewModel
    private lateinit var localityViewModel: LocalityViewModel
    private lateinit var pictureViewModel: PictureViewModel
    private lateinit var adapter: MouseHistRecyclerAdapter

    private val logList = mutableListOf<String>()

    var deviceID: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewMouseBinding.inflate(inflater, container, false)
        specieViewModel = ViewModelProvider(this).get(SpecieViewModel::class.java)
        occasionViewModel = ViewModelProvider(this).get(OccasionViewModel::class.java)
        sessionViewModel = ViewModelProvider(this).get(SessionViewModel::class.java)
        projectViewModel = ViewModelProvider(this).get(ProjectViewModel::class.java)
        mouseViewModel = ViewModelProvider(this).get(MouseViewModel::class.java)
        localityViewModel = ViewModelProvider(this).get(LocalityViewModel::class.java)
        pictureViewModel = ViewModelProvider(this).get(PictureViewModel::class.java)

        deviceID = Settings.Secure.getString(
            requireContext().contentResolver,
            Settings.Secure.ANDROID_ID
        )

        if (args.mouse.imgName != null) {
            pictureViewModel.getPictureById(args.mouse.imgName!!)
        }
        pictureViewModel.gotPicture.observe(viewLifecycleOwner, {
            binding.ivMouse.setImageURI(it.path.toUri())
        })

        val mySpecie = specieViewModel.getSpecie(args.mouse.speciesID)
        binding.tvMouseFullName.text = mySpecie.fullName
        binding.tvSpecie.text = mySpecie.speciesCode

        val occasion: Occasion = occasionViewModel.getOccasion(args.mouse.occasionID)
        binding.tvMouseLeg.text = occasion.leg

        val session: Session = sessionViewModel.getSession(occasion.sessionID)

        binding.tvMouseProjectName.text =
            projectViewModel.getProject(session.projectID!!).projectName

        adapter = MouseHistRecyclerAdapter()
        binding.recyclerMouse.adapter = adapter
        binding.recyclerMouse.layoutManager = LinearLayoutManager(requireContext())

        val dividerItemDecoration = DividerItemDecoration(
            binding.recyclerMouse.context,
            LinearLayoutManager.VERTICAL
        )
        binding.recyclerMouse.addItemDecoration(dividerItemDecoration)

        // zobrazit predch zaznamy aj pre mysi s recap = 1
        if (args.mouse.primeMouseID == null) {
            // naplnit list male alebo female predchadzajucimi udajmi o jedincovi
            mouseViewModel.getMiceForLog(args.mouse.mouseId, deviceID).observe(viewLifecycleOwner, { mouseList ->
                fillList(mouseList)
            })
        } else {
            mouseViewModel.getMouse(args.mouse.primeMouseID!!, deviceID)
            mouseViewModel.getMiceForLog(args.mouse.primeMouseID!!, deviceID).observe(viewLifecycleOwner, { mouseList ->
                fillList(mouseList)
            })
        }

        mouseViewModel.gotMouse.observe(viewLifecycleOwner, {
            if (args.mouse.sex == EnumSex.FEMALE.myName) {
                val dateFormated = SimpleDateFormat.getDateTimeInstance().format(it.mouseDateTimeCreated)
                val locName = localityViewModel.getLocality(it.localityID).localityName
                val femaleLog =
                    "Catch DateTime - $dateFormated, Locality - $locName, Trap Number - ${it.trapID}, Weight - ${it.weight}, Gravidity - ${it.gravidity}, Lactating - ${it.lactating}, Sex. Active - ${it.sexActive}"
                logList.add(0, femaleLog)
                adapter.setData(logList)
            } else {
                val dateFormated = SimpleDateFormat.getDateTimeInstance().format(it.mouseDateTimeCreated)
                val locName = localityViewModel.getLocality(it.localityID).localityName
                val maleLog =
                    "Catch DateTime - $dateFormated, Locality - $locName, Trap Number - ${it.trapID}, Weight - ${it.weight}, Sex. Active - ${it.sexActive}"
                logList.add(0, maleLog)
                adapter.setData(logList)
            }
        })

        initMouseValuesToView()

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
        listener.setTitle("Individual ID: ${args.mouse.code}")
    }

    private fun fillList(mouseList: List<Mouse>) {
        var locality: Locality?
        var sexActive: String
        var oldId: Long = 0
        var newId: Long
        var localName = ""

        if (args.mouse.sex == EnumSex.FEMALE.myName) {
            var femaleLog: String
            mouseList.forEach {
                sexActive = if (it.sexActive == true) "Yes" else "No"
                newId = it.localityID
                if (oldId != newId) {
                    locality = localityViewModel.getLocality(args.mouse.localityID)
                    oldId = newId
                    newId = locality!!.localityId
                    localName = locality!!.localityName
                }
                val dateFormated = SimpleDateFormat.getDateTimeInstance().format(it.mouseDateTimeCreated)
                femaleLog =
                    "Catch DateTime - $dateFormated, Locality - $localName, Trap Number - ${it.trapID}, Weight - ${it.weight}, Gravidity - ${it.gravidity}, Lactating - ${it.lactating}, Sex. Active - $sexActive"
                logList.add(femaleLog)
            }
            adapter.setData(logList)
        } else {
            var maleLog: String
            mouseList.forEach {
                sexActive = if (it.sexActive == true) "Yes" else "No"
                newId = it.localityID
                if (oldId != newId) {
                    locality = localityViewModel.getLocality(args.mouse.localityID)
                    oldId = newId
                    newId = locality!!.localityId
                    localName = locality!!.localityName
                }
                val dateFormated = SimpleDateFormat.getDateTimeInstance().format(it.mouseDateTimeCreated)
                maleLog =
                    "Catch DateTime - $dateFormated, Locality - $localName, Trap Number - ${it.trapID}, Weight - ${it.weight}, Sex. Active - $sexActive"
                logList.add(maleLog)
            }
            adapter.setData(logList)
        }
    }

    private fun initMouseValuesToView() {
        binding.tvBodyLength.text = args.mouse.body.toString()
        binding.tvTailLength.text = args.mouse.tail.toString()
        binding.tvFeetLength.text = args.mouse.feet.toString()
        binding.tvCatchTime.text =
            SimpleDateFormat.getDateTimeInstance().format(args.mouse.mouseDateTimeCreated)
        binding.tvEar.text = args.mouse.ear.toString()
        binding.tvGravidity.text = if (args.mouse.gravidity == true) "Yes" else "No"
        binding.tvLactating.text = if (args.mouse.lactating == true) "Yes" else "No"
        binding.tvSexActive.text = if (args.mouse.sexActive == true) "Yes" else "No"
        binding.tvMouseAge.text = args.mouse.age.toString()
        binding.tvMouseSex.text = args.mouse.sex.toString()
        binding.tvMouseWeight.text = args.mouse.weight.toString()
        binding.tvMouseNote.text = args.mouse.note.toString()
        binding.tvTestesLength.text = args.mouse.testesLength.toString()
        binding.tvTestesWidth.text = args.mouse.testesWidth.toString()
        binding.tvEmbryoRight.text = args.mouse.embryoRight.toString()
        binding.tvEmbryoLeft.text = args.mouse.embryoLeft.toString()
        binding.tvEmbryoDiam.text = args.mouse.embryoDiameter.toString()
        binding.tvMc.text = if (args.mouse.MC == true) "Yes" else if (args.mouse.MC == false) "No" else "null"
        binding.tvMcLeft.text = args.mouse.MCleft.toString()
        binding.tvMcRight.text = args.mouse.MCright.toString()
    }
}