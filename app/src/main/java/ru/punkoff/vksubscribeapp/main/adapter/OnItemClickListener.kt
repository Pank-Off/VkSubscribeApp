package ru.punkoff.vksubscribeapp.main.adapter

import ru.punkoff.vksubscribeapp.model.Subscription

interface OnItemClickListener {
    fun onClick(subscription: Subscription)
}