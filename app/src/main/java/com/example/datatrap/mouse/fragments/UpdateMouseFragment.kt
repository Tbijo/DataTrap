package com.example.datatrap.mouse.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentUpdateMouseBinding
import com.example.datatrap.models.*
import com.example.datatrap.models.tuples.SpecSelectList
import com.example.datatrap.myenums.EnumCaptureID
import com.example.datatrap.myenums.EnumMouseAge
import com.example.datatrap.myenums.EnumSex
import com.example.datatrap.viewmodels.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class UpdateMouseFragment : Fragment() {

    private var _binding: FragmentUpdateMouseBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<UpdateMouseFragmentArgs>()

    private val mouseViewModel: MouseViewModel by viewModels()
    private val specieViewModel: SpecieViewModel by viewModels()
    private val protocolViewModel: ProtocolViewModel by viewModels()
    private val mouseImageViewModel: MouseImageViewModel by viewModels()

    private lateinit var listSpecie: List<SpecSelectList>
    private lateinit var listProtocol: List<Protocol>
    private lateinit var trapList: List<Int>

    private lateinit var currentMouse: Mouse
    private var mouseImage: MouseImage? = null
    private var sex: String? = null
    private var age: String? = null
    private var captureID: String? = null
    private var specie: SpecSelectList? = null
    private var speciesID: Long? = null
    private var protocolID: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentUpdateMouseBinding.inflate(inflater, container, false)

        mouseViewModel.getMouse(args.mouseOccTuple.mouseId).observe(viewLifecycleOwner) {
            currentMouse = it
            initMouseValuesToView(it)
            fillDropDown(it)

            mouseImageViewModel.getImageForMouse(currentMouse.mouseIid, args.mouseOccTuple.deviceID).observe(viewLifecycleOwner) { image ->
                mouseImage = image
            }
        }

        mouseViewModel.getTrapsIdInOccasion(args.occList.occasionId).observe(viewLifecycleOwner) {
            trapList = it
        }

        setListeners()

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        mouseViewModel.getMouse(args.mouseOccTuple.mouseId).observe(viewLifecycleOwner) {
            fillDropDown(it)
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

    private fun fillDropDown(mouse: Mouse) {
        specieViewModel.getSpeciesForSelect().observe(viewLifecycleOwner) {
            listSpecie = it
            val listCode = mutableListOf<String>()
            it.forEach { specie ->
                listCode.add(specie.speciesCode)
            }
            val dropDownArrSpecie =
                ArrayAdapter(requireContext(), R.layout.dropdown_names, listCode)
            binding.autoCompTvSpecie.setAdapter(dropDownArrSpecie)

            // init values
            specie = it.first { specie ->
                specie.specieId == mouse.speciesID
            }
            speciesID = specie?.specieId
            binding.autoCompTvSpecie.setText(specie?.speciesCode, false)

        }

        protocolViewModel.procolList.observe(viewLifecycleOwner) {
            listProtocol = it
            val listProtName = mutableListOf<String>()
            listProtName.add("null")
            it.forEach { protocol ->
                listProtName.add(protocol.protocolName)
            }
            val dropDownArrProtocol =
                ArrayAdapter(requireContext(), R.layout.dropdown_names, listProtName)
            binding.autoCompTvProtocol.setAdapter(dropDownArrProtocol)

            // init values
            val protocol = it.firstOrNull { protocol ->
                protocol.protocolId == mouse.protocolID
            }
            protocolID = protocol?.protocolId
            binding.autoCompTvProtocol.setText(protocol.toString(), false)

        }

        val dropDownArrTrapID = ArrayAdapter(requireContext(), R.layout.dropdown_names, (1..args.occList.numTraps).toList())
        binding.autoCompTvTrapId.setAdapter(dropDownArrTrapID)

        binding.autoCompTvTrapId.setText(mouse.trapID.toString(), false)
    }

    private fun initMouseValuesToView(mouse: Mouse) {
        binding.cbGravit.isChecked = mouse.gravidity == true
        binding.cbLactating.isChecked = mouse.lactating == true
        binding.cbSexActive.isChecked = mouse.sexActive == true
        binding.cbMc.isChecked = mouse.MC == true

        binding.etMouseCodeUpdate.setText(mouse.code.toString())
        binding.etWeight.setText(mouse.weight.toString())
        binding.etTestesLength.setText(mouse.testesLength.toString())
        binding.etTestesWidth.setText(mouse.testesWidth.toString())
        binding.etMouseNote.setText(mouse.note.toString())

        binding.etBody.setText(mouse.body.toString())
        binding.etTail.setText(mouse.tail.toString())
        binding.etFeet.setText(mouse.feet.toString())
        binding.etEar.setText(mouse.ear.toString())

        binding.etEmbryoRight.setText(mouse.embryoRight.toString())
        binding.etEmbryoLeft.setText(mouse.embryoLeft.toString())
        binding.etEmbryoDiameter.setText(mouse.embryoDiameter.toString())
        binding.etMcRight.setText(mouse.MCright.toString())
        binding.etMcLeft.setText(mouse.MCleft.toString())

        when(mouse.sex) {
            EnumSex.MALE.myName -> {
                binding.rbMale.isChecked = true
                hideNonMaleFields()
            }
            EnumSex.FEMALE.myName -> binding.rbFemale.isChecked = true

            null -> {
                binding.rbNullSex.isChecked = true
                hideNonMaleFields()
            }
        }

        when(mouse.age) {
            EnumMouseAge.ADULT.myName -> binding.rbAdult.isChecked = true
            EnumMouseAge.SUBADULT.myName -> binding.rbSubadult.isChecked = true
            EnumMouseAge.JUVENILE.myName -> binding.rbJuvenile.isChecked = true
            null -> binding.rbNullAge.isChecked = true
        }

        when(mouse.captureID) {
            EnumCaptureID.CAPTURED.myName -> binding.rbCaptured.isChecked = true
            EnumCaptureID.DIED.myName -> binding.rbDied.isChecked = true
            EnumCaptureID.ESCAPED.myName -> binding.rbEscaped.isChecked = true
            EnumCaptureID.RELEASED.myName -> binding.rbReleased.isChecked = true
            null -> binding.rbNullCapture.isChecked = true
        }
    }

    private fun setListeners() {
        binding.autoCompTvSpecie.setOnItemClickListener { parent, _, position, _ ->
            val name: String = parent.getItemAtPosition(position) as String
            specie = listSpecie.first {
                it.speciesCode == name
            }
            speciesID = specie!!.specieId
        }

        binding.autoCompTvProtocol.setOnItemClickListener { parent, _, position, _ ->
            val name: String = parent.getItemAtPosition(position) as String
            protocolID = listProtocol.firstOrNull {
                it.protocolName == name
            }?.protocolId
        }

        binding.rgSex.setOnCheckedChangeListener{ _, radioButtonId ->
            when(radioButtonId){
                binding.rbMale.id -> {
                    sex = EnumSex.MALE.myName
                    hideNonMaleFields()
                }
                binding.rbFemale.id -> {
                    sex = EnumSex.FEMALE.myName
                    showNonMaleFields()
                }
                binding.rbNullSex.id -> {
                    sex = null
                    hideNonMaleFields()
                }
            }
        }

        binding.rgAge.setOnCheckedChangeListener { _, radioButtonId ->
            when(radioButtonId){
                binding.rbAdult.id -> age = EnumMouseAge.ADULT.myName
                binding.rbJuvenile.id -> age = EnumMouseAge.JUVENILE.myName
                binding.rbSubadult.id -> age = EnumMouseAge.SUBADULT.myName
                binding.rbNullAge.id -> age = null
            }
        }

        binding.rgCaptureId.setOnCheckedChangeListener { _, radioButtonId ->
            when(radioButtonId){
                binding.rbCaptured.id -> captureID = EnumCaptureID.CAPTURED.myName
                binding.rbDied.id -> captureID = EnumCaptureID.DIED.myName
                binding.rbEscaped.id -> captureID = EnumCaptureID.ESCAPED.myName
                binding.rbReleased.id -> captureID = EnumCaptureID.RELEASED.myName
                binding.rbNullCapture.id -> captureID = null
            }
        }
    }

    private fun hideNonMaleFields(){
        binding.cbGravit.isEnabled = false
        binding.cbGravit.isChecked = false
        binding.cbLactating.isEnabled = false
        binding.cbLactating.isChecked = false
        binding.cbMc.isEnabled = false
        binding.cbMc.isChecked = false
        binding.etMcRight.isEnabled = false
        binding.etMcRight.setText("")
        binding.etMcLeft.isEnabled = false
        binding.etMcLeft.setText("")
        binding.etEmbryoRight.isEnabled = false
        binding.etEmbryoRight.setText("")
        binding.etEmbryoLeft.isEnabled = false
        binding.etEmbryoLeft.setText("")
        binding.etEmbryoDiameter.isEnabled = false
        binding.etEmbryoDiameter.setText("")
    }

    private fun showNonMaleFields(){
        binding.cbGravit.isEnabled = true
        binding.cbLactating.isEnabled = true
        binding.cbMc.isEnabled = true
        binding.etMcRight.isEnabled = true
        binding.etMcLeft.isEnabled = true
        binding.etEmbryoRight.isEnabled = true
        binding.etEmbryoLeft.isEnabled = true
        binding.etEmbryoDiameter.isEnabled = true
    }

    private fun goToCamera() {
        val action = UpdateMouseFragmentDirections.actionUpdateMouseFragmentToTakePhotoFragment("Mouse",
            currentMouse.mouseIid, currentMouse.deviceID)
        findNavController().navigate(action)
    }

    private fun deleteMouse(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->

            mouseViewModel.deleteMouse(args.mouseOccTuple.mouseId, mouseImage?.path)

            Toast.makeText(requireContext(),"Mouse deleted.", Toast.LENGTH_LONG).show()

            findNavController().navigateUp()
        }
            .setNegativeButton("No"){_, _ -> }
            .setTitle("Delete Mouse?")
            .setMessage("Are you sure you want to delete this mouse?")
            .create().show()
    }

    private fun updateMouse() {
        if (speciesID != null){
            val mouse: Mouse = currentMouse.copy()
            mouse.speciesID = speciesID!!
            mouse.trapID = giveOutPutInt(binding.autoCompTvTrapId.text.toString())
            mouse.sex = sex
            mouse.age = age
            mouse.captureID = captureID
            mouse.mouseDateTimeUpdated = Calendar.getInstance().time

            mouse.protocolID = protocolID
            mouse.sexActive = binding.cbSexActive.isChecked
            mouse.weight = giveOutPutFloat(binding.etWeight.text.toString())

            mouse.body = giveOutPutFloat(binding.etBody.text.toString())
            mouse.tail = giveOutPutFloat(binding.etTail.text.toString())
            mouse.feet = giveOutPutFloat(binding.etFeet.text.toString())
            mouse.ear = giveOutPutFloat(binding.etEar.text.toString())

            mouse.testesLength = giveOutPutFloat(binding.etTestesLength.text.toString())
            mouse.testesWidth = giveOutPutFloat(binding.etTestesWidth.text.toString())

            mouse.gravidity = binding.cbGravit.isChecked
            mouse.lactating = binding.cbLactating.isChecked
            //počet embryí v oboch rohoch maternice a ich priemer
            mouse.embryoRight = if (sex == EnumSex.MALE.myName) null else giveOutPutInt(binding.etEmbryoRight.text.toString())
            mouse.embryoLeft = if (sex == EnumSex.MALE.myName) null else giveOutPutInt(binding.etEmbryoLeft.text.toString())
            mouse.embryoDiameter = if (sex == EnumSex.MALE.myName) null else giveOutPutFloat(binding.etEmbryoDiameter.text.toString())
            mouse.MC = binding.cbMc.isChecked
            //počet placentálnych polypov
            mouse.MCright = if (sex == EnumSex.MALE.myName) null else giveOutPutInt(binding.etMcRight.text.toString())
            mouse.MCleft = if (sex == EnumSex.MALE.myName) null else giveOutPutInt(binding.etMcLeft.text.toString())
            mouse.note = binding.etMouseNote.text.toString().ifBlank { null }

            // update mouse
            if (mouse.weight != null) {
                checkWeightAndSave(mouse)
            }
            else if (mouse.trapID != null && mouse.trapID in trapList) {
                checkTrapAvailability(mouse)
            }
            else {
                executeTask(mouse)
            }

        } else {
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun checkWeightAndSave(mouse: Mouse) {
        if (mouse.weight!! > specie?.maxWeight!! || mouse.weight!! < specie?.minWeight!!){
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton("Yes"){_, _ ->
                checkTrapAvailability(mouse)
            }
                .setNegativeButton("No"){_, _ -> }
                .setTitle("Warning: Mouse Weight")
                .setMessage("Mouse weight out of bounds, save anyway?")
                .create().show()
        }else{
            checkTrapAvailability(mouse)
        }
    }

    private fun checkTrapAvailability(mouse: Mouse) {
        if (mouse.trapID != null && mouse.trapID in trapList) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton("Yes") { _, _ ->
                executeTask(mouse)
            }
                .setNegativeButton("No") { _, _ -> }
                .setTitle("Warning: Trap In Use")
                .setMessage("Selected trap is in use, save anyway?")
                .create().show()
        } else {
            executeTask(mouse)
        }
    }

    private fun executeTask(mouse: Mouse) {
        mouseViewModel.updateMouse(mouse)

        Toast.makeText(requireContext(), "Mouse updated.", Toast.LENGTH_SHORT).show()

        findNavController().navigateUp()
    }

    private fun giveOutPutInt(input: String?): Int?{
        return if (input.isNullOrBlank() || input == "null") null else Integer.parseInt(input)
    }

    private fun giveOutPutFloat(input: String?): Float?{
        return if (input.isNullOrBlank() || input == "null") null else input.toFloat()
    }

}