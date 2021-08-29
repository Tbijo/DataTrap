package com.example.datatrap.occasion.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentAddOccasionBinding
import com.example.datatrap.models.*
import com.example.datatrap.viewmodels.*
import java.text.SimpleDateFormat
import java.util.*

class AddOccasionFragment : Fragment() {
/*Pridat take Photo a ziskanie teploty a pocasia z netu overit ci je pristup na net*/
    private var _binding: FragmentAddOccasionBinding? = null
    private val binding get() = _binding!!
    private lateinit var occasionViewModel: OccasionViewModel
    private lateinit var pictureViewModel: PictureViewModel
    private lateinit var envTypeViewModel: EnvTypeViewModel
    private lateinit var methodViewModel: MethodViewModel
    private lateinit var metTypeViewModel: MethodTypeViewModel
    private lateinit var trapTypeViewModel: TrapTypeViewModel
    private lateinit var vegTypeViewModel: VegetTypeViewModel
    private val args by navArgs<AddOccasionFragmentArgs>()

    private lateinit var envTypeList: List<EnvType>
    private lateinit var methodList: List<Method>
    private lateinit var metTypeList: List<MethodType>
    private lateinit var trapTypeList: List<TrapType>
    private lateinit var vegTypeList: List<VegetType>
    private lateinit var envTypeNameList: MutableMap<String, Long>
    private lateinit var methodNameList: MutableMap<String, Long>
    private lateinit var metTypeNameList: MutableMap<String, Long>
    private lateinit var trapTypeNameList: MutableMap<String, Long>
    private lateinit var vegTypeNameList: MutableMap<String, Long>

    private var imageUri: Uri? = null
    private var imgName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentAddOccasionBinding.inflate(inflater, container, false)
        occasionViewModel = ViewModelProvider(this).get(OccasionViewModel::class.java)
        pictureViewModel = ViewModelProvider(this).get(PictureViewModel::class.java)
        envTypeViewModel = ViewModelProvider(this).get(EnvTypeViewModel::class.java)
        methodViewModel = ViewModelProvider(this).get(MethodViewModel::class.java)
        metTypeViewModel = ViewModelProvider(this).get(MethodTypeViewModel::class.java)
        trapTypeViewModel = ViewModelProvider(this).get(TrapTypeViewModel::class.java)
        vegTypeViewModel = ViewModelProvider(this).get(VegetTypeViewModel::class.java)

        envTypeList = envTypeViewModel.envTypeList.value!!
        envTypeList.forEach {
            envTypeNameList[it.envTypeName] = it.envTypeId
        }

        methodList = methodViewModel.methodList.value!!
        methodList.forEach {
            methodNameList[it.methodName] = it.methodId
        }

        metTypeList = metTypeViewModel.methodTypeList.value!!
        metTypeList.forEach {
            metTypeNameList[it.methodTypeName] = it.methodTypeId
        }

        trapTypeList = trapTypeViewModel.trapTypeList.value!!
        trapTypeList.forEach {
            trapTypeNameList[it.trapTypeName] = it.trapTypeId
        }

        vegTypeList = vegTypeViewModel.vegetTypeList.value!!
        vegTypeList.forEach {
            vegTypeNameList[it.vegetTypeName] = it.vegetTypeId
        }

        binding.btnAddOccasion.setOnClickListener {
            insertOccasion()
        }

        binding.btnOccPhoto.setOnClickListener {
            takePicture()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // env type adapter
        val dropDownArrAdapEnvType = ArrayAdapter(requireContext(), R.layout.dropdown_names, envTypeNameList.keys.toList())
        binding.autoCompTvEnvType.setAdapter(dropDownArrAdapEnvType)
        // method adapter
        val dropDownArrAdapMethod = ArrayAdapter(requireContext(), R.layout.dropdown_names, methodNameList.keys.toList())
        binding.autoCompTvMethod.setAdapter(dropDownArrAdapMethod)
        // method type adapter
        val dropDownArrAdapMethodType = ArrayAdapter(requireContext(), R.layout.dropdown_names, metTypeNameList.keys.toList())
        binding.autoCompTvMethodType.setAdapter(dropDownArrAdapMethodType)
        // trap type adapter
        val dropDownArrAdapTrapType = ArrayAdapter(requireContext(), R.layout.dropdown_names, trapTypeNameList.keys.toList())
        binding.autoCompTvTrapType.setAdapter(dropDownArrAdapTrapType)
        // veget type adapter
        val dropDownArrAdapVegType = ArrayAdapter(requireContext(), R.layout.dropdown_names, vegTypeNameList.keys.toList())
        binding.autoCompTvVegType.setAdapter(dropDownArrAdapVegType)
    }

    private fun insertOccasion() {
        val occasionNum: Int = Integer.parseInt(binding.etOccasion.text.toString())
        val method: Long = methodNameList.getValue(binding.autoCompTvMethod.text.toString())
        val methodType: Long = metTypeNameList.getValue(binding.autoCompTvMethodType.text.toString())
        val trapType: Long = trapTypeNameList.getValue(binding.autoCompTvTrapType.text.toString())
        val envType: Long? = envTypeNameList.getValue(binding.autoCompTvEnvType.text.toString())
        val vegType: Long? = vegTypeNameList.getValue(binding.autoCompTvVegType.text.toString())

        val date = Calendar.getInstance().time

        val formatter = SimpleDateFormat.getDateInstance()
        val formatedDate = formatter.format(date)

        val formatterT = SimpleDateFormat.getTimeInstance()
        val formatedTime = formatterT.format(date)

        val gotCaught = 0
        val numTraps = 0
        val numMice = 0
        val temperature: Float? = null
        val weather: String? = null
        val leg: String = binding.etLeg.toString()
        val note: String? = binding.etOccasionNote.toString()

        if (checkInput(occasionNum, method, methodType, trapType, leg)){

            if (imageUri != null){
                imgName = "Nastavi sa pri pridani FOTKY"
                val picture = Picture(imgName!!, imageUri.toString(), binding.etOccPicNote.text.toString())
                pictureViewModel.insertPicture(picture)
            }

            val occasion = Occasion(0, occasionNum, args.locality.localityId, args.session.sessionId,
                method, methodType, trapType, envType, vegType, formatedDate, formatedTime, gotCaught, numTraps,
                numMice, temperature, weather, leg, note, imgName)

            occasionViewModel.insertOccasion(occasion)
            Toast.makeText(requireContext(), "New occasion added.", Toast.LENGTH_SHORT).show()

            findNavController().navigateUp()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun checkInput(
        occasion: Int,
        method: Long,
        methodType: Long,
        trapType: Long,
        leg: String
    ): Boolean {
        return occasion.toString().isNotEmpty() && method.toString().isNotEmpty() && methodType.toString().isNotEmpty() && trapType.toString().isNotEmpty() && leg.isNotEmpty()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun takePicture() {
        imgName = null
    }

}