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

    private var imageUri: Uri? = null

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
        val dropDownArrAdapEnvType = ArrayAdapter(requireContext(), R.layout.dropdown_names, nameList)
        binding.autoCompTvEnvType.setAdapter(dropDownArrAdapEnvType)
        // method adapter
        val dropDownArrAdapMethod = ArrayAdapter(requireContext(), R.layout.dropdown_names, nameList)
        binding.autoCompTvMethod.setAdapter(dropDownArrAdapMethod)
        // method type adapter
        val dropDownArrAdapMethodType = ArrayAdapter(requireContext(), R.layout.dropdown_names, nameList)
        binding.autoCompTvMethodType.setAdapter(dropDownArrAdapMethodType)
        // trap type adapter
        val dropDownArrAdapTrapType = ArrayAdapter(requireContext(), R.layout.dropdown_names, nameList)
        binding.autoCompTvTrapType.setAdapter(dropDownArrAdapTrapType)
        // veget type adapter
        val dropDownArrAdapVegType = ArrayAdapter(requireContext(), R.layout.dropdown_names, nameList)
        binding.autoCompTvVegType.setAdapter(dropDownArrAdapVegType)
    }

    private fun takePicture() {

    }

    private fun insertOccasion() {
        val occasionNum: Int = Integer.parseInt(binding.etOccasion.text.toString())
        val method: String = binding.autoCompTvMethod.text.toString()
        val methodType: String = binding.autoCompTvMethodType.text.toString()
        val trapType: String = binding.autoCompTvTrapType.text.toString()
        val envType: String? = binding.autoCompTvEnvType.text.toString()
        val vegType: String? = binding.autoCompTvVegType.text.toString()

        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate = sdf.format(Date())

        // treba sem cas doplnit kvoli tomu aby sme mohli dostat pocasie v datume aj case
        // ale v tabulke maju len datum tak som dal vlastnu val cas
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getTimeInstance() //or use getDateInstance()
        val formatedDate = formatter.format(date)
        val time = null

        val gotCaught = 0
        val numTraps = 0
        val numMice = 0
        val temperature: Float? = null
        val weather: String? = null
        val leg: String = binding.etLeg.toString()
        val note: String? = binding.etOccasionNote.toString()
        var img: String? = null

        if (checkInput(occasionNum, method, methodType, trapType, leg)){

            if (imageUri != null){
                img = "Nazov Fotky"
                val picture = Picture(img, imageUri.toString(), binding.etOccPicNote.text.toString())
                pictureViewModel.insertPicture(picture)
            }

            val occasion = Occasion(0, occasionNum, args.locality.localityId, args.session.sessionId,
                method, methodType, trapType, envType, vegType, currentDate, "", gotCaught, numTraps,
                numMice, temperature, weather, leg, note, img)

            occasionViewModel.insertOccasion(occasion)
            Toast.makeText(requireContext(), "New occasion added.", Toast.LENGTH_SHORT).show()

            findNavController().navigateUp()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun checkInput(
        occasion: Int,
        method: String,
        methodType: String,
        trapType: String,
        leg: String
    ): Boolean {
        return occasion.toString().isNotEmpty() && method.isNotEmpty() && methodType.isNotEmpty() && trapType.isNotEmpty() && leg.isNotEmpty()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}