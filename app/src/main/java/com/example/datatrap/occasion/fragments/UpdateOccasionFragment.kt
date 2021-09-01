package com.example.datatrap.occasion.fragments

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
import com.example.datatrap.databinding.FragmentUpdateOccasionBinding
import com.example.datatrap.models.*
import com.example.datatrap.occasion.fragments.weather.Weather
import com.example.datatrap.viewmodels.*
import java.text.SimpleDateFormat

class UpdateOccasionFragment : Fragment() {

    private var _binding: FragmentUpdateOccasionBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<UpdateOccasionFragmentArgs>()

    private lateinit var occasionViewModel: OccasionViewModel
    private lateinit var envTypeViewModel: EnvTypeViewModel
    private lateinit var methodViewModel: MethodViewModel
    private lateinit var metTypeViewModel: MethodTypeViewModel
    private lateinit var trapTypeViewModel: TrapTypeViewModel
    private lateinit var vegTypeViewModel: VegetTypeViewModel
    private lateinit var localityViewModel: LocalityViewModel

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
        _binding = FragmentUpdateOccasionBinding.inflate(inflater, container, false)
        occasionViewModel = ViewModelProvider(this).get(OccasionViewModel::class.java)
        envTypeViewModel = ViewModelProvider(this).get(EnvTypeViewModel::class.java)
        methodViewModel = ViewModelProvider(this).get(MethodViewModel::class.java)
        metTypeViewModel = ViewModelProvider(this).get(MethodTypeViewModel::class.java)
        trapTypeViewModel = ViewModelProvider(this).get(TrapTypeViewModel::class.java)
        vegTypeViewModel = ViewModelProvider(this).get(VegetTypeViewModel::class.java)
        localityViewModel = ViewModelProvider(this).get(LocalityViewModel::class.java)

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

        initCurrentOccasion()

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_save -> updateOccasion()
            R.id.menu_camera -> goToCamera()
            R.id.menu_weather -> getHistoryWeather()
            R.id.menu_delete -> deleteOccasion()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun goToCamera(){
        Toast.makeText(requireContext(), "Not implemented.", Toast.LENGTH_LONG).show()
    }

    private fun initCurrentOccasion(){
        binding.etLeg.setText(args.occasion.leg)
        binding.etOccasionNote.setText(args.occasion.note)
        binding.etTemperature.setText(args.occasion.temperature.toString())
        binding.etWeather.setText(args.occasion.weather)
        imgName = args.occasion.imgName
    }

    private fun initAutoComp(){
        envTypeNameList.forEach {
            if (it.value == args.occasion.envTypeId){
                binding.autoCompTvEnvType.setText(it.key, false)
            }
        }
        methodNameList.forEach {
            if (it.value == args.occasion.methodId){
                binding.autoCompTvMethod.setText(it.key, false)
            }
        }
        metTypeNameList.forEach {
            if (it.value == args.occasion.methodTypeId){
                binding.autoCompTvMethodType.setText(it.key, false)
            }
        }
        trapTypeNameList.forEach {
            if (it.value == args.occasion.trapTypeId){
                binding.autoCompTvTrapType.setText(it.key, false)
            }
        }
        vegTypeNameList.forEach {
            if (it.value == args.occasion.vegetTypeId){
                binding.autoCompTvVegType.setText(it.key, false)
            }
        }
    }

    private fun deleteOccasion() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->

            occasionViewModel.deleteOccasion(args.occasion)

            Toast.makeText(requireContext(),"Occasion deleted.", Toast.LENGTH_LONG).show()

            findNavController().navigateUp()
        }
            .setNegativeButton("No"){_, _ -> }
            .setTitle("Delete Occasion?")
            .setMessage("Are you sure you want to delete this occasion?")
            .create().show()
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
        //set values
        initAutoComp()
    }

    private fun updateOccasion() {
        val occasionNum: Int = args.occasion.occasion
        val method: Long = methodNameList.getValue(binding.autoCompTvMethod.text.toString())
        val methodType: Long = metTypeNameList.getValue(binding.autoCompTvMethodType.text.toString())
        val trapType: Long = trapTypeNameList.getValue(binding.autoCompTvTrapType.text.toString())
        val envType: Long? = envTypeNameList.getValue(binding.autoCompTvEnvType.text.toString())
        val vegType: Long? = vegTypeNameList.getValue(binding.autoCompTvVegType.text.toString())
        val date = args.occasion.date
        val time = args.occasion.time
        val gotCaught = if (binding.cbGotCaught.isChecked) 1 else 0
        val numTraps = binding.etNumTraps.text.toString()
        val numMice = args.occasion.numMice
        val leg: String = binding.etLeg.toString()
        val note: String? = binding.etOccasionNote.toString()

        if (checkInput(occasionNum, method, methodType, trapType, leg)){

            val occasion = Occasion(args.occasion.occasionId, occasionNum, args.occasion.localityId, args.occasion.sessionId,
                method, methodType, trapType, envType, vegType, date, time, gotCaught, Integer.parseInt(numTraps),
                numMice, temperature, weatherGlob, leg, note, imgName)

            occasionViewModel.updateOccasion(occasion)
            Toast.makeText(requireContext(), "Occasion updated.", Toast.LENGTH_SHORT).show()

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

    private fun getHistoryWeather(){
        val weather = Weather(requireContext())
        val locality: Locality = localityViewModel.getLocality(args.occasion.localityId).value!!

        val date = args.occasion.date
        val time = args.occasion.time
        val dateTimeString = "$date $time"
        val parser = SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
        val output = parser.parse(dateTimeString)
        val unixtime = output.time / 1000L

        weather.getHistoricalWeatherByCoordinates(locality.x, locality.y, unixtime, object: Weather.VolleyResponseListener{
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