package com.example.simplehome.models

import com.example.simplehome.interfaces.IObservable
import com.example.simplehome.interfaces.IResultObserver
import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.coroutineContext
import kotlin.reflect.typeOf

interface haBase {
    val type: String
}
interface ISensorBatteryLevelAttributes {
    val charger_type: String;
    val device_class: String;
    val friendly_name: String;
    val icon: String;
    val is_charging: Boolean
    val unit_of_measurement: String;
}

interface EntityBase{
    val context: Context;
    val entity_id: String;
    val last_changed: Date;
    val last_updated: Date;
    val state: String;
    val participation: Participation;
}

interface ISensorBatteryLevel : EntityBase {
    val  attributes: ISensorBatteryLevelAttributes
    val icon: String
}


 class baseResponse (
     var id : Int = -1,
     var type : String? = null,
     var success : Boolean = false,
     @SerializedName("result") var result : MutableList<result>? = null
 )


class Context (
    @SerializedName("id") val id : String,
    @SerializedName("parent_id") val parent_id : String,
    @SerializedName("user_id") val user_id : String
)

data  class result (
    var entity_id: String,
    var state: String,
    var last_changed: String,
    var last_updated: String,
    var context: Context? = null,
    var attributes : Attributes? = null,
    var participation: Participation,
    var viewState : ViewState,
    override val resultObservers: ArrayList<IResultObserver>
) : IObservable, IResultObserver, Comparable<result?> {

    fun getEntiId(): String {
        return entity_id.substringAfter(".")
    }

    fun getDomain() :String {
        return entity_id.substringBefore(".")
    }

    override fun update(enti: result) {

    }

    override fun compareTo(other: result?) = compareValuesBy(this, other,
        { it?.state }
    )

}

class Participation{
    var slides:Boolean = false
    var music:Boolean = false
}

class ViewState{
    var isLoaded : Boolean = false
    var viewId : String? = null
}


interface Attributes : Comparable<Any>{
    val friendly_name: String
}

class Attributes_sun (
    @SerializedName("next_dawn") var next_dawn : String,
    @SerializedName("next_dusk") var next_dusk : String,
    @SerializedName("next_midnight") var next_midnight : String,
    @SerializedName("next_noon") var next_noon : String,
    @SerializedName("next_rising") var next_rising : String,
    @SerializedName("next_setting") var next_setting : String,
    @SerializedName("elevation") var elevation : Double,
    @SerializedName("azimuth") var azimuth : Double,
    @SerializedName("rising") var rising : Boolean,
    override val friendly_name: String
): Attributes{
   override fun compareTo(other: kotlin.Any): kotlin.Int {
        TODO("Not yet implemented")
    }
}


class Attributes_script(
    var last_triggered: String,
    override val friendly_name: String
): Attributes {
    override fun compareTo(other: Any): Int {
        if(other is Attributes_script){
            when {
                last_triggered == other.last_triggered -> return 0
            }
        }
        return -1
    }
}



class Attributes_light(
    var brightness: Int,
    var effect: String,
    var effect_list: List<String>,
    var hs_color: List<Int>,
    var max_mireds: Int,
    var min_mireds: Int,
    var rgb_color: List<Int>,
    var supported_features: Int,
    var xy_color: List<Int>,
    override var friendly_name: String
    ): Attributes{
        override fun compareTo(other: Any): Int {
            if(other is Attributes_light){
                when {
                    brightness == other.brightness -> return 0
                }

            }
            return -1
        }
    }







