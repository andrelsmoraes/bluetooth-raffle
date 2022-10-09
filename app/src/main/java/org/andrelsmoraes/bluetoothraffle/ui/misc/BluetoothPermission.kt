package org.andrelsmoraes.bluetoothraffle.ui.misc

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import org.andrelsmoraes.bluetoothraffle.R

sealed class BluetoothPermission(
    fragment: Fragment,
    onGranted: () -> Unit,
    onDenied: () -> Unit
) {

    private var permissionsResultLauncher: ActivityResultLauncher<Array<String>>? =
        fragment.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (isGranted(it)) {
                onGranted()
            } else {
                onDenied()
            }
        }

    protected abstract fun getPermissions(): Array<String>
    protected abstract fun isGranted(result: Map<String, Boolean>): Boolean
    abstract fun isGranted(): Boolean
    abstract fun getPermissionMessage(): String

    fun requestPermission() {
        permissionsResultLauncher?.launch(getPermissions())
    }

    fun clear() {
        permissionsResultLauncher = null
    }

    @RequiresApi(Build.VERSION_CODES.S)
    class Android12(
        private val fragment: Fragment,
        onGranted: () -> Unit,
        onDenied: () -> Unit
    ) : BluetoothPermission(fragment, onGranted, onDenied) {
        override fun isGranted(): Boolean = fragment.requireContext().checkSelfPermission(
            Manifest.permission.BLUETOOTH_CONNECT
        ) == PackageManager.PERMISSION_GRANTED

        override fun isGranted(result: Map<String, Boolean>): Boolean =
            result.getOrDefault(Manifest.permission.BLUETOOTH_CONNECT, false)

        override fun getPermissions(): Array<String> = arrayOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT
        )

        override fun getPermissionMessage(): String =
            fragment.getString(R.string.text_permission_warning_android_12)
    }

    class Legacy(
        private val fragment: Fragment,
        onGranted: () -> Unit,
        onDenied: () -> Unit
    ) : BluetoothPermission(fragment, onGranted, onDenied) {
        override fun isGranted(): Boolean = fragment.requireContext().checkSelfPermission(
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        override fun isGranted(result: Map<String, Boolean>): Boolean =
            result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)

        override fun getPermissions(): Array<String> = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )

        override fun getPermissionMessage(): String =
            fragment.getString(R.string.text_permission_warning)
    }

    object Builder {
        fun create(
            fragment: Fragment,
            onGranted: () -> Unit,
            onDenied: () -> Unit
        ): BluetoothPermission {
            return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                Legacy(fragment, onGranted, onDenied)
            } else {
                Android12(fragment, onGranted, onDenied)
            }
        }

    }

}
