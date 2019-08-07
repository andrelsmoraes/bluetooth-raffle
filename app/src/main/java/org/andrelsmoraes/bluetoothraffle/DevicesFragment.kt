package org.andrelsmoraes.bluetoothraffle

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment

class DevicesFragment : Fragment() {

    companion object {
        fun newInstance(): DevicesFragment {
            return DevicesFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_devices, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.devices_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

}