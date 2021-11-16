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
    private lateinit var userViewModel: UserViewModel

    private lateinit var envTypeList: List<EnvType>
    private lateinit var methodList: List<Method>
    private lateinit var metTypeList: List<MethodType>
    private lateinit var vegTypeList: List<VegetType>
    private lateinit var trapTypeList: List<TrapType>

    private lateinit var sharedViewModel: SharedViewModel
    private var imgName: String? = null

    private var methodID: Long = 0
    private var methodTypeID: Long = 0
    private var trapTypeID: Long = 0
    private var envTypeID: Long? = null
    private var vegetTypeID: Long? = null

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
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        sharedViewModel.dataToShare.observe(requireActivity(), {
            imgName = it
        })

        fillDropDown()

        setListeners()

        // nastavit leg na meno usera
        userViewModel.getActiveUser()
        userViewModel.activeUser.observe(viewLifecycleOwner, {
            if (it != null) {
                binding.etLeg.setText(it.userName)
            }
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

    private fun setListeners() {
        binding.autoCompTvEnvType.setOnItemClickListener { parent, view, position, id ->
            val name: String = parent.getItemAtPosition(position) as String
            envTypeID = envTypeList.firstOrNull() {
                it.envTypeName == name
            }?.envTypeId
        }

        binding.autoCompTvMethod.setOnItemClickListener { parent, view, position, id ->
            val name: String = parent.getItemAtPosition(position) as String
            methodID = methodList.first {
                it.methodName == name
            }.methodId
        }

        binding.autoCompTvMethodType.setOnItemClickListener { parent, view, position, id ->
            val name: String = parent.getItemAtPosition(position) as String
            methodTypeID = metTypeList.first {
                it.methodTypeName == name
            }.methodTypeId
        }

        binding.autoCompTvTrapType.setOnItemClickListener { parent, view, position, id ->
            val name: String = parent.getItemAtPosition(position) as String
            trapTypeID = trapTypeList.first {
                it.trapTypeName == name
            }.trapTypeId
        }

        binding.autoCompTvVegType.setOnItemClickListener { parent, view, position, id ->
            val name: String = parent.getItemAtPosition(position) as String
            vegetTypeID = vegTypeList.firstOrNull {
                it.vegetTypeName == name
            }?.vegetTypeId
        }
    }

    private fun fillDropDown() {
        envTypeViewModel.envTypeList.observe(viewLifecycleOwner, {
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
        })

        methodViewModel.methodList.observe(viewLifecycleOwner, {
            methodList = it
            val nameList = mutableListOf<String>()
            it.forEach { method ->
                nameList.add(method.methodName)
            }
            // method adapter
            val dropDownArrAdapMethod =
                ArrayAdapter(requireContext(), R.layout.dropdown_names, nameList)
            binding.autoCompTvMethod.setAdapter(dropDownArrAdapMethod)
        })

        metTypeViewModel.methodTypeList.observe(viewLifecycleOwner, {
            metTypeList = it
            val nameList = mutableListOf<String>()
            it.forEach { metType ->
                nameList.add(metType.methodTypeName)
            }
            // method type adapter
            val dropDownArrAdapMethodType =
                ArrayAdapter(requireContext(), R.layout.dropdown_names, nameList)
            binding.autoCompTvMethodType.setAdapter(dropDownArrAdapMethodType)
        })

        trapTypeViewModel.trapTypeList.observe(viewLifecycleOwner, {
            trapTypeList = it
            val nameList = mutableListOf<String>()
            it.forEach { trapType ->
                nameList.add(trapType.trapTypeName)
            }
            // trap type adapter
            val dropDownArrAdapTrapType =
                ArrayAdapter(requireContext(), R.layout.dropdown_names, nameList)
            binding.autoCompTvTrapType.setAdapter(dropDownArrAdapTrapType)
        })

        vegTypeViewModel.vegetTypeList.observe(viewLifecycleOwner, {
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
        })

    }

    private fun goToCamera() {
        val action = AddOccasionFragmentDirections.actionAddOccasionFragmentToTakePhotoFragment(
            "Occasion",
            imgName
        )
        findNavController().navigate(action)
    }

    private fun insertOccasion() {
        val occasionNum: Int = args.newOccasionNumber
        val method: Long = methodID
        val methodType: Long = methodTypeID
        val trapType: Long = trapTypeID
        val leg: String = binding.etLeg.text.toString()
        val numTraps = Integer.parseInt(binding.etNumTraps.text.toString())

        if (checkInput(occasionNum, method, methodType, trapType, leg, numTraps)) {
            val envType: Long? = envTypeID
            val vegType: Long? = vegetTypeID
            val gotCaught = false
            val numMice = 0
            val deviceID: String = Settings.Secure.getString(
                requireContext().contentResolver,
                Settings.Secure.ANDROID_ID
            )
            val temper = if (binding.etTemperature.text.toString().isBlank()) null else binding.etTemperature.text.toString().toFloat()
            val weat = if (binding.etWeather.text.toString().isBlank()) null else binding.etWeather.text.toString()

            val note: String? = if (binding.etOccasionNote.text.toString().isBlank()) null else binding.etOccasionNote.text.toString()

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
                numTraps,
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
            val weather = Weather(requireContext())
            weather.getCurrentWeatherByCoordinates(
                args.locality.xA,
                args.locality.yA,
                object : Weather.VolleyResponseListener {
                    override fun onResponse(temp: Int, weather: String) {
                        binding.etTemperature.setText(temp.toString())
                        binding.etWeather.setText(weather)
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