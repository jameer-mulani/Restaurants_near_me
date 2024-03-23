package com.deserve.restaurantsnearme.location

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

object LocationUtility {
    var locationPriority = Priority.PRIORITY_BALANCED_POWER_ACCURACY

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(
        fusedLocationProviderClient: FusedLocationProviderClient,
        onSuccess: (Location) -> Unit, onFailed: (Throwable) -> Unit
    ) {
        fusedLocationProviderClient.getCurrentLocation(
            locationPriority,
            CancellationTokenSource().token
        ).addOnSuccessListener(onSuccess).addOnFailureListener(onFailed)
    }

}