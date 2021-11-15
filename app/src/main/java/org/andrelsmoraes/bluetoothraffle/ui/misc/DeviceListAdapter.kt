package org.andrelsmoraes.bluetoothraffle.ui.misc

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.andrelsmoraes.bluetoothraffle.R
import org.andrelsmoraes.bluetoothraffle.databinding.ItemDeviceBinding
import org.andrelsmoraes.bluetoothraffle.domain.model.Device
import org.andrelsmoraes.bluetoothraffle.utils.formatToTime
import org.andrelsmoraes.bluetoothraffle.utils.visibleOrGone
import java.util.Date

class DeviceListAdapter(private val customTextColorRes: Int? = null) :
    RecyclerView.Adapter<DeviceListAdapter.DeviceViewHolder>() {

    private val items = mutableListOf<Device>()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val binding = ItemDeviceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DeviceViewHolder(binding, customTextColorRes)
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
        private val binding: ItemDeviceBinding,
        private val customTextColorRes: Int? = null
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(value: Device) {
            binding.apply {
                customTextColorRes?.also {
                    textName.setTextColor(
                        ContextCompat.getColor(itemView.context, customTextColorRes))
                    textAddress.setTextColor(
                        ContextCompat.getColor(itemView.context, customTextColorRes))
                    textRaffleTime.setTextColor(
                        ContextCompat.getColor(itemView.context, customTextColorRes))
                }

                textName.text = value.name
                textAddress.text = value.address
                textRaffleTime.visibleOrGone(value.isRaffled())

                if (value.isRaffled()) {
                    textRaffleTime.text = itemView.context.getString(
                        R.string.text_raffled_at,
                        Date(value.raffledTime ?: 0).formatToTime(itemView.context)
                    )
                }
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

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any {
            return newItems[newItemPosition]
        }
    }

}