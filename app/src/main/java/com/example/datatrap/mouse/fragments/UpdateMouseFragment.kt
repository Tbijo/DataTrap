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
import com.example.datatrap.databinding.FragmentUpdateMouseBinding
import com.example.datatrap.models.*
import com.example.datatrap.viewmodels.*

class UpdateMouseFragment : Fragment() {

    private var _binding: FragmentUpdateMouseBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<UpdateMouseFragmentArgs>()
    private lateinit var mouseViewModel: MouseViewModel
    private lateinit var specieViewModel: SpecieViewModel
    private lateinit var protocolViewModel: ProtocolViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var projectViewModel: ProjectViewModel
    private lateinit var sessionViewModel: SessionViewModel
    private lateinit var occasionViewModel: OccasionViewModel

    private lateinit var listSpecie: List<Specie>
    private lateinit var listProtocol: List<Protocol>
    private lateinit var mapSpecie: MutableMap<String, Long>
    private lateinit var mapProtocol: MutableMap<String?, Long>

    private var sex: String? = null
    private var imgName: String? = null
    private var age: String? = null
    private var captureID: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentUpdateMouseBinding.inflate(inflater, container, false)
        projectViewModel = ViewModelProvider(this).get(ProjectViewModel::class.java)
        sessionViewModel = ViewModelProvider(this).get(SessionViewModel::class.java)
        occasionViewModel = ViewModelProvider(this).get(OccasionViewModel::class.java)
        mouseViewModel = ViewModelProvider(this).get(MouseViewModel::class.java)

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

        setListeners()

        initMouseValuesToView()

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val dropDownArrSpecie = ArrayAdapter(requireContext(), R.layout.dropdown_names, mapSpecie.keys.toList())
        binding.autoCompTvSpecie.setAdapter(dropDownArrSpecie)

        val dropDownArrProtocol = ArrayAdapter(requireContext(), R.layout.dropdown_names, mapProtocol.keys.toList())
        binding.autoCompTvProtocol.setAdapter(dropDownArrProtocol)

