package org.andrelsmoraes.bluetoothraffle.utils

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class ShakeDetector(context: Context) : SensorEventListener {
    private val dataPoints: MutableList<DataPoint> = ArrayList()
    private var lastUpdate: Long = 0
    private var lastShake: Long = 0
    private var lastX = 0f
    private var lastY = 0f
    private var lastZ = 0f
    private val sensorManager: SensorManager
    var shakeListener: ShakeListener? = null

    init {
        sensorManager = context
            .getSystemService(Activity.SENSOR_SERVICE) as SensorManager
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    fun registerSensor() {
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    fun unregisterSensor() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val curTime = System.currentTimeMillis()
            // if a shake in last X seconds ignore.
            if (lastShake != 0L && curTime - lastShake < IGNORE_EVENTS_AFTER_SHAKE) return
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            if (lastX != 0f && lastY != 0f && lastZ != 0f && (lastX != x || lastY != y || lastZ != z)) {
                val dataPoint = DataPoint(
                    lastX - x, lastY - y,
                    lastZ - z, curTime
                )
                dataPoints.add(dataPoint)
                if (curTime - lastUpdate > SHAKE_CHECK_THRESHOLD) {
                    lastUpdate = curTime
                    checkForShake()
                }
            }
            lastX = x
            lastY = y
            lastZ = z
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    private fun checkForShake() {
        val curTime = System.currentTimeMillis()
        val cutOffTime = curTime - KEEP_DATA_POINTS_FOR
        while (dataPoints.size > 0
            && dataPoints[0].atTimeMilliseconds < cutOffTime
        ) dataPoints.removeAt(0)
        var xPos = 0
        var xNeg = 0
        var xDir = 0
        var yPos = 0
        var yNeg = 0
        var yDir = 0
        var zPos = 0
        var zNeg = 0
        var zDir = 0
        for (dp in dataPoints) {
            if (dp.x > POSITIVE_COUNTER_THRESHOLD && xDir < 1) {
                ++xPos
                xDir = 1
            }
            if (dp.x < NEGATIVE_COUNTER_THRESHOLD && xDir > -1) {
                ++xNeg
                xDir = -1
            }
            if (dp.y > POSITIVE_COUNTER_THRESHOLD && yDir < 1) {
                ++yPos
                yDir = 1
            }
            if (dp.y < NEGATIVE_COUNTER_THRESHOLD && yDir > -1) {
                ++yNeg
                yDir = -1
            }
            if (dp.z > POSITIVE_COUNTER_THRESHOLD && zDir < 1) {
                ++zPos
                zDir = 1
            }
            if (dp.z < NEGATIVE_COUNTER_THRESHOLD && zDir > -1) {
                ++zNeg
                zDir = -1
            }
        }
        if (xPos >= MINIMUM_EACH_DIRECTION && xNeg >= MINIMUM_EACH_DIRECTION
            || yPos >= MINIMUM_EACH_DIRECTION && yNeg >= MINIMUM_EACH_DIRECTION
            || zPos >= MINIMUM_EACH_DIRECTION && zNeg >= MINIMUM_EACH_DIRECTION
        ) {
            lastShake = System.currentTimeMillis()
            lastX = 0f
            lastY = 0f
            lastZ = 0f
            dataPoints.clear()
            triggerShakeDetected()
        }
    }

    private fun triggerShakeDetected() {
        shakeListener?.onShakeDetected()
    }

    private inner class DataPoint(
        var x: Float,
        var y: Float,
        var z: Float,
        var atTimeMilliseconds: Long
    )

    interface ShakeListener {
        fun onShakeDetected()
    }

    companion object {
        private const val IGNORE_EVENTS_AFTER_SHAKE = 2000
        private const val SHAKE_CHECK_THRESHOLD = 200
        private const val KEEP_DATA_POINTS_FOR: Long = 1500
        private const val POSITIVE_COUNTER_THRESHOLD = 2.0.toFloat()
        private const val NEGATIVE_COUNTER_THRESHOLD = (-2.0).toFloat()
        private const val MINIMUM_EACH_DIRECTION = 3
    }
}