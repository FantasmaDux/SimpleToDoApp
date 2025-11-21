package com.fantasmadux.simpletodoapp.model

import java.time.Instant

data class ToDoModel(
    var task: String,
    var id: Int = 0,
    var status: Int = 0,
    var date: String? = null,      // формат "дд.мм.гггг"
    var time: String? = null       // формат "чч:мм"
) {
    companion object {
        fun createEmpty(): ToDoModel {
            return ToDoModel(task = "", status = 0)
        }
    }
}