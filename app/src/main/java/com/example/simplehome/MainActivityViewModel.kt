package com.example.simplehome

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.simplehome.models.result
import com.example.simplehome.Repository.Entities.Companion.buttonEvents
import com.example.simplehome.Repository.Entities.Companion.lightSliders_data

class MainActivityViewModel : ViewModel(){

    private val TAG = "MainActivityViewModel"
    init {
        getHorizontalButtons()
        getLightSliders()
    }

    var lightSliders: MutableLiveData<List<result>>? = null
    fun getLightSliders(): LiveData<List<result>> {
        Log.d(TAG, "getting sliders")
        if (lightSliders == null) {
            lightSliders = lightSliders_data
            Log.d(TAG, "horizontal buttons" + lightSliders)
        }
        return lightSliders!!
    }

    var horizontalButtons: MutableLiveData<List<result>>? = null
    fun getHorizontalButtons(): LiveData<List<result>> {
        Log.d(TAG, "getting horizontal buttons")
        if (horizontalButtons == null) {
            horizontalButtons = buttonEvents
            Log.d(TAG, "horizontal buttons" + horizontalButtons)
        }
        return horizontalButtons!!
    }


    fun SliderLightValueChange(){
        //HomeAssistActions().CallLightService()
    }

    var toastMessage = MutableLiveData<String>()
    fun setMessage(message: String){
        toastMessage.value = message
    }




}