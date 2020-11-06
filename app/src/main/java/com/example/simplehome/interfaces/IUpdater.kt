package com.example.simplehome.interfaces

import com.example.simplehome.models.IAttributes

interface IAttributeUpdater {
    fun <T : IAttributes> updateObject(Object : T)
}