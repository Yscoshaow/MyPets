package com.chsteam.mypets.internal.compatibility

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

interface SpeedController {

    val context: Context

    val locationCallback: LocationCallback

    var controlType: ControlType

    val locationRequest: LocationRequest
        get() = LocationRequest.Builder(1000).build()

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