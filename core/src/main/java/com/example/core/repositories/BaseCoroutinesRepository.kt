package com.example.core.repositories

import com.example.core.models.BaseData

interface BaseCoroutinesRepository <T: BaseData> {
    fun get(): T
}