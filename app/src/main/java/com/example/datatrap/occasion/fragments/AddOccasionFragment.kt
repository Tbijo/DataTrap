package com.example.datatrap.occasion.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentAddOccasionBinding
import com.example.datatrap.models.*
import com.example.datatrap.occasion.fragments.weather.Weather
import com.example.datatrap.viewmodels.*
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
    private lateinit var sessionViewModel: SessionViewModel
    private lateinit var userViewModel: UserViewModel

    private lateinit var envTypeNameMap: MutableMap<String, Long?>
    private lateinit var methodNameMap: MutableMap<String, Long>
    private lateinit var metTypeNameMap: MutableMap<String, Long>
    private lateinit var trapTypeNameMap: MutableMap<String, Long>
    private lateinit var vegTypeNameMap: MutableMap<String, Long?>

    private var temperature: Float? = null
    private var weatherGlob: String? = null

    private lateinit var sharedViewModel: SharedViewModel
    private var imgName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddOccasionBinding.inflate(inflater, container, false)
        occasionViewModel = ViewModelProvider(this).get(OccasionViewModel::class.java)
        envTypeViewModel = ViewModelProvider(this).get(EnvTypeViewModel::class.java)
        methodViewModel = ViewModelProvider(this).get(MethodViewModel::class.java)
        metTypeViewModel = ViewModelProvider(this).get(MethodTypeViewModel::class.java)
        trapTypeViewModel = ViewModelProvider(this).get(TrapTypeViewModel::class.java)
        vegTypeViewModel = ViewModelProvider(this).get(VegetTypeViewModel::class.java)
        sessionViewModel = ViewModelProvider(this).get(SessionViewModel::class.java)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        envTypeNameMap = mutableMapOf()
        methodNameMap = mutableMapOf()
        metTypeNameMap = mutableMapOf()
        trapTypeNameMap = mutableMapOf()
        vegTypeNameMap = mutableMapOf()

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        sharedViewModel.dataToShare.observe(requireActivity(), Observer<String> {
            imgName = it
        })

        fillDropDown()

        // nastavit leg na meno usera
        userViewModel.getActiveUser()
        userViewModel.activeUser.observe(viewLifecycleOwner, {
            binding.etLeg.setText(it.userName)
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.occasion_menu, menu)
        menu.findItem(R.id.menu_delete).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> insertOccasion()
            R.id.menu_camera -> goToCamera()
            R.id.menu_weather -> getCurrentWeather()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        fillDropDown()
    }

    private fun fillDropDown() {
        envTypeViewModel.envTypeList.observe(viewLifecycleOwner, {
            it.forEach { envType ->
                envTypeNameMap[envType.envTypeName] = envType.envTypeId
            }
            envTypeNameMap["null"] = null
            // env type adapter
            val dropDownArrAdapEnvType =
                ArrayAdapter(
                    requireContext(),
                    R.layout.dropdown_names,
                    envTypeNameMap.keys.toList()
                )
            binding.autoCompTvEnvType.setAdapter(dropDownArrAdapEnvType)
        })

        methodViewModel.methodList.observe(viewLifecycleOwner, {
            it.forEach { method ->
                methodNameMap[method.methodName] = method.methodId
            }
            // method adapter
            val dropDownArrAdapMethod =
                ArrayAdapter(requireContext(), R.layout.dropdown_names, methodNameMap.keys.toList())
            binding.autoCompTvMethod.setAdapter(dropDownArrAdapMethod)
        })
        metTypeViewModel.methodTypeList.observe(viewLifecycleOwner, {
            it.forEach { metType ->
                metTypeNameMap[metType.methodTypeName] = metType.methodTypeId
            }
            // method type adapter
            val dropDownArrAdapMethodType =
                ArrayAdapter(requireContext(), R.layout.dropdown_names, metTypeNameMap.keys.toList())
            binding.autoCompTvMethodType.setAdapter(dropDownArrAdapMethodType)
        })
        trapTypeViewModel.trapTypeList.observe(viewLifecycleOwner, {
            it.forEach { trapType ->
                trapTypeNameMap[trapType.trapTypeName] = trapType.trapTypeId
            }
            // trap type adapter
            val dropDownArrAdapTrapType =
                ArrayAdapter(requireContext(), R.layout.dropdown_names, trapTypeNameMap.keys.toList())
            binding.autoCompTvTrapType.setAdapter(dropDownArrAdapTrapType)
        })
        vegTypeViewModel.vegetTypeList.observe(viewLifecycleOwner, {
            it.forEach { vegType ->
                vegTypeNameMap[vegType.vegetTypeName] = vegType.vegetTypeId
            }
            vegTypeNameMap["null"] = null

            // veget type adapter
            val dropDownArrAdapVegType =
                ArrayAdapter(requireContext(), R.layout.dropdown_names, vegTypeNameMap.keys.toList())
            binding.autoCompTvVegType.setAdapter(dropDownArrAdapVegType)
        })

    }

    private fun goToCamera() {
        val action = AddOccasionFragmentDirections.actionAddOccasionFragmentToTakePhotoFragment(
            "Occasion",
            null
        )
        findNavController().navigate(action)
    }

    private fun insertOccasion() {
        val occasionNum: Int = args.newOccasionNumber
        val method: Long = methodNameMap.getOrDefault(binding.autoCompTvMethod.text.toString(), 1)
        val methodType: Long = metTypeNameMap.getOrDefault(binding.autoCompTvMethodType.text.toString(), 1)
        val trapType: Long = trapTypeNameMap.getOrDefault(binding.autoCompTvTrapType.text.toString(), 1)
        val leg: String = binding.etLeg.text.toString()
        val numTraps = binding.etNumTraps.text.toString()

        if (checkInput(occasionNum, method, methodType, trapType, leg, numTraps)) {
            val envType: Long? = envTypeNameMap.getOrDefault(binding.autoCompTvEnvType.text.toString(), null)
            val vegType: Long? = vegTypeNameMap.getOrDefault(binding.autoCompTvVegType.text.toString(), null)
            val gotCaught = false
            val numMice = 0
            val deviceID: String = Settings.Secure.getString(
                requireContext().contentResolver,
                Settings.Secure.ANDROID_ID
            )
            val temper = if (binding.etTemperature.text.toString().isBlank()) null else temperature
            val weat = if (binding.etWeather.text.toString().isBlank()) null else weatherGlob

            val note: String? = if (binding.etOccasionNote.text.toString().isBlank()) null else binding.etOccasionNote.text.toString()

            // zvacsit numOcc v Session
            updateSessionNumOcc()

            // ulozit occasion
            val occasion = Occasion(
                0,
                occasionNum,
                deviceID,
                args.locality.localityId,
                args.session.sessionId,
                method,
                methodType,
                trapType,
                envType,
                vegType,
                Calendar.getInstance().time,
                null,
                gotCaught,
                Integer.parseInt(numTraps),
                numMice,
                temper,
                weat,
                leg,
                note,
                imgName
            )

            occasionViewModel.insertOccasion(occasion)

            Toast.makeText(requireContext(), "New occasion added.", Toast.LENGTH_SHORT).show()

            findNavController().navigateUp()
        } else {
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun updateSessionNumOcc() {
        val updatedSession: Session = args.session
        updatedSession.numOcc = (updatedSession.numOcc + 1)
        updatedSession.sessionDateTimeUpdated = Calendar.getInstance().time
        sessionViewModel.updateSession(updatedSession)
    }

    private fun checkInput(
        occasion: Int,
        method: Long,
        methodType: Long,
        trapType: Long,
        leg: String,
        numTraps: String
    ): Boolean {
        return occasion.toString().isNotEmpty() && method.toString()
            .isNotEmpty() && methodType.toString().isNotEmpty() && trapType.toString()
            .isNotEmpty() && leg.isNotEmpty() && numTraps.isNotEmpty()
    }

    private fun getCurrentWeather() {
        if (isOnline(requireContext())) {
            val weather = Weather(requireContext())
            weather.getCurrentWeatherByCoordinates(
                args.locality.xA,
                args.locality.yA,
                object : Weather.VolleyResponseListener {
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

}