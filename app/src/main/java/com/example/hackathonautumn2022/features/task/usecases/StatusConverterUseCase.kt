package com.example.hackathonautumn2022.features.task.usecases

import com.example.hackathonautumn2022.R

object StatusConverter {

    fun getCodeByStatus(id: Int) = when(id) {
        R.string.not_started -> "0"
        R.string.in_progress -> "1"
        R.string.finished -> "2"
        R.string.failed -> "3"
        else -> "0"
    }

    fun getStatusByCode(code: String?) = when (code) {
        "0" -> R.string.not_started
        "1" -> R.string.in_progress
        "2" -> R.string.finished
        "3" -> R.string.failed
        else -> R.string.not_started
    }

}