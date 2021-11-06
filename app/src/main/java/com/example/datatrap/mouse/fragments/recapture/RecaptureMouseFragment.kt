package com.example.datatrap.mouse.fragments.recapture

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentRecaptureMouseBinding
import com.example.datatrap.models.Mouse
import com.example.datatrap.models.Specie
import com.example.datatrap.myenums.EnumCaptureID
import com.example.datatrap.myenums.EnumMouseAge
import com.example.datatrap.myenums.EnumSex
import com.example.datatrap.myenums.EnumTrapID
import com.example.datatrap.viewmodels.MouseViewModel
import com.example.datatrap.viewmodels.ProtocolViewModel
import com.example.datatrap.viewmodels.SharedViewModel
import com.example.datatrap.viewmodels.SpecieViewModel
import java.util.*

class RecaptureMouseFragment : Fragment() {

    private var _binding: FragmentRecaptureMouseBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<RecaptureMouseFragmentArgs>()
    private lateinit var mouseViewModel: MouseViewModel
    private lateinit var specieViewModel: SpecieViewModel
    private lateinit var protocolViewModel: ProtocolViewModel
    private lateinit var sharedViewModel: SharedViewModel

    private lateinit var listSpecie: List<Specie>
    private lateinit var mapSpecie: MutableMap<String, Long>
    private lateinit var mapProtocol: MutableMap<String?, Long?>

    private var sex: String? = null
    private var imgName: String? = null
    private var age: String? = null
    private var captureID: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecaptureMouseBinding.inflate(inflater, container, false)

        mouseViewModel = ViewModelProvider(this).get(MouseViewModel::class.java)

        specieViewModel = ViewModelProvider(this).get(SpecieViewModel::class.java)
        protocolViewModel = ViewModelProvider(this).get(ProtocolViewModel::class.java)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        sharedViewModel.dataToShare.observe(requireActivity(), Observer {
            imgName = it
        })

        mapSpecie = mutableMapOf()
        mapProtocol = mutableMapOf()

        fillDropDown()

        setListeners()
        setListenerToTrapID()

        initMouseValuesToView()

