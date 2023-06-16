package com.ayushsinghal.notes.feature.notes.util

sealed class NoteOrder(val orderType: OrderType) {

    class Title(orderType: OrderType) : NoteOrder(orderType)
    class LastModifiedDate(orderType: OrderType) : NoteOrder(orderType)
    class CreatedDate(orderType: OrderType) : NoteOrder(orderType)
//    class Color(orderType: OrderType) : NoteOrder(orderType)

    fun copy(orderType: OrderType): NoteOrder {
        return when (this) {

            is Title -> {
                NoteOrder.Title(orderType = orderType)
            }
            
            is LastModifiedDate -> {
                NoteOrder.LastModifiedDate(orderType = orderType)
            }

            is CreatedDate -> {
                NoteOrder.CreatedDate(orderType = orderType)
            }
        }
    }
}