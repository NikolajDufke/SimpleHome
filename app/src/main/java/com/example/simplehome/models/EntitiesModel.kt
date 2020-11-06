package com.example.simplehome.models

import android.graphics.drawable.Drawable
import com.example.simplehome.R
import com.google.gson.annotations.SerializedName
import java.util.*
import com.example.simplehome.models.IbaseViewData as IbaseViewData1

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
}

interface ISensorBatteryLevel : EntityBase {
    val  attributes: ISensorBatteryLevelAttributes
    val icon: String
}


 class baseResponse (
     var id : Int = -1,
     var type : String? = null,
     var success : Boolean = false,
     @SerializedName("result") var result : MutableList<result>? = mutableListOf()
 )


class Context (
    @SerializedName("id") val id : String,
    @SerializedName("parent_id") val parent_id : String,
    @SerializedName("user_id") val user_id : String
)

data  class result (
    val entity_id: String,
    val state: String,
    val last_changed: String,
    val last_updated: String,
    val context: Context? ,
    val attributes : IAttributes
    ) :  Comparable<result?> {

    fun getDomain() :String {
        return entity_id.substringBefore(".")
    }

    override fun compareTo(other: result?) = compareValuesBy(this, other,
        {
            val cState: Int = this.state.compareTo(other?.state!!)
            val cAttribute: Int = this.attributes.compareTo(other?.attributes!!)
            return cState.compareTo(cAttribute)
        })
    }


/*
data  class result (
    var entity_id: String,
    var state: String,
    var last_changed: String,
    var last_updated: String,
    var context: Context? = null,
    var attributes : IAttributes? = null,
    var participation: Participation = Participation(),
    var viewState : ViewState = ViewState(),
    override val resultObservers: ArrayList<IResultObserver> = arrayListOf()
) : IObservable, IResultObserver, Comparable<result?> {

    fun getEntiId(): String {
        return entity_id.substringAfter(".")
    }

    fun getDomain() :String {
        return entity_id.substringBefore(".")
    }

    override fun update(enti: result) {
        when (getDomain()){
            "light" -> {
                (attributes as Attributes_light).brightness = (enti.attributes as Attributes_light).brightness
            }
        }
    }

    override fun compareTo(other: result?) = compareValuesBy(this, other,
        { it?.state
        }
    )

    fun <T: IbaseViewData> getViewDataModel(): T{

        var base = baseViewData(
            entity_id = this.entity_id,
            state = this.state,
            isLoaded = this.viewState.isLoaded,
            viewId = this.viewState.viewId
        )

        when(getDomain()) {
            "light" -> {
                val lightModel : lightViewData = base as lightViewData
                lightModel.brightness = (this.attributes as Attributes_light).brightnessInProcent
                return lightModel as T
                }
            }
        return base as T
        }
}*/


enum class participation{
    SCRIPTBUTTONS,
    LIGHTSLIDERS,
    MUSIC
}

class ViewState{
    var isLoaded : Boolean = false
    var viewId : Int? = null
}



interface IAttributes : Comparable<Any>{
    val friendly_name: String
}


class Attributes (override val friendly_name: String):IAttributes {
    override fun compareTo(other: Any): Int {
        return -1
    }
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
): IAttributes{
   override fun compareTo(other: kotlin.Any): kotlin.Int {
        return -1
    }


}

class Attributes_script(
    var last_triggered: String,
    val icon: String,
    override val friendly_name: String
): IAttributes {
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
    ): IAttributes{
        var brightnessInProcent: Float = 0.0f
            get() = (100f/255f) * brightness.toFloat()
    var setbrightnessFromProcent: Float = 0.0f
        set(value) {
           val newBrightness = (255f/100f) * value
           this.brightness = newBrightness.toInt()
        }
        override fun compareTo(other: Any): Int {
            if(other is Attributes_light){
                when {
                    brightness == other.brightness -> return 0
                }

            }
            return -1
        }

   /* override fun <T : IAttributes> updateObject(Object: T) {
        if(Object is Attributes_light){
            brightness = Object.brightness
        }*/
}

class EntityContainer(
    val enti: result,
    val participation: MutableList<participation> = mutableListOf(),
    val viewState: ViewState = ViewState(),
    var icon : Int = R.drawable.script
    ){
    fun <T: IbaseViewData1> getViewDataModel(): T{

        var base = baseViewData(
            entity_id = enti.entity_id,
            state = enti.state,
            isLoaded = viewState.isLoaded,
            viewId = viewState.viewId,
            friendly_name = enti.attributes.friendly_name
        )

        when(enti.getDomain()) {
            "light" -> {
                val lightModel : lightViewData = lightViewData(base, (enti.attributes as Attributes_light).brightnessInProcent)
                return lightModel as T
            }
            "script" -> {

                val scriptModel : scriptViewData = scriptViewData(base, icon)
                return scriptModel as T
            }
        }
        return base as T
    }


}











