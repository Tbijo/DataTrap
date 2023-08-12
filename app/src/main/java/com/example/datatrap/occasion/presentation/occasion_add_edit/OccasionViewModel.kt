package com.example.datatrap.occasion.presentation.occasion_add_edit

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.example.datatrap.R
import com.example.datatrap.core.data.pref.PrefViewModel
import com.example.datatrap.core.util.Constants
import com.example.datatrap.mouse.domain.model.MyWeather
import com.example.datatrap.occasion.data.Occasion
import com.example.datatrap.occasion.presentation.occasion_list.OccasionListViewModel
import com.example.datatrap.camera.data.occasion_image.OccasionImage
import com.example.datatrap.settings.envtype.data.EnvType
import com.example.datatrap.settings.envtype.presentation.EnvTypeViewModel
import com.example.datatrap.settings.method.data.Method
import com.example.datatrap.settings.method.presentation.MethodViewModel
import com.example.datatrap.settings.methodtype.data.MethodType
import com.example.datatrap.settings.methodtype.presentation.MethodTypeViewModel
import com.example.datatrap.settings.traptype.data.TrapType
import com.example.datatrap.settings.traptype.presentation.TrapTypeViewModel
import com.example.datatrap.settings.user.presentation.UserViewModel
import com.example.datatrap.settings.vegettype.data.VegetType
import com.example.datatrap.settings.vegettype.presentation.VegetTypeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class OccasionViewModel @Inject constructor(
    private val reqQue: RequestQueue
): ViewModel() {
    private val occasionListViewModel: OccasionListViewModel by viewModels()
    private val envTypeViewModel: EnvTypeViewModel by viewModels()
    private val methodViewModel: MethodViewModel by viewModels()
    private val metTypeViewModel: MethodTypeViewModel by viewModels()
    private val trapTypeViewModel: TrapTypeViewModel by viewModels()
    private val vegTypeViewModel: VegetTypeViewModel by viewModels()
    private val occasionImageViewModel: OccasionImageViewModel by viewModels()
    private val volleyViewModel: VolleyViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private val prefViewModel: PrefViewModel by viewModels()

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
    private var occasionId: Long = 0

    init {
        fillDropDown()

        setListeners()

        // nastavit leg na meno usera
        prefViewModel.readUserIdPref.observe(viewLifecycleOwner) {
            userViewModel.getActiveUser(it).observe(viewLifecycleOwner) { user ->
                if (user != null) {
                    binding.etLeg.setText(user.userName)
                }
            }
        }

        volleyViewModel.weatherCoor.observe(viewLifecycleOwner) {
            binding.etTemperature.setText(it.temp.toString())
            binding.etWeather.setText(it.weather)
        }

        volleyViewModel.errorWeather.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }

        R.id.menu_save -> insertOccasion()
        R.id.menu_camera -> goToCamera()
        R.id.menu_weather -> getCurrentWeather()

        // UPDATE
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

        R.id.menu_save -> updateOccasion()
        R.id.menu_camera -> goToCamera()
        R.id.menu_weather -> getHistoryWeather()
        R.id.menu_delete -> deleteOccasion()

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

    private fun fillDropDown() {
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
        }

    }

    private fun goToCamera() {
        if (occasionId <= 0) {
            Toast.makeText(requireContext(), "You need to insert a occasion first.", Toast.LENGTH_LONG)
                .show()
            return
        }

        val action = AddOccasionFragmentDirections.actionAddOccasionFragmentToTakePhotoFragment(
            "Occasion",
            occasionId,
            ""
        )
        findNavController().navigate(action)
    }

    private fun insertOccasion() {
        if (occasionId > 0) {
            Toast.makeText(requireContext(), "Occasion already inserted.", Toast.LENGTH_LONG).show()
            return
        }

        val occasionNum: Int = args.newOccasionNumber
        val method: Long = methodID
        val methodType: Long = methodTypeID
        val trapType: Long = trapTypeID
        val leg: String = binding.etLeg.text.toString()
        val numTraps: Int = Integer.parseInt(binding.etNumTraps.text.toString().ifBlank { "0" })

        if (checkInput(occasionNum, method, methodType, trapType, leg, numTraps)) {
            val envType: Long? = envTypeID
            val vegType: Long? = vegetTypeID
            val gotCaught = false
            val numMice = 0
            val temper = if (binding.etTemperature.text.toString().isBlank()) null else binding.etTemperature.text.toString().toFloat()
            val weat = binding.etWeather.text.toString().ifBlank { null }

            val note: String? = binding.etOccasionNote.text.toString().ifBlank { null }

            // ulozit occasion
            val occasion = Occasion(
                0,
                occasionNum,
                args.locList.localityId,
                args.session.sessionId,
                method,
                methodType,
                trapType,
                envType,
                vegType,
                Calendar.getInstance().time,
                null,
                gotCaught,
                numTraps,
                numMice,
                temper,
                weat,
                leg,
                note,
                Calendar.getInstance().time.time
            )

            occasionListViewModel.insertOccasion(occasion)

            Toast.makeText(requireContext(), "New occasion added.", Toast.LENGTH_SHORT).show()

            occasionListViewModel.occasionId.observe(viewLifecycleOwner) {
                occasionId = it
            }

        } else {
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG)
                .show()
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

    private fun getCurrentWeather() {
        if (isOnline(requireContext())) {

            if (args.locList.xA == null || args.locList.yA == null) {
                Toast.makeText(requireContext(), "No coordinates in this locality.", Toast.LENGTH_LONG).show()
                return
            }

            volleyViewModel.getCurrentWeatherByCoordinates(args.locList.xA!!, args.locList.yA!!)
        } else {
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

    // UPDATE
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

    // Volley
    val weatherCoor: MutableLiveData<MyWeather> = MutableLiveData<MyWeather>()
    val errorWeather: MutableLiveData<String> = MutableLiveData<String>()

    fun getCurrentWeatherByCoordinates(sirka: Float, dlzka: Float) {
        viewModelScope.launch(Dispatchers.IO) {
            val url =
                Constants.QUERRY_CURRENT_WEATHER_COOR + Constants.SIRKA_URL + sirka + Constants.DLZKA_URL + dlzka + Constants.UNITS + Constants.OWM_API_KEY_URL

            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                { response ->

                    val mainJSON = response["main"] as JSONObject
                    val temp = mainJSON.getInt("temp")

                    val weatherArray = response.getJSONArray("weather") as JSONArray
                    val weatherJSON = weatherArray.getJSONObject(0)
                    val weather = weatherJSON.getString("main")

                    weatherCoor.value = MyWeather(temp, weather)
                },
                {
                    errorWeather.value = "Error in Response."
                }
            )
            reqQue.add(jsonObjectRequest)
        }
    }

    fun getHistoricalWeatherByCoordinates(sirka: Float, dlzka: Float, unixTime: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentTime = Calendar.getInstance().time.time
            if (currentTime - unixTime >= Constants.FIVE_DAYS) {
                errorWeather.value = "Occasion is older than 5 days."
                return@launch
            }

            val url =
                Constants.QUERRY_HISTORY_WEATHER_COOR + Constants.SIRKA_URL + sirka + Constants.DLZKA_URL + dlzka + Constants.DATE_TIME_URL + (unixTime / 1000) + Constants.UNITS + Constants.OWM_API_KEY_URL

            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                { response ->

                    val mainJSON = response["current"] as JSONObject
                    val temp = mainJSON.getInt("temp")

                    val weatherArray = mainJSON.getJSONArray("weather") as JSONArray
                    val weatherJSON = weatherArray.getJSONObject(0)
                    val weather = weatherJSON.getString("main")

                    weatherCoor.value = MyWeather(temp, weather)
                },
                {
                    errorWeather.value = "Error in Response."
                }
            )
            reqQue.add(jsonObjectRequest)
        }
    }
}