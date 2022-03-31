package ru.punkoff.vksubscribeapp.main.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.generic.RoundingParams
import ru.punkoff.vksubscribeapp.R
import ru.punkoff.vksubscribeapp.databinding.ItemCommunityBinding
import ru.punkoff.vksubscribeapp.model.Subscription

val COMMUNITIES_COMPARATOR = object : DiffUtil.ItemCallback<Subscription>() {
    override fun areItemsTheSame(oldItem: Subscription, newItem: Subscription): Boolean =
        oldItem.userId == newItem.userId

    override fun areContentsTheSame(oldItem: Subscription, newItem: Subscription): Boolean =
        oldItem == newItem
}

class CommunitiesAdapter :
    ListAdapter<Subscription, CommunitiesAdapter.CommunitiesViewHolder>(COMMUNITIES_COMPARATOR) {

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

        private val borderColor = binding.root.resources.getColor(R.color.border_item_color, null)
        fun bind(currentItem: Subscription) {
            with(binding) {
                name.text = currentItem.name
                photo.setImageURI(currentItem.imageUri)

                photo.hierarchy.roundingParams = getImageStyle(currentItem)

                Log.e(javaClass.simpleName, "${currentItem.name} - ${currentItem.isSelected}")
                photo.setOnClickListener {
                    currentItem.isSelected = !currentItem.isSelected
                    photo.hierarchy.roundingParams = getImageStyle(currentItem)
                    listener.onClick(currentItem)
                }
            }
        }

        private fun getImageStyle(currentItem: Subscription): RoundingParams {
            val roundingParams = RoundingParams()
            roundingParams.roundAsCircle = true
            if (currentItem.isSelected) {
                roundingParams.setBorder(borderColor, 5.0f)
            }
            return roundingParams
        }
    }
}
