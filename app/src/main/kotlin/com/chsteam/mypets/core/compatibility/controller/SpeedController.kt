package com.chsteam.mypets.core.compatibility.controller

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.chsteam.mypets.core.compatibility.ControlType
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

interface SpeedController {

    val context: Context

    val locationCallback: LocationCallback

    var controlType: ControlType

    val locationRequest: LocationRequest
        get() = LocationRequest.Builder(1)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setMinUpdateIntervalMillis(1)
            .setMaxUpdateDelayMillis(1)
            .build()

    fun activeSpeed() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        this.controlType = ControlType.SENSOR
        LocationServices.getFusedLocationProviderClient(context)
            .requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    fun inactiveSpeed() {
        this.controlType = ControlType.HUMAN
        LocationServices.getFusedLocationProviderClient(context).removeLocationUpdates(locationCallback)
    }
}