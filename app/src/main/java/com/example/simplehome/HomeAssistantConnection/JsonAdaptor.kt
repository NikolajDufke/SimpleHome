package com.example.simplehome.HomeAssistantConnection

import android.util.Log
import com.example.simplehome.models.*
import com.google.gson.*
import com.google.gson.JsonParseException
import java.lang.reflect.Type


class JsonAdaptor(){

   fun <T> toJson(obj: T): String{
        val clazz  = obj
        val jsonString: String = Gson().toJson(clazz)

        return jsonString
    }
}




internal class ResponseDeserializer : JsonDeserializer<baseResponse?> {
    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): baseResponse? {

        var jsonResult : baseResponse =
            baseResponse()

        val jsonObject = json.asJsonObject

        jsonResult.id = jsonObject["id"].asInt
        jsonResult.success = jsonObject["success"].asBoolean
        jsonResult.type = jsonObject["type"].asString

        val type = jsonObject["result"]?.asJsonArray


        jsonResult.result = mutableListOf()


        type?.forEach{
            if (it != null) {

                val itJsonObject =  it.asJsonObject

                var itResult: result =
                    result(
                        entity_id = it.asJsonObject["entity_id"].asString,
                        state = it.asJsonObject["state"].asString,
                        last_changed = it.asJsonObject["last_changed"].asString,
                        last_updated = it.asJsonObject["last_updated"].asString,
                        context = Gson().fromJson(
                            itJsonObject["context"].asJsonObject,
                            Context::class.java

                        ),
                        participation = Participation(),
                        viewState = ViewState(),
                        resultObservers = arrayListOf()

                    )

                var attributes: Attributes? = null
                val domain = itResult.entity_id.split(".")[0]

                 when (domain){
                    "sun" -> {
                         attributes = fromJson<Attributes_sun>(itJsonObject)
                        Log.d("jsonds", "sun")
                    }
                    //return context.deserialize(jsonObject,RectangleShape::class.java)
                    "script" -> {
                        attributes = fromJson<Attributes_script>(itJsonObject)
                        Log.d("jsonds", "script")
                    }
                    //return context.deserialize(jsonObject,CircleShape::class.java)
                    "light" -> {
                        attributes = fromJson<Attributes_light>(itJsonObject)
                        Log.d("jsonds", "light")
                    }
                }
                itResult.attributes = attributes
                jsonResult.result!!.add(itResult)
            }}

        return jsonResult
    }

    inline fun <reified T> fromJson(jsonObject: JsonObject): T{
        return Gson().fromJson(jsonObject["attributes"].asJsonObject, T::class.java)
    }
}