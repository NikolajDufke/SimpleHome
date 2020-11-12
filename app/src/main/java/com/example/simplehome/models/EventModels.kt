package com.example.simplehome.models

import com.google.gson.annotations.SerializedName

data class baseEvent (

    @SerializedName("id") var id : Int = -1,
    @SerializedName("type") var type : String? = null,
    @SerializedName("event") var event : Event? = null
)

data class Event (

    @SerializedName("event_type") val event_type : String,
    @SerializedName("data") var data : baseData? = null,
    @SerializedName("origin") val origin : String,
    @SerializedName("time_fired") val time_fired : String,
    @SerializedName("context") val context : Context
)

interface baseData{

}

data class Data_onChange (

    @SerializedName("entity_id") val entity_id : String,
    @SerializedName("old_state") val old_state : result? = null,
    @SerializedName("new_state") val new_state : result?= null
) : baseData

data class Data_event (

    @SerializedName("domain") val domain : String,
    @SerializedName("service") val service : String,
    @SerializedName("service_data") val service_data : IbaseSendMessage
): baseData