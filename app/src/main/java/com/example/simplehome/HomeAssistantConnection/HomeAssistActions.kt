package com.example.simplehome.HomeAssistantConnection

import com.example.simplehome.models.*
import javax.net.ssl.SSLEngineResult

class HomeAssistActions(){

    val TAG = "HomeAssistActions"

    fun <T> sendMessage(data: T) {
        val message = JsonAdaptor().toJson(data)
        Log("", message)
        SocketManager.sendMessage(message)
    }

    fun sendMessage(data: String) {
        SocketManager.sendMessage(data)
    }

    fun sendScript(entiId: String) {
        Log("sending script", entiId)
        val message =
            baseEntiCall(
                id = SocketManager.messageId,
                domain = "script",
                service_data = service_data_base(
                    entiId
                )
            )
        sendMessage(message)
    }

    fun CallLightService(entity_id: String, brightness: Int){
        Log("Calling light service","")

        when (brightness){
            0 -> {
                val call = baseEntiCall(
                    id = SocketManager.messageId,
                    service = "turn_off",
                    domain = "light",
                    service_data = service_data_base(
                        entity_id
                ))
                sendMessage(call)

        }
            else -> {
                val call = lightCall(
                    id = SocketManager.messageId,
                    service_data = service_data_light(
                        entity_id,
                        brightness = brightness
                    ))
                sendMessage(call)

            }
        }
    }

    fun callMusicAction(entiId: String, action: MusicAction){
        Log("Calling music service", action.toString())

        var service : String = ""

        when (action){
            MusicAction.PreviousTrack -> service = "media_previous_track"
            MusicAction.NextTrack -> service = "media_next_track"
            MusicAction.Pause -> service = "media_pause"
            MusicAction.Play -> service = "media_play"
        }

        val call = baseEntiCall(
            id = SocketManager.messageId,
            domain = "media_player",
            service = service,
            service_data = service_data_base(entiId)
        )
        sendMessage(call)

    }

    enum class MusicAction {
        Play,
        Pause,
        PreviousTrack,
        NextTrack
    }



    fun CallMusicVolume(entiId: String, volume: Float){
        Log("Calling volume service","")

        val call = musicVolumeCall(
            id = SocketManager.messageId,
            service_data = service_data_music_volume(entiId, volume)
            )
        sendMessage(call)
    }

    fun Log(where: String, message: String) {
        android.util.Log.d(TAG, "sendMessage " + where + ": " + message)
    }

}
