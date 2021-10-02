package com.example.datatrap.mouse.fragments.listinview

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datatrap.databinding.FragmentViewMouseBinding
import com.example.datatrap.models.Locality
import com.example.datatrap.models.Mouse
import com.example.datatrap.models.Occasion
import com.example.datatrap.models.Session
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
    private lateinit var adapter: MouseHistRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentViewMouseBinding.inflate(inflater, container, false)
        specieViewModel = ViewModelProvider(this).get(SpecieViewModel::class.java)
        occasionViewModel = ViewModelProvider(this).get(OccasionViewModel::class.java)
        sessionViewModel = ViewModelProvider(this).get(SessionViewModel::class.java)
        projectViewModel = ViewModelProvider(this).get(ProjectViewModel::class.java)
        mouseViewModel = ViewModelProvider(this).get(MouseViewModel::class.java)
        localityViewModel = ViewModelProvider(this).get(LocalityViewModel::class.java)

        initMouseValuesToView()

        specieViewModel.getSpecie(args.mouse.speciesID).observe(viewLifecycleOwner, Observer {
            binding.tvMouseFullName.text = it.fullName
            binding.tvSpecie.text = it.speciesCode
        })

        var occasion: Occasion? = null
        occasionViewModel.getOccasion(args.mouse.occasionID).observe(viewLifecycleOwner, Observer {
            binding.tvMouseLeg.text = it.leg
            occasion = it
        })

        var session: Session? = null
        sessionViewModel.getSession(occasion!!.sessionID).observe(viewLifecycleOwner, Observer {
            session = it
        })

        projectViewModel.getProject(session?.projectID!!).observe(viewLifecycleOwner, Observer {
            binding.tvMouseProjectName.text = it.projectName
        })

        adapter = MouseHistRecyclerAdapter()
        binding.recyclerMouse.adapter = adapter
        binding.recyclerMouse.layoutManager = LinearLayoutManager(requireContext())

        adapter.setData(fillList())

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

    // naplnit list male alebo female predchadzajucimi udajmi o jedincovi
    private fun fillList(): List<String>{
        val logList = mutableListOf<String>()
        var mouseList = emptyList<Mouse>()
        mouseViewModel.getMiceForLog(args.mouse.mouseId).observe(viewLifecycleOwner, Observer {
            mouseList = it
        })

        var locality: Locality?
        var sexActive: String
        var oldId: Long = 0
        var newId: Long
        var localName = ""

        if (args.mouse.sex == "Male"){
            var maleLog: String
            mouseList.forEach {
                sexActive = if (it.sexActive == 1) "Yes" else "No"
                newId = it.localityID
                if (oldId != newId){
                    locality = localityViewModel.getLocality(args.mouse.localityID).value!!
                    oldId = newId
                    newId = locality!!.localityId
                    localName = locality!!.localityName
                }
                maleLog = "Catch DateTime - ${it.catchDateTime} Locality - $localName Trap Number - ${it.trapID} Weight - ${it.weight} Sex. Active - $sexActive"
                logList.add(maleLog)
            }
            return logList
        }else{
            var femaleLog: String
            mouseList.forEach {
                sexActive = if (it.sexActive == 1) "Yes" else "No"
                newId = it.localityID
                if (oldId != newId){
                    locality = localityViewModel.getLocality(args.mouse.localityID).value!!
                    oldId = newId
                    newId = locality!!.localityId
                    localName = locality!!.localityName
                }
                femaleLog = "Catch DateTime - ${it.catchDateTime} Locality - $localName Trap Number - ${it.trapID} Weight - ${it.weight} Gravidity - ${it.gravidity} Lactating - ${it.lactating} Sex. Active - $sexActive"
                logList.add(femaleLog)
            }
            return logList
        }

    }

    private fun initMouseValuesToView(){
        binding.tvBodyLength.text = args.mouse.body.toString()
        binding.tvTailLength.text = args.mouse.tail.toString()
        binding.tvFeetLength.text = args.mouse.feet.toString()
        binding.tvCatchTime.text = SimpleDateFormat.getDateTimeInstance().format(args.mouse.catchDateTime)
        binding.tvEar.text = args.mouse.ear.toString()
        binding.tvGravidity.text = if (args.mouse.gravidity == 1) "Yes" else "No"
        binding.tvLactating.text = if (args.mouse.lactating == 1) "Yes" else "No"
        binding.tvSexActive.text = if (args.mouse.sexActive == 1) "Yes" else "No"
        binding.tvMouseAge.text = args.mouse.age
        binding.tvMouseSex.text = args.mouse.sex
        binding.tvMouseWeight.text = args.mouse.weight.toString()
        binding.tvMouseNote.text = args.mouse.note
        binding.tvTestesLength.text = args.mouse.testesLength.toString()
        binding.tvTestesWidth.text = args.mouse.testesWidth.toString()
        binding.tvEmbryoRight.text = args.mouse.embryoRight.toString()
        binding.tvEmbryoLeft.text = args.mouse.embryoLeft.toString()
        binding.tvEmbryoDiam.text = args.mouse.embryoDiameter.toString()
        binding.tvMc.text = if (args.mouse.MC == 1) "Yes" else "No"
        binding.tvMcLeft.text = args.mouse.MCleft.toString()
        binding.tvMcRight.text = args.mouse.MCright.toString()
    }
}