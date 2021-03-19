package ru.d3st.academyandroid

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import timber.log.Timber
import java.io.IOException
import java.util.*


class LocationFragment : Fragment() {


    private val TAG = LocationFragment::class.java.simpleName

    private val REQUEST_LOCATION_PERMISSION = 1

    private var fusedLocationClient: FusedLocationProviderClient? = null


    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())


        val homeLatLng = LatLng(59.94019072565021, 30.31458675591602)

        val zoomLevel = 15f

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, zoomLevel))
        //googleMap.addMarker(MarkerOptions().position(homeLatLng))


        //применить стиль из папки Raw
        setMapStyle(googleMap)


        //включить слежение
        enableMyLocation(googleMap)

        val myLocationButton: FloatingActionButton =
            requireActivity().findViewById(R.id.fab_location)

        myLocationButton.setOnClickListener {
            getMyLocation(googleMap)
        }


        val confirmButton: Button = requireActivity().findViewById(R.id.confirm_button)
        confirmButton.setOnClickListener {
            this.findNavController().navigate(R.id.action_location_to_movieListFragment)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getMyLocation(googleMap: GoogleMap) {

        Timber.tag(TAG).i("Button my location has been clicked")

        fusedLocationClient?.lastLocation
            ?.addOnSuccessListener { location: Location? ->
                updateMapLocation(location, googleMap)

            }
    }

    private fun updateMapLocation(location: Location?, googleMap: GoogleMap) {
        googleMap.moveCamera(
            CameraUpdateFactory.newLatLng(
                LatLng(
                    location?.latitude ?: 0.0,
                    location?.longitude ?: 0.0
                )
            )
        )

        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15.0f))
        location?.let {
        }
    }


    //включить слежение за местоположением
    @SuppressLint("MissingPermission")
    private fun enableMyLocation(map: GoogleMap) {
        if (isPermissionGranted()) {
            map.isMyLocationEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = false
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    //проверка доступа к геолокации
    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        mapFragment?.getMapAsync(callback)
    }

    private fun setMapStyle(map: GoogleMap) {
        try {
            val success = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(),
                    R.raw.map_style
                )
            )
            if (!success) {
                Timber.tag(TAG).i("Style apply failed")
            }
        } catch (e: Resources.NotFoundException) {
            Timber.tag(TAG).e(e, "Can't find style. Error: ")
        }
    }
/*    fun getAddressFromLocation(
        latitude: Double,
        longitude: Double,
        context: Context?,
        handler: Handler?
    ) {
        val thread: Thread = object : Thread() {
            override fun run() {
                val geocoder = Geocoder(context, Locale.getDefault())
                var result: String? = null
                try {
                    val addressList: List<Address>? =
                        geocoder.getFromLocation(latitude, longitude, 1)
                    if (addressList != null && addressList.isNotEmpty()) {
                        val address: Address = addressList[0]
                        val sb = StringBuilder()
                        for (i in 0 until address.maxAddressLineIndex) {
                            sb.append(address.getAddressLine(i)) //.append("\n");
                        }
                        sb.append(address.locality).append("\n")
                        sb.append(address.postalCode).append("\n")
                        sb.append(address.countryName)
                        result = sb.toString()
                    }
                } catch (e: IOException) {
                    Timber.tag("Location Address Loader").e(e, "Unable connect to Geocoder")
                } finally {
                    val message: Message = Message.obtain()
                    message.target = handler
                    if (result != null) {
                        message.what = 1
                        val bundle = Bundle()
                        bundle.putString("address", result)
                        message.data = bundle
                    } else {
                        message.what = 1
                        val bundle = Bundle()
                        result = " Unable to get address for this location."
                        bundle.putString("address", result)
                        message.data = bundle
                    }
                    message.sendToTarget()
                }
            }
        }
        thread.start()
    }*/

}



