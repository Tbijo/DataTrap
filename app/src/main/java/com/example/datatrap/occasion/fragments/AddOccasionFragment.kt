package com.example.datatrap.occasion.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentAddOccasionBinding
import com.example.datatrap.models.*
import com.example.datatrap.occasion.fragments.weather.Weather
import com.example.datatrap.viewmodels.*
import java.text.SimpleDateFormat
import java.util.*

class AddOccasionFragment : Fragment() {

    private var _binding: FragmentAddOccasionBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<AddOccasionFragmentArgs>()
    private lateinit var occasionViewModel: OccasionViewModel
    private lateinit var envTypeViewModel: EnvTypeViewModel
    private lateinit var methodViewModel: MethodViewModel
    private lateinit var metTypeViewModel: MethodTypeViewModel
    private lateinit var trapTypeViewModel: TrapTypeViewModel
    private lateinit var vegTypeViewModel: VegetTypeViewModel

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

    private var temperature: Float? = null
    private var weatherGlob: String? = null

    private lateinit var sharedViewModel: SharedViewModel
    private var imgName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentAddOccasionBinding.inflate(inflater, container, false)
        occasionViewModel = ViewModelProvider(this).get(OccasionViewModel::class.java)
        envTypeViewModel = ViewModelProvider(this).get(EnvTypeViewModel::class.java)
        methodViewModel = ViewModelProvider(this).get(MethodViewModel::class.java)
        metTypeViewModel = ViewModelProvider(this).get(MethodTypeViewModel::class.java)
        trapTypeViewModel = ViewModelProvider(this).get(TrapTypeViewModel::class.java)
        vegTypeViewModel = ViewModelProvider(this).get(VegetTypeViewModel::class.java)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        sharedViewModel.dataToShare.observe(requireActivity(), Observer<String> {
            imgName = it
        })

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

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.occasion_menu, menu)
        menu[R.id.menu_delete].isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_save -> insertOccasion()
            R.id.menu_camera -> goToCamera()
            R.id.menu_weather -> getCurrentWeather()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun goToCamera(){
        val action = AddOccasionFragmentDirections.actionAddOccasionFragmentToTakePhotoFragment("AddOccasionFragment")
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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
        val numTraps = binding.etNumTraps.text.toString()
        val numMice = 0
        val leg: String = binding.etLeg.toString()
        val note: String? = binding.etOccasionNote.toString()

        if (checkInput(occasionNum, method, methodType, trapType, leg)){

            val occasion = Occasion(0, occasionNum, args.locality.localityId, args.session.sessionId,
                method, methodType, trapType, envType, vegType, date, time, gotCaught, Integer.parseInt(numTraps),
                numMice, temperature, weatherGlob, leg, note, imgName)

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

}