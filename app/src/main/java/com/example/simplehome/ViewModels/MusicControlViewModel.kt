package com.example.simplehome.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.simplehome.HomeAssistantConnection.HomeAssistActions
import com.example.simplehome.Repository.Entities
import com.example.simplehome.models.lightViewData
import com.example.simplehome.models.musicViewData
import com.example.simplehome.models.result


class MusicControlViewModel : ViewModel() {

    private val TAG = "MusicFragmentViewModel"

    private var activeEntity : String? = null

    init {
        getmMusic()
    }

    var MusicViewData: MutableLiveData<musicViewData>? = null
    fun getmMusic() : MutableLiveData<musicViewData> {
            Log.d(TAG, "getting music entity")
            if (activeEntity == null) {
                MusicViewData = Entities.musicEntity

            }
        return MusicViewData!!
        }


    fun Action (action : HomeAssistActions.MusicAction){
        MusicViewData?.value?.entity_id?.let { HomeAssistActions().callMusicAction(it, action) }
    }

    fun playPause(){
        when (MusicViewData?.value?.state){
            "playing" -> { Action(HomeAssistActions.MusicAction.Pause)
            }
                else -> { Action(HomeAssistActions.MusicAction.Play)
            }
        }
    }

    fun PreviousTrack(){
        Action(HomeAssistActions.MusicAction.PreviousTrack)
    }

    fun naxtTrack(){
        Action(HomeAssistActions.MusicAction.NextTrack)
    }

    fun unloadView() {
        activeEntity?.let { Entities.unloadView(it)
        }
    }


    var Volume : MutableLiveData<Float>? = MutableLiveData()
    fun onVolumeChange(volume: Float){
       //HomeAssistActions().CallMusicVolume(volume)
    }
    fun getVolume() {
        //TODO set value from socket

        val volume: Float = 20F
        val Mld : MutableLiveData<Float>? = null
         Mld?.postValue(volume)
        if (Mld != null) {
            Volume = Mld
        }
    }


}