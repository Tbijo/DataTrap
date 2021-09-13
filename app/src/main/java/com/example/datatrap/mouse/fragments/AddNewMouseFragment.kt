package com.example.datatrap.mouse.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentAddNewMouseBinding
import com.example.datatrap.models.*
import com.example.datatrap.mouse.fragments.generator.CodeGenerator
import com.example.datatrap.viewmodels.*
import java.util.*

class AddNewMouseFragment : Fragment() {

    private var _binding: FragmentAddNewMouseBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<AddNewMouseFragmentArgs>()
    private lateinit var mouseViewModel: MouseViewModel
    private lateinit var specieViewModel: SpecieViewModel
    private lateinit var protocolViewModel: ProtocolViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var projectViewModel: ProjectViewModel
    private lateinit var sessionViewModel: SessionViewModel
    private lateinit var occasionViewModel: OccasionViewModel
    private lateinit var userViewModel: UserViewModel

    private lateinit var listSpecie: List<Specie>
    private lateinit var listProtocol: List<Protocol>
    private lateinit var mapSpecie: MutableMap<String, Long>
    private lateinit var mapProtocol: MutableMap<String?, Long>

    private var sex: String? = null
    private var imgName: String? = null
    private var age: String? = null
    private var captureID: String? = null
    private var code: Int = 0
    private var speciesID: Long = 0
    private var isMale: Boolean = false
    private var specie: Specie? = null
    private var oldCode: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentAddNewMouseBinding.inflate(inflater, container, false)
        projectViewModel = ViewModelProvider(this).get(ProjectViewModel::class.java)
        sessionViewModel = ViewModelProvider(this).get(SessionViewModel::class.java)
        occasionViewModel = ViewModelProvider(this).get(OccasionViewModel::class.java)
        mouseViewModel = ViewModelProvider(this).get(MouseViewModel::class.java)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        specieViewModel = ViewModelProvider(this).get(SpecieViewModel::class.java)
        listSpecie = specieViewModel.specieList.value!!
        listSpecie.forEach {
            mapSpecie[it.speciesCode] = it.specieId
        }

        protocolViewModel = ViewModelProvider(this).get(ProtocolViewModel::class.java)
        listProtocol = protocolViewModel.procolList.value!!
        listProtocol.forEach {
            mapProtocol[it.protocolName] = it.protocolId
        }

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        sharedViewModel.dataToShare.observe(requireActivity(), Observer {
            imgName = it
        })

        mouseViewModel.countMiceForLocality(args.occasion.localityID).observe(viewLifecycleOwner, Observer {
            code = it + 1
            oldCode = code
        })

        setListeners()

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val dropDownArrSpecie = ArrayAdapter(requireContext(), R.layout.dropdown_names, mapSpecie.keys.toList())
        binding.autoCompTvSpecie.setAdapter(dropDownArrSpecie)

