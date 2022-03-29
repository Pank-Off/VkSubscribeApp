package ru.punkoff.vksubscribeapp.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vk.sdk.api.groups.dto.GroupsGroupFull
import ru.punkoff.vksubscribeapp.databinding.ItemCommunityBinding

val COMMUNITIES_COMPARATOR = object : DiffUtil.ItemCallback<GroupsGroupFull>() {
    override fun areItemsTheSame(oldItem: GroupsGroupFull, newItem: GroupsGroupFull): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: GroupsGroupFull, newItem: GroupsGroupFull): Boolean =
        oldItem == newItem
}

class CommunitiesAdapter :
    ListAdapter<GroupsGroupFull, CommunitiesAdapter.CommunitiesViewHolder>(COMMUNITIES_COMPARATOR) {

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
            }
        }
    }
}