        mapSpecie.forEach {
            if (it.value == args.mouse.speciesID){
                binding.autoCompTvSpecie.setText(it.key, false)
            }
        }
        mapProtocol.forEach {
            if (it.value == args.mouse.protocolID){
                binding.autoCompTvProtocol.setText(it.key, false)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.mouse_menu, menu)
        menu.findItem(R.id.menu_rat).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_save -> updateMouse()
            R.id.menu_camera -> goToCamera()
            R.id.menu_delete -> deleteMouse()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initMouseValuesToView(){
        binding.cbGravit.isChecked = args.mouse.gravidity == 1
        binding.cbLactating.isChecked = args.mouse.lactating == 1
        binding.cbSexActive.isChecked = args.mouse.sexActive == 1
        binding.cbMc.isChecked = args.mouse.MC == 1

        binding.etMouseCodeUpdate.setText(args.mouse.code.toString())
        binding.etWeight.setText(args.mouse.weight.toString())
        binding.etBody.setText(args.mouse.body.toString())
        binding.etTail.setText(args.mouse.tail.toString())
        binding.etFeet.setText(args.mouse.feet.toString())
        binding.etTrapId.setText(args.mouse.trapID.toString())
        binding.etEar.setText(args.mouse.ear.toString())
        binding.etTestesLength.setText(args.mouse.testesLength.toString())
        binding.etTestesWidth.setText(args.mouse.testesWidth.toString())
        binding.etEmbryoRight.setText(args.mouse.embryoRight.toString())
        binding.etEmbryoLeft.setText(args.mouse.embryoLeft.toString())
        binding.etEmbryoDiameter.setText(args.mouse.embryoDiameter.toString())
        binding.etMouseNote.setText(args.mouse.note)
        binding.etMcRight.setText(args.mouse.MCright.toString())
        binding.etMcLeft.setText(args.mouse.MCleft.toString())

        when(args.mouse.sex){
            "Male" -> binding.rbMale.isChecked = true
            "Female" -> binding.rbFemale.isChecked = true
            null -> binding.rbNullSex.isChecked = true
        }

        when(args.mouse.age){
            "Adult" -> binding.rbAdult.isChecked = true
            "Subadult" -> binding.rbSubadult.isChecked = true
            "Juvenile" -> binding.rbJuvenile.isChecked = true
            null -> binding.rbNullAge.isChecked = true
        }

        when(args.mouse.captureID){
            "Captured" -> binding.rbCaptured.isChecked = true
            "Died" -> binding.rbDied.isChecked = true
            "Escaped" -> binding.rbEscaped.isChecked = true
            "Released" -> binding.rbReleased.isChecked = true
            null -> binding.rbNullCapture.isChecked = true
        }
    }

    private fun setListeners(){
        binding.rgSex.setOnCheckedChangeListener{ radioGroup, radioButtonId ->
            sex = when(radioButtonId){
                binding.rbMale.id -> "Male"
                binding.rbFemale.id -> "Female"
                binding.rbNullSex.id -> null
                else -> null
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
    }

    private fun goToCamera() {
        val action = UpdateMouseFragmentDirections.actionUpdateMouseFragmentToTakePhotoFragment("Mouse", args.mouse.imgName)
        findNavController().navigate(action)
    }

    private fun deleteMouse(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->
            // zmensit numMice projektu do ktoreho sa pridava tato mys
            updateProjectNumMice()

            // zmensit numMice occasion do ktorej sa pridava tato mys
            updateOccasionNumMice()

            mouseViewModel.deleteMouse(args.mouse)

            Toast.makeText(requireContext(),"Mouse deleted.", Toast.LENGTH_LONG).show()
            findNavController().navigateUp()
        }
            .setNegativeButton("No"){_, _ -> }
            .setTitle("Delete Mouse?")
            .setMessage("Are you sure you want to delete this mouse?")
            .create().show()
    }

    private fun updateProjectNumMice(){
        val session: Session = sessionViewModel.getSession(args.occasion.sessionID).value!!
        val updatedProject: Project = projectViewModel.getProject(session.projectID!!).value!!
        updatedProject.numMice = (updatedProject.numMice - 1)
        projectViewModel.updateProject(updatedProject)
    }

    private fun updateOccasionNumMice(){
        val updatedOccasion: Occasion = args.occasion
        updatedOccasion.numMice = (updatedOccasion.numMice?.minus(1))
        occasionViewModel.updateOccasion(updatedOccasion)
    }

    private fun updateMouse() {
        val code: Int? = Integer.parseInt(binding.etMouseCodeUpdate.text.toString())
        val speciesID: Long = mapSpecie.getValue(binding.autoCompTvSpecie.text.toString())
        val trapID: Int = Integer.parseInt(binding.etTrapId.text.toString())

        if (checkInput(code, speciesID, trapID)){
            val mouse: Mouse = args.mouse
            mouse.code = code
            mouse.speciesID = speciesID
            mouse.trapID = trapID
            mouse.sex = sex
            mouse.age = age
            mouse.captureID = captureID
            mouse.protocolID = mapProtocol.getValue(binding.autoCompTvProtocol.text.toString())
            mouse.gravidity = if (binding.cbGravit.isChecked) 1 else 0
            mouse.lactating = if (binding.cbLactating.isChecked) 1 else 0
            mouse.sexActive = if (binding.cbSexActive.isChecked) 1 else 0
            mouse.weight = Integer.parseInt(binding.etWeight.text.toString()).toFloat()
            mouse.body = Integer.parseInt(binding.etBody.text.toString()).toFloat()
            mouse.tail = Integer.parseInt(binding.etTail.text.toString()).toFloat()
            mouse.feet = Integer.parseInt(binding.etFeet.text.toString()).toFloat()
            mouse.ear = Integer.parseInt(binding.etEar.text.toString()).toFloat()
            mouse.testesLength = Integer.parseInt(binding.etTestesLength.text.toString()).toFloat()
            mouse.testesWidth = Integer.parseInt(binding.etTestesWidth.text.toString()).toFloat()
            //počet embryí v oboch rohoch maternice a ich priemer
            mouse.embryoRight = Integer.parseInt(binding.etEmbryoRight.text.toString())
            mouse.embryoLeft = Integer.parseInt(binding.etEmbryoLeft.text.toString())
            mouse.embryoDiameter = Integer.parseInt(binding.etEmbryoDiameter.text.toString()).toFloat()
            mouse.MC = if (binding.cbMc.isChecked) 1 else 0
            //počet placentálnych polypov
            mouse.MCright = Integer.parseInt(binding.etMcRight.text.toString())
            mouse.MCleft = Integer.parseInt(binding.etMcLeft.text.toString())
            mouse.note = binding.etMouseNote.text.toString()
            mouse.imgName = imgName

            // update mouse
            checkWeightAndSave(mouse)

        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun checkWeightAndSave(mouse: Mouse) {
        val specie: Specie = specieViewModel.getSpecie(mouse.speciesID).value!!

        if (mouse.weight!! > specie.maxWeight!! || mouse.weight!! < specie.minWeight!!){
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton("Yes"){_, _ ->
                mouseViewModel.insertMouse(mouse)

                Toast.makeText(requireContext(), "Mouse updated.", Toast.LENGTH_SHORT).show()

                findNavController().navigateUp()
            }
                .setNegativeButton("No"){_, _ -> }
                .setTitle("Warning: Mouse Weight")
                .setMessage("Mouse weight out of bounds, save anyway?")
                .create().show()
        }else{
            mouseViewModel.insertMouse(mouse)

            Toast.makeText(requireContext(), "Mouse updated.", Toast.LENGTH_SHORT).show()

            findNavController().navigateUp()
        }
    }

    private fun checkInput(code: Int?, specieID: Long, trapID: Int): Boolean {
        return code.toString().isNotEmpty() && specieID.toString().isNotEmpty() && trapID.toString().isNotEmpty()
    }

}