        // zmena pohlavia
        mouseViewModel.gotMouse.observe(viewLifecycleOwner, { mouse ->
            mouse.sex = sex
            mouse.mouseDateTimeUpdated = Calendar.getInstance().time
            mouseViewModel.updateMouse(mouse)
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        fillDropDown()

        val dropDownArrTrapID =
            ArrayAdapter(requireContext(), R.layout.dropdown_names, EnumTrapID.myValues())
        binding.autoCompTvTrapId.setAdapter(dropDownArrTrapID)

        binding.autoCompTvTrapId.setText(args.mouse.trapID, false)
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

    private fun fillDropDown() {
        specieViewModel.specieList.observe(viewLifecycleOwner, {
            listSpecie = it
            it.forEach { specie ->
                mapSpecie[specie.speciesCode] = specie.specieId
            }
            val dropDownArrSpecie =
                ArrayAdapter(requireContext(), R.layout.dropdown_names, mapSpecie.keys.toList())
            binding.autoCompTvSpecie.setAdapter(dropDownArrSpecie)

            // init values
            mapSpecie.forEach { map ->
                if (map.value == args.mouse.speciesID) {
                    binding.autoCompTvSpecie.setText(map.key, false)
                }
            }
        })

        protocolViewModel.procolList.observe(viewLifecycleOwner, {
            it.forEach { protocol ->
                mapProtocol[protocol.protocolName] = protocol.protocolId
            }
            mapProtocol["null"] = null

            val dropDownArrProtocol =
                ArrayAdapter(requireContext(), R.layout.dropdown_names, mapProtocol.keys.toList())
            binding.autoCompTvProtocol.setAdapter(dropDownArrProtocol)

            // init values
            mapProtocol.forEach { map ->
                if (map.value == args.mouse.protocolID) {
                    binding.autoCompTvProtocol.setText(map.key, false)
                }
            }
        })
    }

    private fun initMouseValuesToView() {
        binding.cbGravit.isChecked = args.mouse.gravidity == true
        binding.cbLactating.isChecked = args.mouse.lactating == true
        binding.cbSexActive.isChecked = args.mouse.sexActive == true
        binding.cbMc.isChecked = args.mouse.MC == true

        binding.etWeight.setText(args.mouse.weight.toString())
        binding.etTestesLength.setText(args.mouse.testesLength.toString())
        binding.etTestesWidth.setText(args.mouse.testesWidth.toString())
        binding.etMouseNote.setText(args.mouse.note.toString())

        binding.etBody.setText(args.mouse.body.toString())
        binding.etTail.setText(args.mouse.tail.toString())
        binding.etFeet.setText(args.mouse.feet.toString())
        binding.etEar.setText(args.mouse.ear.toString())

        binding.etEmbryoRight.setText(args.mouse.embryoRight.toString())
        binding.etEmbryoLeft.setText(args.mouse.embryoLeft.toString())
        binding.etEmbryoDiameter.setText(args.mouse.embryoDiameter.toString())
        binding.etMcRight.setText(args.mouse.MCright.toString())
        binding.etMcLeft.setText(args.mouse.MCleft.toString())

        when (args.mouse.sex) {
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

        when (args.mouse.age) {
            EnumMouseAge.ADULT.myName -> binding.rbAdult.isChecked = true
            EnumMouseAge.SUBADULT.myName -> binding.rbSubadult.isChecked = true
            EnumMouseAge.JUVENILE.myName -> binding.rbJuvenile.isChecked = true
            null -> binding.rbNullAge.isChecked = true
        }

        when (args.mouse.captureID) {
            EnumCaptureID.CAPTURED.myName -> binding.rbCaptured.isChecked = true
            EnumCaptureID.DIED.myName -> binding.rbDied.isChecked = true
            EnumCaptureID.ESCAPED.myName -> binding.rbEscaped.isChecked = true
            EnumCaptureID.RELEASED.myName -> binding.rbReleased.isChecked = true
            null -> binding.rbNullCapture.isChecked = true
        }

        when (args.mouse.trapID) {
            EnumTrapID.LIVE_TRAPS.myName -> {
                binding.etBody.isEnabled = false
                binding.etTail.isEnabled = false
                binding.etFeet.isEnabled = false
                binding.etEar.isEnabled = false
            }
            EnumTrapID.SNAP_TRAPS.myName -> {
                binding.rgCaptureId.children.forEach {
                    it.isEnabled = false
                }
            }
        }
    }

    private fun setListeners() {
        binding.rgSex.setOnCheckedChangeListener { radioGroup, radioButtonId ->
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

        binding.rgAge.setOnCheckedChangeListener { radioGroup, radioButtonId ->
            when (radioButtonId) {
                binding.rbAdult.id -> age = EnumMouseAge.ADULT.myName
                binding.rbJuvenile.id -> age = EnumMouseAge.JUVENILE.myName
                binding.rbSubadult.id -> age = EnumMouseAge.SUBADULT.myName
                binding.rbNullAge.id -> age = null
            }
        }

        binding.rgCaptureId.setOnCheckedChangeListener { radioGroup, radioButtonId ->
            when (radioButtonId) {
                binding.rbCaptured.id -> captureID = EnumCaptureID.CAPTURED.myName
                binding.rbDied.id -> captureID = EnumCaptureID.DIED.myName
                binding.rbEscaped.id -> captureID = EnumCaptureID.ESCAPED.myName
                binding.rbReleased.id -> captureID = EnumCaptureID.RELEASED.myName
                binding.rbNullCapture.id -> captureID = null
            }
        }
    }

    private fun setListenerToTrapID() {
        binding.autoCompTvTrapId.setOnItemClickListener { parent, view, position, id ->
            when (parent.getItemAtPosition(position) as String) {
                EnumTrapID.LIVE_TRAPS.myName -> {
                    binding.etBody.isEnabled = false
                    binding.etBody.setText("")
                    binding.etTail.isEnabled = false
                    binding.etTail.setText("")
                    binding.etFeet.isEnabled = false
                    binding.etFeet.setText("")
                    binding.etEar.isEnabled = false
                    binding.etEar.setText("")

                    binding.rgCaptureId.children.forEach {
                        it.isEnabled = true
                    }
                }
                EnumTrapID.SNAP_TRAPS.myName -> {
                    binding.rgCaptureId.children.forEach {
                        it.isEnabled = false
                    }
                    binding.rgCaptureId.clearCheck()

                    binding.etBody.isEnabled = true
                    binding.etTail.isEnabled = true
                    binding.etFeet.isEnabled = true
                    binding.etEar.isEnabled = true
                }
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
        val action =
            RecaptureMouseFragmentDirections.actionRecaptureMouseFragmentToTakePhotoFragment(
                "Mouse",
                imgName
            )
        findNavController().navigate(action)
    }

    private fun recaptureMouse() {
        val trapID: String = binding.autoCompTvTrapId.text.toString()
        val speciesID: Long = mapSpecie.getOrDefault(binding.autoCompTvSpecie.text.toString(), 1)

        if (checkInput(speciesID, trapID)) {
            // recapture mouse
            val mouse: Mouse = args.mouse.copy()
            mouse.mouseId = 0
            mouse.primeMouseID = args.mouse.mouseId
            mouse.occasionID = args.occasion.occasionId
            mouse.localityID = args.occasion.localityID
            mouse.mouseDateTimeCreated = Calendar.getInstance().time
            mouse.mouseDateTimeUpdated = null
            mouse.recapture = true
            mouse.speciesID = speciesID
            mouse.trapID = trapID
            mouse.sex = sex
            mouse.age = age
            mouse.captureID = if (trapID == EnumTrapID.SNAP_TRAPS.myName) null else captureID

            mouse.protocolID = mapProtocol.getOrDefault(binding.autoCompTvProtocol.text.toString(), null)
            mouse.sexActive = binding.cbSexActive.isChecked
            mouse.weight = giveOutPutFloat(binding.etWeight.text.toString())
            mouse.testesLength = giveOutPutFloat(binding.etTestesLength.text.toString())
            mouse.testesWidth = giveOutPutFloat(binding.etTestesWidth.text.toString())

            mouse.body = if (trapID == EnumTrapID.LIVE_TRAPS.myName) null else giveOutPutFloat(binding.etBody.text.toString())
            mouse.tail = if (trapID == EnumTrapID.LIVE_TRAPS.myName) null else giveOutPutFloat(binding.etTail.text.toString())
            mouse.feet = if (trapID == EnumTrapID.LIVE_TRAPS.myName) null else giveOutPutFloat(binding.etFeet.text.toString())
            mouse.ear = if (trapID == EnumTrapID.LIVE_TRAPS.myName) null else giveOutPutFloat(binding.etEar.text.toString())

            mouse.gravidity = if (sex == EnumSex.MALE.myName) null else binding.cbGravit.isChecked
            mouse.lactating = if (sex == EnumSex.MALE.myName) null else binding.cbLactating.isChecked
            //počet embryí v oboch rohoch maternice a ich priemer
            mouse.embryoRight =
                if (sex == EnumSex.MALE.myName) null else giveOutPutInt(binding.etEmbryoRight.text.toString())
            mouse.embryoLeft =
                if (sex == EnumSex.MALE.myName) null else giveOutPutInt(binding.etEmbryoLeft.text.toString())
            mouse.embryoDiameter =
                if (sex == EnumSex.MALE.myName) null else giveOutPutFloat(binding.etEmbryoDiameter.text.toString())
            mouse.MC = if (sex == EnumSex.MALE.myName) null else binding.cbMc.isChecked
            //počet placentálnych polypov
            mouse.MCright =
                if (sex == EnumSex.MALE.myName) null else giveOutPutInt(binding.etMcRight.text.toString())
            mouse.MCleft = if (sex == EnumSex.MALE.myName) null else giveOutPutInt(binding.etMcLeft.text.toString())

            mouse.note = if (binding.etMouseNote.text.toString().isBlank()) null else binding.etMouseNote.text.toString()
            mouse.imgName = imgName

            var selSpecie: Specie? = null
            listSpecie.forEach {
                if (it.specieId == mouse.speciesID) {
                    selSpecie = it
                }
            }

            if (mouse.weight != null && selSpecie?.minWeight != null && selSpecie?.maxWeight != null) {
                checkWeightAndSave(mouse, selSpecie!!)
            } else {
                executeTask(mouse)
            }

        } else {
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun checkWeightAndSave(mouse: Mouse, selSpecie: Specie) {
        if (mouse.weight!! > selSpecie.maxWeight!! || mouse.weight!! < selSpecie.minWeight!!) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton("Yes") { _, _ ->
                executeTask(mouse)
            }
                .setNegativeButton("No") { _, _ -> }
                .setTitle("Warning: Mouse Weight")
                .setMessage("Mouse weight out of bounds, save anyway?")
                .create().show()
        } else {
            executeTask(mouse)
        }
    }

    private fun executeTask(mouse: Mouse) {
        // zmenit pohlavie vsetkym predch. mysiam ak sa zmenilo
        if (args.mouse.sex != mouse.sex) {
            changeSexIfNecesary()
        }

        mouseViewModel.insertMouse(mouse)

        Toast.makeText(requireContext(), "Mouse recaptured.", Toast.LENGTH_SHORT).show()

        findNavController().navigateUp()
    }

    private fun checkInput(specieID: Long, trapID: String): Boolean {
        return specieID > 0 && trapID.isNotEmpty()
    }

    private fun giveOutPutInt(input: String?): Int? {
        return if (input.isNullOrBlank() || input == "null") null else Integer.parseInt(input)
    }

    private fun giveOutPutFloat(input: String?): Float? {
        return if (input.isNullOrBlank() || input == "null") null else input.toFloat()
    }

    private fun changeSexIfNecesary() {
        // upravit zmenu pohlavia
        mouseViewModel.getMiceForLog(args.mouse.mouseId).observe(viewLifecycleOwner, {
            it.forEach { mouse ->
                mouse.sex = sex
                mouse.mouseDateTimeUpdated = Calendar.getInstance().time
                mouseViewModel.updateMouse(mouse)
            }
        })

        // zmena pohlavia prveho zaznamu
        mouseViewModel.getMouse(args.mouse.mouseId)
    }

}