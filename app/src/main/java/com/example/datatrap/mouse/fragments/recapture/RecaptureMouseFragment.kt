package com.example.datatrap.mouse.fragments.recapture

import android.app.AlertDialog
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentRecaptureMouseBinding
import com.example.datatrap.models.Mouse
import com.example.datatrap.models.Protocol
import com.example.datatrap.models.tuples.SpecSelectList
import com.example.datatrap.myenums.EnumCaptureID
import com.example.datatrap.myenums.EnumMouseAge
import com.example.datatrap.myenums.EnumSex
import com.example.datatrap.viewmodels.MouseViewModel
import com.example.datatrap.viewmodels.ProtocolViewModel
import com.example.datatrap.viewmodels.SpecieViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class RecaptureMouseFragment : Fragment() {

    private var _binding: FragmentRecaptureMouseBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<RecaptureMouseFragmentArgs>()

    private val mouseViewModel: MouseViewModel by viewModels()
    private val specieViewModel: SpecieViewModel by viewModels()
    private val protocolViewModel: ProtocolViewModel by viewModels()

    var deviceID: String = ""

    private lateinit var listSpecie: List<SpecSelectList>
    private lateinit var listProtocol: List<Protocol>
    private lateinit var trapList: List<Int>

    private lateinit var currentMouse: Mouse
    private var sex: String? = null
    private var age: String? = null
    private var captureID: String? = null
    private var specie: SpecSelectList? = null
    private var speciesID: Long? = null
    private var protocolID: Long? = null

    private var mouseId: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecaptureMouseBinding.inflate(inflater, container, false)

        mouseViewModel.getMouse(args.recapMouse.mouseId).observe(viewLifecycleOwner) {
            currentMouse = it
            initMouseValuesToView(it)
            fillDropDown(it)
        }

        mouseViewModel.getTrapsIdInOccasion(args.occList.occasionId).observe(viewLifecycleOwner) {
            trapList = it
        }

        deviceID = Settings.Secure.getString(
            requireContext().contentResolver,
            Settings.Secure.ANDROID_ID
        )

        setListeners()

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        mouseViewModel.getMouse(args.recapMouse.mouseId).observe(viewLifecycleOwner) {
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
        menu.findItem(R.id.menu_delete).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> recaptureMouse()
            R.id.menu_camera -> goToCamera()
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

        val dropDownArrTrapID =
            ArrayAdapter(
                requireContext(),
                R.layout.dropdown_names,
                (1..args.occList.numTraps).toList()
            )
        binding.autoCompTvTrapId.setAdapter(dropDownArrTrapID)

        binding.autoCompTvTrapId.setText(mouse.trapID.toString(), false)
    }

    private fun initMouseValuesToView(mouse: Mouse) {
        binding.cbGravit.isChecked = mouse.gravidity == true
        binding.cbLactating.isChecked = mouse.lactating == true
        binding.cbSexActive.isChecked = mouse.sexActive == true
        binding.cbMc.isChecked = mouse.MC == true

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

        when (mouse.sex) {
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

        when (mouse.age) {
            EnumMouseAge.ADULT.myName -> binding.rbAdult.isChecked = true
            EnumMouseAge.SUBADULT.myName -> binding.rbSubadult.isChecked = true
            EnumMouseAge.JUVENILE.myName -> binding.rbJuvenile.isChecked = true
            null -> binding.rbNullAge.isChecked = true
        }

        when (mouse.captureID) {
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

    private fun hideNonMaleFields() {
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

    private fun showNonMaleFields() {
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
        if (mouseId <= 0) {
            Toast.makeText(requireContext(), "You need to insert a mouse first.", Toast.LENGTH_LONG)
                .show()
            return
        }

        val action = RecaptureMouseFragmentDirections.actionRecaptureMouseFragmentToTakePhotoFragment(
                "Mouse",
            mouseId,
            currentMouse.deviceID
            )
        findNavController().navigate(action)
    }

    private fun recaptureMouse() {
        if (mouseId > 0) {
            Toast.makeText(requireContext(), "Mouse already inserted.", Toast.LENGTH_LONG).show()
            return
        }

        if (speciesID != null) {
            val mouse: Mouse = currentMouse.copy()
            mouse.mouseId = 0
            mouse.mouseIid = 0
            mouse.primeMouseID =
                if (args.recapMouse.primeMouseID == null) currentMouse.mouseIid else args.recapMouse.primeMouseID
            mouse.occasionID = args.occList.occasionId
            mouse.localityID = args.occList.localityID
            mouse.mouseDateTimeCreated = Calendar.getInstance().time
            mouse.mouseDateTimeUpdated = null
            mouse.recapture = true
            mouse.speciesID = speciesID!!
            mouse.trapID = giveOutPutInt(binding.autoCompTvTrapId.text.toString())
            mouse.sex = sex
            mouse.age = age
            mouse.captureID = captureID

            mouse.protocolID = protocolID
            mouse.sexActive = binding.cbSexActive.isChecked
            mouse.weight = giveOutPutFloat(binding.etWeight.text.toString())
            mouse.testesLength = giveOutPutFloat(binding.etTestesLength.text.toString())
            mouse.testesWidth = giveOutPutFloat(binding.etTestesWidth.text.toString())

            mouse.body = giveOutPutFloat(binding.etBody.text.toString())
            mouse.tail = giveOutPutFloat(binding.etTail.text.toString())
            mouse.feet = giveOutPutFloat(binding.etFeet.text.toString())
            mouse.ear = giveOutPutFloat(binding.etEar.text.toString())

            mouse.gravidity = binding.cbGravit.isChecked
            mouse.lactating = binding.cbLactating.isChecked
            //počet embryí v oboch rohoch maternice a ich priemer
            mouse.embryoRight =
                if (sex == EnumSex.MALE.myName) null else giveOutPutInt(binding.etEmbryoRight.text.toString())
            mouse.embryoLeft =
                if (sex == EnumSex.MALE.myName) null else giveOutPutInt(binding.etEmbryoLeft.text.toString())
            mouse.embryoDiameter =
                if (sex == EnumSex.MALE.myName) null else giveOutPutFloat(binding.etEmbryoDiameter.text.toString())
            mouse.MC = binding.cbMc.isChecked
            //počet placentálnych polypov
            mouse.MCright =
                if (sex == EnumSex.MALE.myName) null else giveOutPutInt(binding.etMcRight.text.toString())
            mouse.MCleft =
                if (sex == EnumSex.MALE.myName) null else giveOutPutInt(binding.etMcLeft.text.toString())

            mouse.note = binding.etMouseNote.text.toString().ifBlank { null }

            mouse.mouseCaught = Calendar.getInstance().time.time

            // recapture
            if (mouse.weight != null && specie?.minWeight != null && specie?.maxWeight != null) {
                checkWeightAndSave(mouse)
            }
            else if (mouse.trapID != null && mouse.trapID in trapList) {
                checkTrapAvailability(mouse)
            }
            else {
                executeTask(mouse)
            }

        } else {
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun checkWeightAndSave(mouse: Mouse) {
        if (mouse.weight!! > specie!!.maxWeight!! || mouse.weight!! < specie!!.minWeight!!) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton("Yes") { _, _ ->
                checkTrapAvailability(mouse)
            }
                .setNegativeButton("No") { _, _ -> }
                .setTitle("Warning: Mouse Weight")
                .setMessage("Mouse weight out of bounds, save anyway?")
                .create().show()
        } else {
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
        mouseViewModel.insertMouse(mouse)

        Toast.makeText(requireContext(), "Mouse recaptured.", Toast.LENGTH_SHORT).show()

        mouseViewModel.mouseId.observe(viewLifecycleOwner) {
            mouseId = it
        }
    }

    private fun giveOutPutInt(input: String?): Int? {
        return if (input.isNullOrBlank() || input == "null") null else Integer.parseInt(input)
    }

    private fun giveOutPutFloat(input: String?): Float? {
        return if (input.isNullOrBlank() || input == "null") null else input.toFloat()
    }

}