package ru.punkoff.vksubscribeapp.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.generic.RoundingParams
import com.vk.sdk.api.groups.dto.GroupsGroupFull
import ru.punkoff.vksubscribeapp.R
import ru.punkoff.vksubscribeapp.databinding.ItemCommunityBinding

val COMMUNITIES_COMPARATOR = object : DiffUtil.ItemCallback<GroupsGroupFull>() {
    override fun areItemsTheSame(oldItem: GroupsGroupFull, newItem: GroupsGroupFull): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: GroupsGroupFull, newItem: GroupsGroupFull): Boolean =
        oldItem == newItem
}

class CommunitiesAdapter :
    ListAdapter<GroupsGroupFull, CommunitiesAdapter.CommunitiesViewHolder>(COMMUNITIES_COMPARATOR) {

    private lateinit var listener: OnItemClickListener
    fun attachListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunitiesViewHolder {
        return CommunitiesViewHolder(parent)
    }

    override fun onBindViewHolder(holder: CommunitiesViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class CommunitiesViewHolder(
        parent: ViewGroup,
        private val binding: ItemCommunityBinding = ItemCommunityBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(currentItem: GroupsGroupFull) {
            with(binding) {
                name.text = currentItem.name
                photo.setImageURI(currentItem.photo100)
                photo.setOnClickListener {
                    photo.isSelected = !photo.isSelected
                    val roundingParams = RoundingParams()
                    roundingParams.roundAsCircle = true

                    if (photo.isSelected) {
                        val borderColor = it.resources.getColor(R.color.border_item_color, null)
                        roundingParams.setBorder(borderColor, 5.0f)
                    }
                    photo.hierarchy.roundingParams = roundingParams

                    listener.onClick(photo.isSelected)
                }
            }
        }
    }
}
