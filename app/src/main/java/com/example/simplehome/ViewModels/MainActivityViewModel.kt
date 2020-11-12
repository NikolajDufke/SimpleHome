package com.example.simplehome.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.simplehome.HomeAssistantConnection.HomeAssistActions
import com.example.simplehome.Repository.Entities
import com.example.simplehome.Repository.Entities.buttonEvents
import com.example.simplehome.models.baseViewData
import com.example.simplehome.models.lightViewData
import com.example.simplehome.models.scriptViewData

class MainActivityViewModel : ViewModel(){

    private val TAG = "MainActivityViewModel"
    init {
        getHorizontalButtons()
        getLightSliders()
    }

    var lightSliders: MutableLiveData<List<lightViewData>>? = null
    fun getLightSliders(): LiveData<List<lightViewData>> {
        Log.d(TAG, "getting sliders")
        if (lightSliders == null) {
            lightSliders = Entities.lightSliders
            Log.d(TAG, "horizontal buttons" + lightSliders)
        }
        return lightSliders!!
    }

    var horizontalButtons: MutableLiveData<List<scriptViewData>>? = null
    fun getHorizontalButtons(): LiveData<List<scriptViewData>> {
        Log.d(TAG, "getting horizontal buttons")
        if (horizontalButtons == null) {
            horizontalButtons = buttonEvents
            Log.d(TAG, "horizontal buttons" + horizontalButtons)
        }
        return horizontalButtons!!
    }


    fun SliderLightValueChange(entity_id: String, newLightValue: Float){
        Entities.SliderLightValueChange(entity_id,newLightValue)
    }

    fun RunScript(entiId : String){
        HomeAssistActions().sendScript(entiId)
    }

    fun ViewLoaded(entityId :String, ViewID : Int){
        Entities.viewIsLoaded(entityId, ViewID)
    }

    var toastMessage = MutableLiveData<String>()
    fun setMessage(message: String){
        toastMessage.value = message
    }




}