package com.powerhouse.ai.weathertraining.ui.schedule

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.jetpack.compose.myweather.utils.Constanta
import com.jetpack.compose.myweather.utils.Helper
import com.powerhouse.ai.weathertraining.BuildConfig
import com.powerhouse.ai.weathertraining.R
import com.powerhouse.ai.weathertraining.adapter.ListScheduleAdapter
import com.powerhouse.ai.weathertraining.databinding.FragmentHomeBinding
import com.powerhouse.ai.weathertraining.model.lib.Schedule
import com.powerhouse.ai.weathertraining.model.lib.WeatherRecord
import com.powerhouse.ai.weathertraining.model.remote.api.ApiConfig
import com.powerhouse.ai.weathertraining.utils.QueryType
import com.powerhouse.ai.weathertraining.utils.UserPreference
import com.powerhouse.ai.weathertraining.viewModel.ScheduleViewModel
import com.powerhouse.ai.weathertraining.viewModel.ScheduleViewModelFactory
import com.powerhouse.ai.weathertraining.viewModel.WeatherViewModel
import com.powerhouse.ai.weathertraining.viewModel.WeatherViewModelFactory

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val appId = BuildConfig.APP_ID
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var scheduleViewModel: ScheduleViewModel
    private lateinit var preference: UserPreference
    private var queryType = QueryType.CURRENT_DAY;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preference = UserPreference(requireContext())
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        weatherViewModel = getWeatherViewModel(requireContext())
        scheduleViewModel = getScheduleViewModel(requireContext())
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                for (location in locationResult.locations) {
                    weatherViewModel.apply {
                        setLocation(location.latitude, location.longitude)
                        getWeatherReport(latitude.value!!, longitude.value!!, appId)
                    }
                    stopLocationUpdates()
                }
            }
        }
        requestLocationUpdates(requireContext())
        setupInformation(requireContext())
        showNearestSchedule()
    }

    private fun showNearestSchedule() {
        scheduleViewModel.apply {
            getNearestSchedule()?.observe(viewLifecycleOwner) {
                checkQuery(it)
                if (it != null) {
                    val remainingTime = Helper.timeDifference(it.day, it.startTime)
                    binding.tvNearestScheduleTitle.text = it.scheduleName
                    binding.tvNearestScheduleTime.text = "${it.startTime}-${it.endTime}"
                    binding.tvNearestScheduleDay.text = if (remainingTime == "(In 7 days)") "Today"
                    else remainingTime
                }
            }
        }
    }

    private fun checkQuery(schedule: Schedule?){
        if (schedule == null) {
            val newQueryType: QueryType = when (queryType) {
                QueryType.CURRENT_DAY -> QueryType.NEXT_DAY
                QueryType.NEXT_DAY -> QueryType.PAST_DAY
                else -> QueryType.CURRENT_DAY
            }
            scheduleViewModel.setQueryType(newQueryType)
            queryType = newQueryType
        }
    }

    private fun setupInformation(context: Context) {
        if (checkLocationPermission(context)) {
            weatherViewModel.apply {
                currentCity.observe(viewLifecycleOwner) {
                    getCityWeatherDB(it).observe(viewLifecycleOwner) { weather ->
                        if (weather != null) {
                            preference.saveCurrentCity(weather.city)
                            setupInformation(weather)
                        }
                    }
                }
                if (latitude.value != null && longitude.value != null && latitude.value != 0.0 && longitude.value != 0.0) {
                    getWeatherReport(latitude.value!!, longitude.value!!, appId)
                } else {
                    requestLocationUpdates(context)
                }
                showNoInternetSnackbar.observe(viewLifecycleOwner) { showSnackbar ->
                    if (showSnackbar) {
                        Snackbar.make(binding.root, "No Internet Connection", Snackbar.LENGTH_SHORT)
                            .show()
                        weatherViewModel.setSnackBarValue(false)
                        getCityWeatherDB(preference.getCurrentCity()!!).observe(viewLifecycleOwner) {
                            setupInformation(it)
                        }
                    }
                }
                isLoading.observe(viewLifecycleOwner) {
                    showLoading(it)
                }
                getCurrentTime()
                currentTime.observe(viewLifecycleOwner) {
                    showCurrentTime(it)
                }
            }
        }
        scheduleViewModel.getTodaySchedule().observe(viewLifecycleOwner) {
            val layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            binding.rvSchedule.layoutManager = layoutManager
            binding.rvSchedule.adapter = ListScheduleAdapter(it)
        }
        binding.swipeRefresh.setOnRefreshListener {
            setupInformation(context)
            binding.swipeRefresh.isRefreshing = true
            // Use a Handler to post a delayed action
            Handler().postDelayed({
                binding.swipeRefresh.isRefreshing = false
            }, 1000)
        }

        binding.tvDay.text = Helper.getTodaysDayName()
    }

    private fun showCurrentTime(time: String) {
        binding.tvTime.text = time
    }

    private fun setupInformation(weather: WeatherRecord) {
        binding.apply {
            preference.saveCurrentCity(weather.city)
            tvLocation.text = weather.city
            tvTemperature.text = resources.getString(
                R.string.temp_value,
                Helper.kelvinToCelcius(weather.temperature.toDouble())
            )
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun requestLocationUpdates(context: Context) {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000 //10 second
            fastestInterval = 5000 //5 second
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        if (checkLocationPermission(context)) {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest, locationCallback, Looper.getMainLooper()
            )
        } else {
            requestPermissions(requireActivity())
        }
    }

    private fun checkLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            Constanta.PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constanta.PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates(requireContext())
                setupInformation(requireContext())
            } else {
                showPermissionDeniedSnackbar()
                binding.tvLocation.text = getString(R.string.permission_denied)
            }
        }
    }

    private fun showPermissionDeniedSnackbar() {
        Snackbar.make(
            binding.root,
            "Location permission denied. Please grant the permission from the app settings.",
            Snackbar.LENGTH_SHORT
        )
            .setAction("Open Settings") {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", requireContext().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .show()
    }

    private fun getWeatherViewModel(context: Context): WeatherViewModel {
        val viewModel: WeatherViewModel by viewModels {
            WeatherViewModelFactory(context, ApiConfig.getApiService(context))
        }
        return viewModel
    }

    private fun getScheduleViewModel(context: Context): ScheduleViewModel {
        val viewModel: ScheduleViewModel by viewModels {
            ScheduleViewModelFactory(context)
        }
        return viewModel
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}