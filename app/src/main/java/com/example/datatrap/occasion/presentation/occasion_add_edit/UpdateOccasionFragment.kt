package com.example.datatrap.occasion.presentation.occasion_add_edit

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentUpdateOccasionBinding
import com.example.datatrap.models.*
import com.example.datatrap.occasion.data.Occasion
import com.example.datatrap.picture.data.OccasionImage
import com.example.datatrap.picture.presentation.OccasionImageViewModel
import com.example.datatrap.occasion.presentation.occasion_list.OccasionListViewModel
import com.example.datatrap.settings.envtype.data.EnvType
import com.example.datatrap.settings.envtype.presentation.EnvTypeViewModel
import com.example.datatrap.settings.method.data.Method
import com.example.datatrap.settings.method.presentation.MethodViewModel
import com.example.datatrap.settings.methodtype.data.MethodType
import com.example.datatrap.settings.methodtype.presentation.MethodTypeViewModel
import com.example.datatrap.settings.traptype.data.TrapType
import com.example.datatrap.settings.traptype.presentation.TrapTypeViewModel
import com.example.datatrap.settings.vegettype.data.VegetType
import com.example.datatrap.settings.vegettype.presentation.VegetTypeViewModel
import com.example.datatrap.viewmodels.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class UpdateOccasionFragment : Fragment() {

    private var _binding: FragmentUpdateOccasionBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<UpdateOccasionFragmentArgs>()

    private val occasionListViewModel: OccasionListViewModel by viewModels()
    private val envTypeViewModel: EnvTypeViewModel by viewModels()
    private val methodViewModel: MethodViewModel by viewModels()
    private val metTypeViewModel: MethodTypeViewModel by viewModels()
    private val trapTypeViewModel: TrapTypeViewModel by viewModels()
    private val vegTypeViewModel: VegetTypeViewModel by viewModels()
    private val occasionImageViewModel: OccasionImageViewModel by viewModels()
    private val volleyViewModel: VolleyViewModel by viewModels()

    private lateinit var envTypeList: List<EnvType>
    private lateinit var methodList: List<Method>
    private lateinit var metTypeList: List<MethodType>
    private lateinit var vegTypeList: List<VegetType>
    private lateinit var trapTypeList: List<TrapType>

    private lateinit var currentOccasion: Occasion
    private var occasionImage: OccasionImage? = null
    private var methodID: Long = 0
    private var methodTypeID: Long = 0
    private var trapTypeID: Long = 0
    private var envTypeID: Long? = null
    private var vegetTypeID: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentUpdateOccasionBinding.inflate(inflater, container, false)

        occasionImageViewModel.getImageForOccasion(args.occList.occasionId).observe(viewLifecycleOwner) {
            occasionImage = it
        }

        occasionListViewModel.getOccasion(args.occList.occasionId).observe(viewLifecycleOwner) {
            currentOccasion = it
            fillDropDown(it)
            initOccasionValuesToView(it)
        }

        setListeners()

        volleyViewModel.weatherCoor.observe(viewLifecycleOwner) {
            binding.etTemperature.setText(it.temp.toString())
            binding.etWeather.setText(it.weather)
        }

        volleyViewModel.errorWeather.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }

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
        occasionListViewModel.getOccasion(args.occList.occasionId).observe(viewLifecycleOwner) {
            fillDropDown(it)
        }
    }

    private fun setListeners() {
        binding.autoCompTvEnvType.setOnItemClickListener { parent, _, position, _ ->
            val name: String = parent.getItemAtPosition(position) as String
            envTypeID = envTypeList.firstOrNull {
                it.envTypeName == name
            }?.envTypeId
        }

        binding.autoCompTvMethod.setOnItemClickListener { parent, _, position, _ ->
            val name: String = parent.getItemAtPosition(position) as String
            methodID = methodList.first {
                it.methodName == name
            }.methodId
        }

        binding.autoCompTvMethodType.setOnItemClickListener { parent, _, position, _ ->
            val name: String = parent.getItemAtPosition(position) as String
            methodTypeID = metTypeList.first {
                it.methodTypeName == name
            }.methodTypeId
        }

        binding.autoCompTvTrapType.setOnItemClickListener { parent, _, position, _ ->
            val name: String = parent.getItemAtPosition(position) as String
            trapTypeID = trapTypeList.first {
                it.trapTypeName == name
            }.trapTypeId
        }

        binding.autoCompTvVegType.setOnItemClickListener { parent, _, position, _ ->
            val name: String = parent.getItemAtPosition(position) as String
            vegetTypeID = vegTypeList.firstOrNull {
                it.vegetTypeName == name
            }?.vegetTypeId
        }
    }

    private fun fillDropDown(occasion: Occasion) {
        envTypeViewModel.envTypeList.observe(viewLifecycleOwner) {
            envTypeList = it
            val nameList = mutableListOf<String>()
            nameList.add("null")
            it.forEach { env ->
                nameList.add(env.envTypeName)
            }
            // env type adapter
            val dropDownArrAdapEnvType =
                ArrayAdapter(requireContext(), R.layout.dropdown_names, nameList)
            binding.autoCompTvEnvType.setAdapter(dropDownArrAdapEnvType)
            //set values
            val current = it.firstOrNull { envType ->
                envType.envTypeId == occasion.envTypeID
            }
            envTypeID = current?.envTypeId
            binding.autoCompTvEnvType.setText(current?.envTypeName.toString(), false)
        }

        methodViewModel.methodList.observe(viewLifecycleOwner) {
            methodList = it
            val nameList = mutableListOf<String>()
            it.forEach { method ->
                nameList.add(method.methodName)
            }
            // method adapter
            val dropDownArrAdapMethod =
                ArrayAdapter(requireContext(), R.layout.dropdown_names, nameList)
            binding.autoCompTvMethod.setAdapter(dropDownArrAdapMethod)
            // set values
            val current = it.first { method ->
                method.methodId == occasion.methodID
            }
            methodID = current.methodId
            binding.autoCompTvMethod.setText(current.methodName, false)
        }

        metTypeViewModel.methodTypeList.observe(viewLifecycleOwner) {
            metTypeList = it
            val nameList = mutableListOf<String>()
            it.forEach { metType ->
                nameList.add(metType.methodTypeName)
            }
            // method type adapter
            val dropDownArrAdapMethodType =
                ArrayAdapter(requireContext(), R.layout.dropdown_names, nameList)
            binding.autoCompTvMethodType.setAdapter(dropDownArrAdapMethodType)
            // set Values
            val current = it.first { methodType ->
                methodType.methodTypeId == occasion.methodTypeID
            }
            methodTypeID = current.methodTypeId
            binding.autoCompTvMethodType.setText(current.methodTypeName, false)
        }

        trapTypeViewModel.trapTypeList.observe(viewLifecycleOwner) {
            trapTypeList = it
            val nameList = mutableListOf<String>()
            it.forEach { trapType ->
                nameList.add(trapType.trapTypeName)
            }
            // trap type adapter
            val dropDownArrAdapTrapType =
                ArrayAdapter(requireContext(), R.layout.dropdown_names, nameList)
            binding.autoCompTvTrapType.setAdapter(dropDownArrAdapTrapType)
            // set values
            val current = it.first { trapType ->
                trapType.trapTypeId == occasion.trapTypeID
            }
            trapTypeID = current.trapTypeId
            binding.autoCompTvTrapType.setText(current.trapTypeName, false)
        }

        vegTypeViewModel.vegetTypeList.observe(viewLifecycleOwner) {
            vegTypeList = it
            val nameList = mutableListOf<String>()
            nameList.add("null")
            it.forEach { vegType ->
                nameList.add(vegType.vegetTypeName)
            }
            // veget type adapter
            val dropDownArrAdapVegType =
                ArrayAdapter(requireContext(), R.layout.dropdown_names, nameList)
            binding.autoCompTvVegType.setAdapter(dropDownArrAdapVegType)
            // set values
            val current = it.firstOrNull { vegType ->
                vegType.vegetTypeId == occasion.vegetTypeID
            }
            vegetTypeID = current?.vegetTypeId
            binding.autoCompTvVegType.setText(current?.vegetTypeName.toString(), false)
        }

    }

    private fun goToCamera() {
        val action = UpdateOccasionFragmentDirections.actionUpdateOccasionFragmentToTakePhotoFragment("Occasion",
            args.occList.occasionId, "")
        findNavController().navigate(action)
    }

    private fun initOccasionValuesToView(occasion: Occasion) {
        binding.etNumTraps.setText(occasion.numTraps.toString())
        binding.etTemperature.setText(occasion.temperature.toString())
        binding.etWeather.setText(occasion.weather.toString())
        binding.cbGotCaught.isChecked = occasion.gotCaught == true

        binding.etLeg.setText(occasion.leg)
        binding.etOccasionNote.setText(occasion.note.toString())
    }

    private fun deleteOccasion() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->

            // vymazat occasion
            occasionListViewModel.deleteOccasion(args.occList.occasionId, occasionImage?.path)

            Toast.makeText(requireContext(),"Occasion deleted.", Toast.LENGTH_LONG).show()

            findNavController().navigateUp()
        }
            .setNegativeButton("No"){_, _ -> }
            .setTitle("Delete Occasion?")
            .setMessage("Are you sure you want to delete this occasion?")
            .create().show()
    }

    private fun updateOccasion() {
        val occasionNum: Int = currentOccasion.occasion
        val leg: String = binding.etLeg.text.toString()
        val numTraps = Integer.parseInt(binding.etNumTraps.text.toString().ifBlank { "0" })

        if (checkInput(occasionNum, methodID, methodTypeID, trapTypeID, leg, numTraps)) {

            val occasion: Occasion = currentOccasion.copy()
            occasion.methodID = methodID
            occasion.methodTypeID = methodTypeID
            occasion.trapTypeID = trapTypeID
            occasion.leg = leg
            occasion.occasionDateTimeUpdated = Calendar.getInstance().time

            occasion.envTypeID = envTypeID
            occasion.vegetTypeID = vegetTypeID
            occasion.gotCaught = binding.cbGotCaught.isChecked
            occasion.numTraps = numTraps
            occasion.temperature = giveOutPutFloat(binding.etTemperature.text.toString())
            occasion.weather = binding.etWeather.text.toString().ifBlank { null }
            occasion.note = binding.etOccasionNote.text.toString().ifBlank { null }

            occasionListViewModel.updateOccasion(occasion)

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
        leg: String,
        numTraps: Int
    ): Boolean {
        return occasion > 0 &&
                method > 0 &&
                methodType > 0 &&
                trapType > 0 &&
                leg.isNotBlank() &&
                numTraps >= 0
    }

    private fun getHistoryWeather() {
        if (isOnline(requireContext())) {
            val locality = args.locList

            if (locality.xA == null || locality.yA == null){
                Toast.makeText(requireContext(), "No coordinates in this locality.", Toast.LENGTH_LONG).show()
                return
            }
            val unixtime = currentOccasion.occasionDateTimeCreated.time

            volleyViewModel.getHistoricalWeatherByCoordinates(locality.xA!!, locality.yA!!, unixtime)
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

    private fun giveOutPutFloat(input: String?): Float?{
        return if (input.isNullOrBlank() || input == "null") null else input.toFloat()
    }

}