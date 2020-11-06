package com.example.simplehome.models

import com.google.gson.annotations.SerializedName

class AuthenticationCall (
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("type") val type : String
)

interface IbaseCall {
    val id: Int
    val type: String
}

interface IbaseSendMessage : IbaseCall {
    val domain: String
    val service: String
    val service_data: service_data_base
}

open class service_data_base (
    val entity_id:String
)

class service_data_light(
    entity_id: String = "light",
    val brightness: Int

): service_data_base(entity_id)

class baseCall(
    override val id: Int,
    override val type: String
):IbaseCall

open class baseEntiCall(
    override val id: Int,
    override val type: String = "call_service",
    override val domain: String,
    override val service: String = "turn_on",
    @SerializedName("service_data") override val service_data: service_data_base
) : IbaseCall,
    IbaseSendMessage


class lightCall(
    override val id: Int,
    override val type: String = "call_service",
    override val domain: String = "light",
    override var service: String = "turn_on",
    @SerializedName("service_data") override val service_data: service_data_light
): IbaseSendMessage