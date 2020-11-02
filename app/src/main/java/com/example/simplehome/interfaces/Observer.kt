package com.example.simplehome.interfaces

import com.example.simplehome.models.result

interface IResultObserver {
    fun update(enti: result)
}

interface IObservable {
    val resultObservers: ArrayList<IResultObserver>

    fun add(resultObserver: IResultObserver) {
        resultObservers.add(resultObserver)
    }

    fun remove(resultObserver: IResultObserver) {
        resultObservers.remove(resultObserver)
    }

    fun sendUpdateEvent(enti: result) {
        resultObservers.forEach { it.update(enti) }
    }
}