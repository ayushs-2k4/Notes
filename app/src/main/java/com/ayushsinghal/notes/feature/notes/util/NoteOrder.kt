package com.ayushsinghal.notes.feature.notes.util

sealed class NoteOrder(val orderType: OrderType) {

    class Title(orderType: OrderType) : NoteOrder(orderType)
    class LastModifiedDate(orderType: OrderType) : NoteOrder(orderType)
    class CreatedDate(orderType: OrderType) : NoteOrder(orderType)
//    class Color(orderType: OrderType) : NoteOrder(orderType)
}