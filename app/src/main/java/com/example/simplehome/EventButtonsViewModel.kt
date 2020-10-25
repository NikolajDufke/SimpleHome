package com.example.simplehome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EventButtonsViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    private val test: MutableLiveData<List<String>> by lazy {
        test.also {
        loadTest()
        }
    }

    fun getTest(): LiveData<List<String>>{
        return test
    }

    private fun loadTest(){

    }


}

