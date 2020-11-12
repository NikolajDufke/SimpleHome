package com.example.simplehome.HomeAssistantConnection

import com.example.simplehome.models.baseEvent
import com.example.simplehome.models.baseResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class GsonFactory{
    companion object{
   fun GetGsonResponse(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(baseResponse::class.java, ResponseDeserializer())
            .create()
    }

    fun GetGsonEvent(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(baseEvent::class.java, EventDeserializer())
            .create()
        }
    }
}