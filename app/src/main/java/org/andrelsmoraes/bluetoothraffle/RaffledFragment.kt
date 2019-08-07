package org.andrelsmoraes.bluetoothraffle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class RaffledFragment : Fragment() {

    companion object {
        fun newInstance(): RaffledFragment {
            return RaffledFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_raffled, container, false)
    }

}