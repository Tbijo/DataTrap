package com.example.datatrap.mouse.presentation.mouse_recapture

import android.app.AlertDialog
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.datatrap.R
import com.example.datatrap.core.util.EnumCaptureID
import com.example.datatrap.core.util.EnumMouseAge
import com.example.datatrap.core.util.EnumSex
import com.example.datatrap.mouse.data.MouseEntity
import com.example.datatrap.mouse.data.MouseRepository
import com.example.datatrap.mouse.domain.model.MouseRecapList
import com.example.datatrap.mouse.domain.model.SearchMouse
import com.example.datatrap.mouse.presentation.mouse_list.MouseListViewModel
import com.example.datatrap.specie.data.SpecieRepository
import com.example.datatrap.specie.presentation.specie_list.SpecieListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class RecaptureViewModel @Inject constructor(
    private val mouseRepository: MouseRepository,
    private val specieRepository: SpecieRepository,
): ViewModel() {

    private val _state = MutableStateFlow(RecaptureUiState())
    val state = _state.asStateFlow()

    init {
        // recapture
        mouseListViewModel.getMouse(args.recapMouse.mouseId).observe(viewLifecycleOwner) {
            currentMouseEntity = it
            initMouseValuesToView(it)
            fillDropDown(it)
        }

        mouseListViewModel.getTrapsIdInOccasion(args.occList.occasionId).observe(viewLifecycleOwner) {
            trapList = it
        }

        specieListViewModel.getSpeciesForSelect().observe(viewLifecycleOwner) {
            it.forEach { specie ->
                specieMap[specie.speciesCode] = specie.specieId
            }
        }
    }

    private fun fillDropDown(mouseEntity: MouseEntity) {
        specieListViewModel.getSpeciesForSelect().observe(viewLifecycleOwner) {
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
                specie.specieId == mouseEntity.speciesID
            }
            speciesID = specie?.specieId
            binding.autoCompTvSpecie.setText(specie?.speciesCode, false)

        }

        protocolViewModel.procolList.observe(viewLifecycleOwner) {
            listProtocolEntity = it
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
                protocol.protocolId == mouseEntity.protocolID
            }
            protocolID = protocol?.protocolId
            binding.autoCompTvProtocol.setText(protocol.toString(), false)

        }

        val dropDownArrTrapID =
            ArrayAdapter(
                requireContext(),
                R.layout.dropdown_names,
                (1..args.occList.numTraps).toList()
            )
        binding.autoCompTvTrapId.setAdapter(dropDownArrTrapID)

        binding.autoCompTvTrapId.setText(mouseEntity.trapID.toString(), false)
    }

    private fun initMouseValuesToView(mouseEntity: MouseEntity) {
        binding.cbGravit.isChecked = mouseEntity.gravidity == true
        binding.cbLactating.isChecked = mouseEntity.lactating == true
        binding.cbSexActive.isChecked = mouseEntity.sexActive == true
        binding.cbMc.isChecked = mouseEntity.MC == true

        binding.etWeight.setText(mouseEntity.weight.toString())
        binding.etTestesLength.setText(mouseEntity.testesLength.toString())
        binding.etTestesWidth.setText(mouseEntity.testesWidth.toString())
        binding.etMouseNote.setText(mouseEntity.note.toString())

        binding.etBody.setText(mouseEntity.body.toString())
        binding.etTail.setText(mouseEntity.tail.toString())
        binding.etFeet.setText(mouseEntity.feet.toString())
        binding.etEar.setText(mouseEntity.ear.toString())

        binding.etEmbryoRight.setText(mouseEntity.embryoRight.toString())
        binding.etEmbryoLeft.setText(mouseEntity.embryoLeft.toString())
        binding.etEmbryoDiameter.setText(mouseEntity.embryoDiameter.toString())
        binding.etMcRight.setText(mouseEntity.MCright.toString())
        binding.etMcLeft.setText(mouseEntity.MCleft.toString())

        when (mouseEntity.sex) {
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

        when (mouseEntity.age) {
            EnumMouseAge.ADULT.myName -> binding.rbAdult.isChecked = true
            EnumMouseAge.SUBADULT.myName -> binding.rbSubadult.isChecked = true
            EnumMouseAge.JUVENILE.myName -> binding.rbJuvenile.isChecked = true
            null -> binding.rbNullAge.isChecked = true
        }

        when (mouseEntity.captureID) {
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
            protocolID = listProtocolEntity.firstOrNull {
                it.protocolName == name
            }?.protocolId
        }

        binding.rgSex.setOnCheckedChangeListener { _, radioButtonId ->
            when (radioButtonId) {
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
            when (radioButtonId) {
                binding.rbAdult.id -> age = EnumMouseAge.ADULT.myName
                binding.rbJuvenile.id -> age = EnumMouseAge.JUVENILE.myName
                binding.rbSubadult.id -> age = EnumMouseAge.SUBADULT.myName
                binding.rbNullAge.id -> age = null
            }
        }

        binding.rgCaptureId.setOnCheckedChangeListener { _, radioButtonId ->
            when (radioButtonId) {
                binding.rbCaptured.id -> captureID = EnumCaptureID.CAPTURED.myName
                binding.rbDied.id -> captureID = EnumCaptureID.DIED.myName
                binding.rbEscaped.id -> captureID = EnumCaptureID.ESCAPED.myName
                binding.rbReleased.id -> captureID = EnumCaptureID.RELEASED.myName
                binding.rbNullCapture.id -> captureID = null
            }
        }
    }

    private fun recaptureMouse() {
        if (mouseId > 0) {
            Toast.makeText(requireContext(), "Mouse already inserted.", Toast.LENGTH_LONG).show()
            return
        }

        if (speciesID == null) {
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
            return
        }
        val mouseEntity: MouseEntity = currentMouseEntity.copy()
        mouseEntity.mouseId = 0
        mouseEntity.primeMouseID = if (args.recapMouse.primeMouseID == null) currentMouseEntity.mouseIid else args.recapMouse.primeMouseID
        mouseEntity.occasionID = args.occList.occasionId
        mouseEntity.localityID = args.occList.localityID
        mouseEntity.mouseDateTimeCreated = Calendar.getInstance().time
        mouseEntity.mouseDateTimeUpdated = null
        mouseEntity.recapture = true
        mouseEntity.speciesID = speciesID!!
        mouseEntity.trapID = giveOutPutInt(binding.autoCompTvTrapId.text.toString())
        mouseEntity.sex = sex
        mouseEntity.age = age
        mouseEntity.captureID = captureID

        mouseEntity.protocolID = protocolID
        mouseEntity.sexActive = binding.cbSexActive.isChecked
        mouseEntity.weight = giveOutPutFloat(binding.etWeight.text.toString())
        mouseEntity.testesLength = giveOutPutFloat(binding.etTestesLength.text.toString())
        mouseEntity.testesWidth = giveOutPutFloat(binding.etTestesWidth.text.toString())

        mouseEntity.body = giveOutPutFloat(binding.etBody.text.toString())
        mouseEntity.tail = giveOutPutFloat(binding.etTail.text.toString())
        mouseEntity.feet = giveOutPutFloat(binding.etFeet.text.toString())
        mouseEntity.ear = giveOutPutFloat(binding.etEar.text.toString())

        mouseEntity.gravidity = binding.cbGravit.isChecked
        mouseEntity.lactating = binding.cbLactating.isChecked
        //počet embryí v oboch rohoch maternice a ich priemer
        mouseEntity.embryoRight =
            if (sex == EnumSex.MALE.myName) null else giveOutPutInt(binding.etEmbryoRight.text.toString())
        mouseEntity.embryoLeft =
            if (sex == EnumSex.MALE.myName) null else giveOutPutInt(binding.etEmbryoLeft.text.toString())
        mouseEntity.embryoDiameter =
            if (sex == EnumSex.MALE.myName) null else giveOutPutFloat(binding.etEmbryoDiameter.text.toString())
        mouseEntity.MC = binding.cbMc.isChecked
        //počet placentálnych polypov
        mouseEntity.MCright =
            if (sex == EnumSex.MALE.myName) null else giveOutPutInt(binding.etMcRight.text.toString())
        mouseEntity.MCleft =
            if (sex == EnumSex.MALE.myName) null else giveOutPutInt(binding.etMcLeft.text.toString())

        mouseEntity.note = binding.etMouseNote.text.toString().ifBlank { null }

        mouseEntity.mouseCaught = Calendar.getInstance().time.time

        // recapture
        if (mouseEntity.weight != null && specie?.minWeight != null && specie?.maxWeight != null) {
            checkWeightAndSave(mouseEntity)
        }
        else if (mouseEntity.trapID != null && mouseEntity.trapID in trapList) {
            checkTrapAvailability(mouseEntity)
        }
        else {
            mouseRepository.insertMouse(mouseEntity)
        }
    }

    private fun checkWeightAndSave(mouseEntity: MouseEntity) {
        if (mouseEntity.weight!! > specie!!.maxWeight!! || mouseEntity.weight!! < specie!!.minWeight!!) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton("Yes") { _, _ ->
                checkTrapAvailability(mouseEntity)
            }
                .setNegativeButton("No") { _, _ -> }
                .setTitle("Warning: Mouse Weight")
                .setMessage("Mouse weight out of bounds, save anyway?")
                .create().show()
        } else {
            checkTrapAvailability(mouseEntity)
        }
    }

    private fun checkTrapAvailability(mouseEntity: MouseEntity) {
        if (mouseEntity.trapID != null && mouseEntity.trapID in trapList) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton("Yes") { _, _ ->
                mouseRepository.insertMouse(mouseEntity)
            }
                .setNegativeButton("No") { _, _ -> }
                .setTitle("Warning: Trap In Use")
                .setMessage("Selected trap is in use, save anyway?")
                .create().show()
        } else {
            mouseRepository.insertMouse(mouseEntity)
        }
    }

    private fun goToCamera() {
        if (mouseId <= 0) {
            Toast.makeText(requireContext(), "You need to insert a mouse first.", Toast.LENGTH_LONG)
                .show()
            return
        }

        val action = RecaptureMouseFragmentDirections.actionRecaptureMouseFragmentToTakePhotoFragment(
            "Mouse",
            mouseId,
            currentMouseEntity.deviceID
        )
        findNavController().navigate(action)
    }

}