package org.andrelsmoraes.bluetoothraffle.ui.misc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_device.view.*
import org.andrelsmoraes.bluetoothraffle.R
import org.andrelsmoraes.bluetoothraffle.domain.model.Device
import org.andrelsmoraes.bluetoothraffle.utils.formatToTime
import org.andrelsmoraes.bluetoothraffle.utils.visibleOrGone
import java.util.*

class DeviceListAdapter(private val customTextColorRes: Int? = null) :
    RecyclerView.Adapter<DeviceListAdapter.DeviceViewHolder>() {

    private val items = mutableListOf<Device>()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_device, parent, false)
        return DeviceViewHolder(itemView, customTextColorRes)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    override fun getItemId(position: Int): Long {
        return items[position].address.hashCode().toLong()
    }

    fun setItems(newItems: List<Device>) {
        val result = DiffUtil.calculateDiff(DeviceDiffCallback(this.items, newItems))
        result.dispatchUpdatesTo(this)
        this.items.clear()
        this.items.addAll(newItems)
    }

    class DeviceViewHolder(
        itemView: View,
        private val customTextColorRes: Int? = null
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(value: Device) {
            customTextColorRes?.also {
                itemView.textName.setTextColor(
                    ContextCompat.getColor(itemView.context, customTextColorRes))
                itemView.textAddress.setTextColor(
                    ContextCompat.getColor(itemView.context, customTextColorRes))
                itemView.textRaffleTime.setTextColor(
                    ContextCompat.getColor(itemView.context, customTextColorRes))
            }

            itemView.textName.text = value.name
            itemView.textAddress.text = value.address
            itemView.textRaffleTime.visibleOrGone(value.isRaffled())

            if (value.isRaffled()) {
                itemView.textRaffleTime.text = itemView.context.getString(
                    R.string.text_raffled_at,
                    Date(value.raffledTime ?: 0).formatToTime(itemView.context)
                )
            }
        }

    }

    class DeviceDiffCallback(
        private var oldItems: List<Device>,
        private var newItems: List<Device>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition].address == newItems[newItemPosition].address

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition] == newItems[newItemPosition]

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            return newItems[newItemPosition]
        }
    }

}