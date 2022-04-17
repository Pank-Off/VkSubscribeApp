package ru.punkoff.vksubscribeapp.main.adapter

import ru.punkoff.vksubscribeapp.data.model.Subscription

interface OnItemClickListener {
    fun onClick(subscription: Subscription)
    fun onLongClick(subscription: Subscription)
}
