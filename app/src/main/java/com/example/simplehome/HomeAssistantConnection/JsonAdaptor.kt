package com.example.simplehome.HomeAssistantConnection

import android.util.Log
import com.example.simplehome.models.*
import com.google.gson.*
import com.google.gson.JsonParseException
import java.lang.reflect.Array
import java.lang.reflect.Type


class JsonAdaptor(){

   fun <T> toJson(obj: T): String{
        val clazz  = obj
        val jsonString: String = Gson().toJson(clazz)

        return jsonString
    }
}

internal class ResponseDeserializer : JsonDeserializer<baseResponse?> {
    private val TAG: String = "ResponseDeserializer"

    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): baseResponse? {

        var jsonResult: baseResponse =
            baseResponse()

        val jsonObject = json.asJsonObject

        jsonResult.id = jsonObject["id"].asInt
        jsonResult.success = jsonObject["success"].asBoolean
        jsonResult.type = jsonObject["type"].asString

        var resultArray: JsonArray? = JsonArray()
        try {
            resultArray = jsonObject["result"]?.asJsonArray
            resultArray?.forEach {
                if (it != null) {
                    val itJsonObject = it.asJsonObject
                    jsonResult.result!!.add(getResult(itJsonObject))
                }
            }
        }
        catch(e: Exception){
            Log.d(TAG, "no result in response")
            resultArray = JsonArray()
        }
            return jsonResult
        }
    }



    internal class EventDeserializer : JsonDeserializer<baseEvent?> {
        @Throws(JsonParseException::class)
        override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
        ): baseEvent? {

            val rootJsonObject = json.asJsonObject
            val eventJsonObject = rootJsonObject["event"].asJsonObject
            val dataObject =  eventJsonObject["data"].asJsonObject

            val jsonResult : baseEvent =
                baseEvent(
                    id = rootJsonObject["id"].asInt,
                    type =  rootJsonObject["type"].asString,
                    event = Event(
                        event_type =  eventJsonObject["event_type"].asString,
                        origin =  eventJsonObject["origin"].asString,
                        time_fired = eventJsonObject["time_fired"].asString,
                        context = Gson().fromJson(
                            eventJsonObject["context"].asJsonObject,
                            Context::class.java)

                        )
                    )



            when(rootJsonObject["type"].asString){
                 "onChange"  -> {
                     jsonResult.event?.data = Data(
                         entity_id = dataObject["entity_id"].asString,
                         old_state = getResult(dataObject["old_state"].asJsonObject),
                         new_state = getResult(dataObject["new_state"].asJsonObject))
                     return jsonResult
            }
        }

            return jsonResult
        }
    }

    fun getResult(resultJson:JsonObject): result {

        val entity_id= resultJson["entity_id"].asString
        if(entity_id == null)
        {
            val t = entity_id
        }
        return result(
            entity_id = entity_id,
            state = resultJson["state"].asString,
            last_changed = resultJson["last_changed"].asString,
            last_updated = resultJson["last_updated"].asString,
            context = Gson().fromJson(
                resultJson["context"].asJsonObject,
                Context::class.java),
            attributes = getAttributesFormJson(entity_id.substringBefore("."),resultJson["attributes"].asJsonObject)
        )
    }

    internal fun <T: IAttributes> getAttributesFormJson(domain: String, AttributeJsonObject: JsonObject ) :T{
        when (domain){
            "sun" -> {
                Log.d("jsonds", "sun")
                return fromJson<Attributes_sun>(AttributeJsonObject) as T }
            "script" -> {
                Log.d("jsonds", "script")
                return fromJson<Attributes_script>(AttributeJsonObject) as T }
            "light" -> {
                Log.d("jsonds", "light")
                return fromJson<Attributes_light>(AttributeJsonObject) as T }
            else -> {
                //val t = Gson().fromJson<Attributes>(AttributeJsonObject, Attributes::class.java)

                return fromJson<Attributes>(AttributeJsonObject) as T }
        }
    }

    inline fun <reified T> fromJson(jsonObject: JsonObject): T{
        return Gson().fromJson(jsonObject, T::class.java) }



