package com.example.datatrap.occasion.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentAddOccasionBinding
import com.example.datatrap.models.*
import com.example.datatrap.occasion.fragments.weather.Weather
import com.example.datatrap.viewmodels.*
import java.io.File
import java.io.IOException
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
    private lateinit var envTypeNameList: MutableMap<String, Long>
    private lateinit var methodNameList: MutableMap<String, Long>
    private lateinit var metTypeNameList: MutableMap<String, Long>
    private lateinit var trapTypeNameList: MutableMap<String, Long>
    private lateinit var vegTypeNameList: MutableMap<String, Long>

    private var myPath: String? = null
    private  var photoURI: Uri? = null
    private var title: String? = null

    private var temperature: Float? = null
    private var weatherGlob: String? = null

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

        binding.btnGetWeather.setOnClickListener {
            getCurrentWeather()
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
        val occasionNum: Int = args.newOccasionNumber
        val method: Long = methodNameList.getValue(binding.autoCompTvMethod.text.toString())
        val methodType: Long = metTypeNameList.getValue(binding.autoCompTvMethodType.text.toString())
        val trapType: Long = trapTypeNameList.getValue(binding.autoCompTvTrapType.text.toString())
        val envType: Long? = envTypeNameList.getValue(binding.autoCompTvEnvType.text.toString())
        val vegType: Long? = vegTypeNameList.getValue(binding.autoCompTvVegType.text.toString())
        val date = getDate()
        val time = getTime()
        val gotCaught = 0
        val numTraps = 0
        val numMice = 0
        val leg: String = binding.etLeg.toString()
        val note: String? = binding.etOccasionNote.toString()

        if (checkInput(occasionNum, method, methodType, trapType, leg)){

            if (title != null){
                val picture = Picture(title!!, photoURI.toString(), binding.etOccPicNote.text.toString())
                pictureViewModel.insertPicture(picture)
                galleryAddPic(photoURI!!, title!!)
            }

            val occasion = Occasion(0, occasionNum, args.locality.localityId, args.session.sessionId,
                method, methodType, trapType, envType, vegType, date, time, gotCaught, numTraps,
                numMice, temperature, weatherGlob, leg, note, title)

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

    private fun getDate(): String{
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateInstance()
        return formatter.format(date)
    }

    private fun getTime():String{
        val date = Calendar.getInstance().time
        val formatterT = SimpleDateFormat.getTimeInstance()
        return formatterT.format(date)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getCurrentWeather(){
        val weather = Weather(requireContext())
        weather.getCurrentWeatherByCoordinates(args.locality.x, args.locality.y, object: Weather.VolleyResponseListener{
            override fun onResponse(temp: Int, weather: String) {
                temperature = temp.toFloat()
                binding.etTemperature.setText(temperature.toString())
                weatherGlob = weather
                binding.etWeather.setText(weatherGlob)
            }

            override fun onError(message: String) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun takePicture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity?.packageManager!!)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    Toast.makeText(requireContext(), "File was not created.", Toast.LENGTH_LONG).show()
                    null
                }
                photoFile?.also {
                    photoURI = FileProvider.getUriForFile(
                        requireContext(),
                        "com.example.datatrap.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    resultLauncher.launch(takePictureIntent)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateTimeInstance()
        val formatedDate = formatter.format(date)
        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "Occasion_${formatedDate}_",
            ".jpg",
            storageDir
        ).apply {
            myPath = absolutePath
            binding.tvOccPhoto.text = "Specie_$formatedDate"
            title = "Occasion_$formatedDate"
        }
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode != Activity.RESULT_OK) {
            // sem sa pojde ak pouzivatel nespravil fotku
            // treba vymazat empty file ktora bola vytvorena
            val myFile: File = File(myPath)
            if (myFile.exists()) myFile.delete()
            binding.tvOccPhoto.text = getString(R.string.noPicture)
            title = null
            photoURI = null
            myPath = null
            Toast.makeText(requireContext(), "Empty File deleted.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun galleryAddPic(imageUri:Uri, title:String) {
        val bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri)
        MediaStore.Images.Media.insertImage(
            requireContext().contentResolver,
            bitmap,
            title,
            "Image of $title"
        )
        Toast.makeText(requireContext(), "Picture Added to Gallery", Toast.LENGTH_SHORT).show()
    }

}