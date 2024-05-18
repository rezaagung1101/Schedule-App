package com.powerhouse.ai.weathertraining.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.jetpack.compose.myweather.utils.Constanta.PERMISSION_REQUEST_CODE
import com.jetpack.compose.myweather.utils.Helper
import com.powerhouse.ai.weathertraining.BuildConfig
import com.powerhouse.ai.weathertraining.R
import com.powerhouse.ai.weathertraining.databinding.ActivityMainBinding
import com.powerhouse.ai.weathertraining.model.lib.WeatherRecord
import com.powerhouse.ai.weathertraining.model.remote.api.ApiConfig
import com.powerhouse.ai.weathertraining.viewModel.WeatherViewModel
import com.powerhouse.ai.weathertraining.viewModel.WeatherViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val appId = BuildConfig.APP_ID
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getViewModel()
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
        requestLocationUpdates()
        setupInformation()
    }

    private fun setupInformation() {
        if(checkLocationPermission()){
            weatherViewModel.apply{
                currentCity.observe(this@MainActivity){
                    getCityWeatherDB(it).observe(this@MainActivity){ weather ->
                        if(weather != null){
//                            userPreference.saveCurrentCity(weather.city)
                            setupInformation(weather)
                        }
                    }
                }
                if (latitude.value !=null && longitude.value !=null && latitude.value != 0.0 && longitude.value != 0.0){
                    getWeatherReport(latitude.value!!, longitude.value!!, appId)
                }else{
                    requestLocationUpdates()
                }
                showNoInternetSnackbar.observe(this@MainActivity){ showSnackbar ->
                    if(showSnackbar){
                        Snackbar.make(binding.root, "No Internet Connection", Snackbar.LENGTH_SHORT).show()
                        weatherViewModel.setSnackBarValue(false)
//                        getCityWeatherDB(userPreference.getCurrentCity()!!).observe(this@MainActivity){
//                            setupInformation(it)
//                        }
                    }
                }
                isLoading.observe(this@MainActivity) {
                    showLoading(it)
                }
            }
        }
    }

    private fun setupInformation(weather: WeatherRecord) {
        binding.apply{
//            userPreference.saveCurrentCity(weather.city)
            tvLocation.text = weather.city
            tvTemperature.text = resources.getString(R.string.temp_value, Helper.kelvinToCelcius(weather.temperature.toDouble()))
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun requestLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000 //10 second
            fastestInterval = 5000 //5 second
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        if (checkLocationPermission()) {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest, locationCallback, Looper.getMainLooper()
            )
        } else {
            requestPermissions()
        }
    }

    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates()
                setupInformation()
            } else {
//                Snackbar.make(binding.root, "Location Permission Denied", Snackbar.LENGTH_SHORT).show()
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
                val uri: Uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .show()
    }

    private fun getViewModel(): WeatherViewModel {
        val viewModel: WeatherViewModel by viewModels {
            WeatherViewModelFactory(this, ApiConfig.getApiService(this))
        }
        return viewModel
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

}