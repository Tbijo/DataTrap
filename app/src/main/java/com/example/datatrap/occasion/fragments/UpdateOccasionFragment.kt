package com.example.datatrap.occasion.fragments

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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
import java.util.*

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
    private lateinit var sessionViewModel: SessionViewModel

    private lateinit var envTypeList: List<EnvType>
    private lateinit var methodList: List<Method>
    private lateinit var metTypeList: List<MethodType>
    private lateinit var trapTypeList: List<TrapType>
    private lateinit var vegTypeList: List<VegetType>

    private lateinit var envTypeNameMap: MutableMap<String, Long?>
    private lateinit var methodNameMap: MutableMap<String, Long>
    private lateinit var metTypeNameMap: MutableMap<String, Long>
    private lateinit var trapTypeNameMap: MutableMap<String, Long>
    private lateinit var vegTypeNameMap: MutableMap<String, Long?>

    private var temperature: Float? = null
    private var weatherGlob: String? = null

    private lateinit var sharedViewModel: SharedViewModel
    private var imgName: String? = args.occasion.imgName

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
        sessionViewModel = ViewModelProvider(this).get(SessionViewModel::class.java)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        sharedViewModel.dataToShare.observe(requireActivity(), Observer<String> {
            imgName = it
        })

        envTypeList = envTypeViewModel.envTypeList.value!!
        envTypeList.forEach {
            envTypeNameMap[it.envTypeName] = it.envTypeId
        }
        envTypeNameMap["null"] = null

        methodList = methodViewModel.methodList.value!!
        methodList.forEach {
            methodNameMap[it.methodName] = it.methodId
        }

        metTypeList = metTypeViewModel.methodTypeList.value!!
        metTypeList.forEach {
            metTypeNameMap[it.methodTypeName] = it.methodTypeId
        }

        trapTypeList = trapTypeViewModel.trapTypeList.value!!
        trapTypeList.forEach {
            trapTypeNameMap[it.trapTypeName] = it.trapTypeId
        }

        vegTypeList = vegTypeViewModel.vegetTypeList.value!!
        vegTypeList.forEach {
            vegTypeNameMap[it.vegetTypeName] = it.vegetTypeId
        }
        vegTypeNameMap["null"] = null

        initOccasionValuesToView()

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.occasion_menu, menu)
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

    override fun onResume() {
        super.onResume()
        // env type adapter
        val dropDownArrAdapEnvType = ArrayAdapter(requireContext(), R.layout.dropdown_names, envTypeNameMap.keys.toList())
        binding.autoCompTvEnvType.setAdapter(dropDownArrAdapEnvType)
        // method adapter
        val dropDownArrAdapMethod = ArrayAdapter(requireContext(), R.layout.dropdown_names, methodNameMap.keys.toList())
        binding.autoCompTvMethod.setAdapter(dropDownArrAdapMethod)
        // method type adapter
        val dropDownArrAdapMethodType = ArrayAdapter(requireContext(), R.layout.dropdown_names, metTypeNameMap.keys.toList())
        binding.autoCompTvMethodType.setAdapter(dropDownArrAdapMethodType)
        // trap type adapter
        val dropDownArrAdapTrapType = ArrayAdapter(requireContext(), R.layout.dropdown_names, trapTypeNameMap.keys.toList())
        binding.autoCompTvTrapType.setAdapter(dropDownArrAdapTrapType)
        // veget type adapter
        val dropDownArrAdapVegType = ArrayAdapter(requireContext(), R.layout.dropdown_names, vegTypeNameMap.keys.toList())
        binding.autoCompTvVegType.setAdapter(dropDownArrAdapVegType)

        //set values
        envTypeNameMap.forEach {
            if (it.value == args.occasion.envTypeID){
                binding.autoCompTvEnvType.setText(it.key, false)
            }
        }
        methodNameMap.forEach {
            if (it.value == args.occasion.methodID){
                binding.autoCompTvMethod.setText(it.key, false)
            }
        }
        metTypeNameMap.forEach {
            if (it.value == args.occasion.methodTypeID){
                binding.autoCompTvMethodType.setText(it.key, false)
            }
        }
        trapTypeNameMap.forEach {
            if (it.value == args.occasion.trapTypeID){
                binding.autoCompTvTrapType.setText(it.key, false)
            }
        }
        vegTypeNameMap.forEach {
            if (it.value == args.occasion.vegetTypeID){
                binding.autoCompTvVegType.setText(it.key, false)
            }
        }
    }

    private fun goToCamera(){
        val action = UpdateOccasionFragmentDirections.actionUpdateOccasionFragmentToTakePhotoFragment("Occasion", imgName)
        findNavController().navigate(action)
    }

    private fun initOccasionValuesToView(){
        binding.etLeg.setText(args.occasion.leg)
        binding.etOccasionNote.setText(args.occasion.note)
        binding.etTemperature.setText(args.occasion.temperature.toString())
        binding.etWeather.setText(args.occasion.weather)
        imgName = args.occasion.imgName
    }

    private fun deleteOccasion() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->

            // zmensit numOcc v session
            updateSessionNumOcc()

            // vymazat occasion
            occasionViewModel.deleteOccasion(args.occasion)

            Toast.makeText(requireContext(),"Occasion deleted.", Toast.LENGTH_LONG).show()

            findNavController().navigateUp()
        }
            .setNegativeButton("No"){_, _ -> }
            .setTitle("Delete Occasion?")
            .setMessage("Are you sure you want to delete this occasion?")
            .create().show()
    }

    private fun updateOccasion() {
        val occasionNum: Int = args.occasion.occasion
        val method: Long = methodNameMap.getValue(binding.autoCompTvMethod.text.toString())
        val methodType: Long = metTypeNameMap.getValue(binding.autoCompTvMethodType.text.toString())
        val trapType: Long = trapTypeNameMap.getValue(binding.autoCompTvTrapType.text.toString())
        val leg: String = binding.etLeg.toString()

        if (checkInput(occasionNum, method, methodType, trapType, leg)){

            val occasion: Occasion = args.occasion
            occasion.methodID = method
            occasion.methodTypeID = methodType
            occasion.trapTypeID = trapType
            occasion.leg = leg
            occasion.occasionDateTimeUpdated = Calendar.getInstance().time

            occasion.envTypeID = envTypeNameMap.getValue(binding.autoCompTvEnvType.text.toString())
            occasion.vegetTypeID = vegTypeNameMap.getValue(binding.autoCompTvVegType.text.toString())
            occasion.gotCaught = if (binding.cbGotCaught.isChecked) 1 else 0
            occasion.numTraps = if (binding.etNumTraps.text.toString().isBlank()) null else Integer.parseInt(binding.etNumTraps.text.toString())
            occasion.temperature = if (binding.etTemperature.text.toString().isBlank()) null else temperature
            occasion.weather = if (binding.etWeather.text.toString().isBlank()) null else weatherGlob
            occasion.note = if (binding.etOccasionNote.toString().isBlank()) null else binding.etOccasionNote.toString()
            occasion.imgName = imgName

            occasionViewModel.updateOccasion(occasion)

            Toast.makeText(requireContext(), "Occasion updated.", Toast.LENGTH_SHORT).show()

            findNavController().navigateUp()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun updateSessionNumOcc(){
        val updatedSession: Session = sessionViewModel.getSession(args.occasion.sessionID).value!!
        updatedSession.numOcc = (updatedSession.numOcc - 1)
        sessionViewModel.updateSession(updatedSession)
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
        if (isOnline(requireContext())){
            val weather = Weather(requireContext())
            val locality: Locality = localityViewModel.getLocality(args.occasion.localityID).value!!

            val unixtime = args.occasion.occasionDateTimeCreated.time / 1000L

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
        }else{
            Toast.makeText(requireContext(), "Connect to Internet.", Toast.LENGTH_LONG).show()
        }
    }

    private fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val n = cm.activeNetwork
        if (n != null) {
            val nc = cm.getNetworkCapabilities(n)
            return nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(
                NetworkCapabilities.TRANSPORT_WIFI
            )
        }
        return false
    }

}