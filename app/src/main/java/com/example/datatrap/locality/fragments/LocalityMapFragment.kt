package com.example.datatrap.locality.fragments

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.datatrap.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class LocalityMapFragment : Fragment() {

    private val args by navArgs<LocalityMapFragmentArgs>()

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->

        googleMap.isMyLocationEnabled = true

        if (args.localities.isNotEmpty()) {
            args.localities.forEach {
                val locality = LatLng(it.x.toDouble(), it.y.toDouble())
                googleMap.addMarker(MarkerOptions().position(locality).title(it.localityName))
            }

            val lastLat = args.localities.last().x.toDouble()
            val lastlon = args.localities.last().y.toDouble()
            val lastLatLon = LatLng(lastLat, lastlon)
            // bolo by treba nastavit najblizsiu takto sa nastavi najnovsia
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLatLon, 13F))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_locality_map, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        return view
    }

}