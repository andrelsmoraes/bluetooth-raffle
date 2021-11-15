package org.andrelsmoraes.bluetoothraffle.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.google.android.material.transition.platform.MaterialArcMotion
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import org.andrelsmoraes.bluetoothraffle.R
import org.andrelsmoraes.bluetoothraffle.databinding.ActivityNewRaffledBinding
import org.andrelsmoraes.bluetoothraffle.domain.model.Device
import org.andrelsmoraes.bluetoothraffle.ui.misc.DeviceListAdapter
import org.andrelsmoraes.bluetoothraffle.utils.viewBinding

class NewRaffledActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityNewRaffledBinding::inflate)

    private val raffledDeviceListAdapter = DeviceListAdapter(R.color.colorTextDarkBackground)

    override fun onCreate(savedInstanceState: Bundle?) {
        configureTransition()

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.recyclerRaffled.adapter = raffledDeviceListAdapter

        configureRaffledData()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun configureTransition() {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        ViewCompat.setTransitionName(
            findViewById(android.R.id.content),
            DevicesFragment.TRANSITION_TO_RAFFLED_NAME
        )
        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())

        val materialTransform = MaterialContainerTransform().apply {
            addTarget(android.R.id.content)
            pathMotion = MaterialArcMotion()
            duration = TRANSITION_DURATION_MILLIS
            interpolator = FastOutSlowInInterpolator()
        }
        window.sharedElementEnterTransition = materialTransform
    }

    private fun configureRaffledData() {
        val raffledDevices =
            intent.getParcelableArrayListExtra<Device>(EXTRA_RAFFLED_DEVICES)?.toList()
                ?: emptyList()

        supportActionBar?.title = resources.getQuantityString(
            R.plurals.new_raffled_devices,
            raffledDevices.size,
            raffledDevices.size
        )

        raffledDeviceListAdapter.setItems(raffledDevices)
    }

    companion object {
        private const val EXTRA_RAFFLED_DEVICES = "extraRaffledDevices"
        private const val TRANSITION_DURATION_MILLIS = 500L

        fun createIntent(context: Context, raffledDevices: List<Device>) =
            Intent(context, NewRaffledActivity::class.java)
                .putParcelableArrayListExtra(EXTRA_RAFFLED_DEVICES, ArrayList(raffledDevices))
    }

}
