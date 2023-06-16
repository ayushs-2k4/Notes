package com.ayushsinghal.notes.feature.notes.util

sealed class OrderType {
    object Ascending : OrderType()
    object Descending : OrderType()
}