        val dropDownArrProtocol = ArrayAdapter(requireContext(), R.layout.dropdown_names, mapProtocol.keys.toList())
        binding.autoCompTvProtocol.setAdapter(dropDownArrProtocol)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.mouse_menu, menu)
        menu.findItem(R.id.menu_delete).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_save -> insertMouse()
            R.id.menu_rat -> showDrawnRat()
            R.id.menu_camera -> goToCamera()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setListeners(){
        binding.rgSex.setOnCheckedChangeListener{ radioGroup, radioButtonId ->
            when(radioButtonId){
                binding.rbMale.id -> {
                    sex = "Male"
                    isMale = true
                    hideNonMaleFields()
                }
                binding.rbFemale.id -> {
                    sex = "Female"
                    isMale = false
                    showNonMaleFields()
                }
                binding.rbNullSex.id -> sex = null
            }
        }

        binding.rgAge.setOnCheckedChangeListener { radioGroup, radioButtonId ->
            age = when(radioButtonId){
                binding.rbAdult.id -> "Adult"
                binding.rbJuvenile.id -> "Juvenile"
                binding.rbSubadult.id -> "Subadult"
                binding.rbNullAge.id -> null
                else -> null
            }
        }

        binding.rgCaptureId.setOnCheckedChangeListener { radioGroup, radioButtonId ->
            captureID = when(radioButtonId){
                binding.rbCaptured.id -> "Captured"
                binding.rbDied.id -> "Died"
                binding.rbEscaped.id -> "Escaped"
                binding.rbReleased.id -> "Released"
                binding.rbNullCapture.id -> null
                else -> null
            }
        }

        binding.autoCompTvSpecie.setOnItemClickListener { parent, view, position, id ->
            val specieCode: String = parent.getItemAtPosition(position) as String
            listSpecie.forEach {
                if (it.speciesCode == specieCode){
                    specie = it
                }
            }
            val team: Int? = userViewModel.getActiveUser().value?.team
            val codeGen = CodeGenerator(this, oldCode, specie?.upperFingers!!, team, args.occasion.localityID)
            code = codeGen.generateCode()
            if(code == 0){
                Toast.makeText(requireContext(), "No code is available.", Toast.LENGTH_LONG).show()
            }
            binding.etCodeMouseAdd.setText(code.toString())
        }
    }

    private fun hideNonMaleFields(){
        binding.etEmbryoRight.visibility = View.INVISIBLE
        binding.etEmbryoLeft.visibility = View.INVISIBLE
        binding.etEmbryoDiameter.visibility = View.INVISIBLE
        binding.cbMc.visibility = View.INVISIBLE
        binding.etMcRight.visibility = View.INVISIBLE
        binding.etMcLeft.visibility = View.INVISIBLE
    }

    private fun showNonMaleFields(){
        binding.etEmbryoRight.visibility = View.VISIBLE
        binding.etEmbryoLeft.visibility = View.VISIBLE
        binding.etEmbryoDiameter.visibility = View.VISIBLE
        binding.cbMc.visibility = View.VISIBLE
        binding.etMcRight.visibility = View.VISIBLE
        binding.etMcLeft.visibility = View.VISIBLE
    }

    private fun showDrawnRat(){
        speciesID = mapSpecie.getValue(binding.autoCompTvSpecie.text.toString())
        if (speciesID > 0 && code > 0){
            val fragman = requireActivity().supportFragmentManager
            val floatFrag = DrawnFragment(code, specie?.upperFingers!!)
            floatFrag.show(fragman, "FloatFragMouseCode")
        }else{
            Toast.makeText(requireContext(), "Choose a specie.", Toast.LENGTH_LONG).show()
        }
    }

    private fun goToCamera() {
        val action = AddNewMouseFragmentDirections.actionAddNewMouseFragmentToTakePhotoFragment("Mouse", null)
        findNavController().navigate(action)
    }

    private fun insertMouse() {
        code = Integer.parseInt(binding.etCodeMouseAdd.text.toString())
        speciesID = mapSpecie.getValue(binding.autoCompTvSpecie.text.toString())
        val trapID: Int = Integer.parseInt(binding.etTrapId.text.toString())
        val protocolID: Long? = mapProtocol.getValue(binding.autoCompTvProtocol.text.toString())
        val gravitidy: Int? = if (binding.cbGravit.isChecked) 1 else 0
        val lactating: Int? = if (binding.cbLactating.isChecked) 1 else 0
        val sexActive: Int? = if (binding.cbSexActive.isChecked) 1 else 0
        val weight: Float? = Integer.parseInt(binding.etWeight.text.toString()).toFloat()
        val body: Float? = Integer.parseInt(binding.etBody.text.toString()).toFloat()
        val tail: Float? = Integer.parseInt(binding.etTail.text.toString()).toFloat()
        val feet: Float? = Integer.parseInt(binding.etFeet.text.toString()).toFloat()
        val ear: Float? = Integer.parseInt(binding.etEar.text.toString()).toFloat()
        val testesLength: Float? = Integer.parseInt(binding.etTestesLength.text.toString()).toFloat()
        val testesWidth: Float? = Integer.parseInt(binding.etTestesWidth.text.toString()).toFloat()
        //počet embryí v oboch rohoch maternice a ich priemer
        val embryoRight: Int? = if (isMale) null else Integer.parseInt(binding.etEmbryoRight.text.toString())
        val embryoLeft: Int? = if (isMale) null else Integer.parseInt(binding.etEmbryoLeft.text.toString())
        val embryoDiameter: Float? = if (isMale) null else Integer.parseInt(binding.etEmbryoDiameter.text.toString()).toFloat()
        val MC: Int? = if (isMale) null else { if (binding.cbMc.isChecked) 1 else 0 }
        //počet placentálnych polypov
        val MCright: Int? = if (isMale) null else Integer.parseInt(binding.etMcRight.text.toString())
        val MCleft: Int? = if (isMale) null else Integer.parseInt(binding.etMcLeft.text.toString())
        val note: String? = binding.etMouseNote.text.toString()

        if (checkInput(code, speciesID, trapID)){

            // zvacsit numMice projektu do ktoreho sa pridava tato mys
            updateProjectNumMice()

            // zvacsit numMice occasion do ktorej sa pridava tato mys
            updateOccasionNumMice()

            val mouse = Mouse(0, code, speciesID, protocolID, args.occasion.occasionId,
                args.occasion.localityID, trapID, Calendar.getInstance().time, sex, age, gravitidy, lactating, sexActive,
                weight, recapture = 0, captureID, body, tail, feet, ear, testesLength, testesWidth, embryoRight, embryoLeft,
                embryoDiameter, MC, MCright, MCleft, note, imgName)

            // ulozit mys
            checkWeightAndSave(mouse)

        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun checkWeightAndSave(mouse: Mouse) {

        if (mouse.weight!! > specie?.maxWeight!! || mouse.weight!! < specie?.minWeight!!){
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton("Yes"){_, _ ->
                mouseViewModel.insertMouse(mouse)

                Toast.makeText(requireContext(), "New mouse added.", Toast.LENGTH_SHORT).show()

                findNavController().navigateUp()
            }
                .setNegativeButton("No"){_, _ -> }
                .setTitle("Warning: Mouse Weight")
                .setMessage("Mouse weight out of bounds, save anyway?")
                .create().show()
        }else{
            mouseViewModel.insertMouse(mouse)

            Toast.makeText(requireContext(), "New mouse added.", Toast.LENGTH_SHORT).show()

            findNavController().navigateUp()
        }
    }

    private fun updateProjectNumMice(){
        val session: Session = sessionViewModel.getSession(args.occasion.sessionID).value!!
        val updatedProject: Project = projectViewModel.getProject(session.projectID!!).value!!

        updatedProject.numMice = (updatedProject.numMice + 1)

        projectViewModel.updateProject(updatedProject)
    }

    private fun updateOccasionNumMice(){
        val updatedOccasion: Occasion = args.occasion
        updatedOccasion.numMice = (updatedOccasion.numMice?.plus(1))

        occasionViewModel.updateOccasion(updatedOccasion)
    }

    private fun checkInput(code: Int, specieID: Long, trapID: Int): Boolean {
        return code > 0 && specieID > 0 && trapID.toString().isNotEmpty()
    }

}