package org.andrelsmoraes.bluetoothraffle.data.repository

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.andrelsmoraes.bluetoothraffle.data.mapper.BluetoothDeviceMapper
import org.andrelsmoraes.bluetoothraffle.domain.repository.BluetoothDeviceRepository
import org.andrelsmoraes.bluetoothraffle.domain.storage.AllDevicesStorage
import org.andrelsmoraes.bluetoothraffle.utils.downTo
import org.andrelsmoraes.bluetoothraffle.utils.safeRunWithBluetoothPermissions
import org.andrelsmoraes.bluetoothraffle.utils.time.Seconds

@SuppressLint("MissingPermission")
class BluetoothDeviceRepositoryImpl(
    private val context: Context,
    private val allDevicesStorage: AllDevicesStorage
) : BluetoothDeviceRepository {

    companion object {
        private val TAG = BluetoothDeviceRepositoryImpl::class.java.simpleName
    }

    private var stopRequested = false
    private var bluetoothAdapter: BluetoothAdapter? = null
    private val bluetoothReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when(intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? = intent.getBluetoothDevice()
                    if (device != null && device.isPhone()) {
                        allDevicesStorage.addDevice(BluetoothDeviceMapper.map(device))
                    }
                }
            }
        }
    }

    override fun startDiscovery(timeout: Seconds): Flow<Unit> = flow {
        context.safeRunWithBluetoothPermissions(
            onGranted = {
                bluetoothAdapter = context.getSystemService(BluetoothManager::class.java)?.adapter
                if (bluetoothAdapter?.isEnabled == false) {
                    emit(Unit)
                    return@flow
                }

                val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
                context.registerReceiver(bluetoothReceiver, filter)
                bluetoothAdapter?.startDiscovery()

                (timeout downTo Seconds.ZERO).forEach { _ ->
                    if (stopRequested) return@forEach
                    delay(Seconds.ONE.toMillis())
                }

                emit(Unit)
                stopRequested = false
                bluetoothAdapter?.cancelDiscovery()
                kotlin.runCatching { context.unregisterReceiver(bluetoothReceiver) }
            },
            onDenied = {
                Log.e(
                    TAG,
                    "Action 'startDiscovery' couldn't be executed because Bluetooth permission is not granted."
                )
            }
        )
    }

    override fun stopDiscovery(): Flow<Unit> = flow {
        context.safeRunWithBluetoothPermissions(
            onGranted = {
                stopRequested = true
                emit(Unit)
                bluetoothAdapter?.cancelDiscovery()
                kotlin.runCatching { context.unregisterReceiver(bluetoothReceiver) }
            },
            onDenied = {
                Log.e(
                    TAG,
                    "Action 'stopDiscovery' couldn't be executed because Bluetooth permission is not granted."
                )
            }
        )
    }

    private fun Intent.getBluetoothDevice(): BluetoothDevice? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice::class.java)
        } else {
            @Suppress("DEPRECATION")
            getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
        }
    }

    private fun BluetoothDevice.isPhone(): Boolean {
        return this.bluetoothClass.majorDeviceClass == BluetoothClass.Device.Major.PHONE
    }

}
