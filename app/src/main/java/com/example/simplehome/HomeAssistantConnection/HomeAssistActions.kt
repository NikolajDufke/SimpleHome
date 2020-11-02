package com.example.simplehome.HomeAssistantConnection

import com.example.simplehome.models.*

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

    fun sendScript(script: String) {
        Log("sending script", script)
        val message =
            baseEntiCall(
                id = SocketManager.messageId,
                service_data = service_data_base(
                    script
                )
            )
        sendMessage(message)
    }

    fun CallLightService(enti: result){
        Log("Calling light service","")

        val attr = enti.attributes as Attributes_light
        var call = lightCall(
            id = SocketManager.messageId,
            service_data = service_data_light(
                enti.entity_id,
                brightness = attr.brightness
            )
        )

        if(attr.brightness == 0)
        {
            call.service = "turn_off"
        }

        sendMessage(call)
    }


/*
    fun CallLightService(brightness: number, entityId: string){
            if (brightness > 0) {
                SendMessage(
                    {
                        "id": id,
                        "type": "call_service",
                        "domain": "light",
                        "service": "turn_on",
                        "service_data": {
                        "entity_id": entityId,
                        "brightness": brightness,
                    }
                    }
                )
            }
            if (brightness == 0) {
                console.log(" light off")
                SendMessage(
                    {
                        "id": id,
                        "type": "call_service",
                        "domain": "light",
                        "service": "turn_off",
                        "service_data": {
                        "entity_id": entityId,
                    }
                    }
                )
            }
        }


    }
*/
    fun Log(where: String, message: String) {
        android.util.Log.d(TAG, "sendMessage " + where + ": " + message)
    }

}
