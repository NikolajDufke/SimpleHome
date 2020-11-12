package com.example.simplehome.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.simplehome.HomeAssistantConnection.HomeAssistActions
import com.example.simplehome.R
import com.example.simplehome.models.*
import com.example.simplehome.models.participation.*
import kotlin.reflect.typeOf

object Entities {

        private val TAG = "Entities"

        //Data collections
        var buttonEvents: MutableLiveData<List<scriptViewData>> = MutableLiveData()
        var lightSliders: MutableLiveData<List<lightViewData>> = MutableLiveData()
        var musicEntity: MutableLiveData<musicViewData> = MutableLiveData()
        private var EntitiesData: MutableMap<String, EntityContainer> = mutableMapOf()

        private val drawleIconMap = mapOf(
            "script.1585514325951" to R.drawable.sleep,
            "script.1585515702737" to R.drawable.weather_night,
            "script.1586409097105" to R.drawable.lightbulb_group_off)

        private var buttonEvents_has_changed = false
        private var lightSliders_has_changed = false
        private var music_has_changed = false
        private var buttonEvents_local : MutableList<scriptViewData> = mutableListOf()
        private var lightSliders_local : MutableList<lightViewData> = mutableListOf()

        //Hardcoded Entities
        val scripts: List<String> = listOf(
            "script.1585514325951",
            "script.1585515702737",
            "script.1586409097105"
        )
        val firstFlooreEntities: List<String> = listOf(
            "light.hue_go_1",
            "light.repo",
            "media_player.borne_vaerelse",
            "light.ellas_seng",
            "light.johanna_s_seng",
            "sensor.motorola_one_vision_battery_level_2"
        )
        val firstfloorSlidersSelection: List<String> = listOf(
            "light.hue_go_1",
            "light.repo"
        )

        val firstfloormusic: List<String> = listOf(
            "media_player.borne_vaerelse"
        )

        fun onChange(){
            if(lightSliders_has_changed){
                lightSliders.postValue(lightSliders_local)
                lightSliders_has_changed = false
            }
            if(buttonEvents_has_changed){
                buttonEvents.postValue(buttonEvents_local)
                buttonEvents_has_changed = false
            }
            if(music_has_changed){
                EntitiesData["media_player.borne_vaerelse"]?.let {  musicEntity.postValue(it.getViewDataModel()) }
               }
        }

        fun onEntityCreate(entity: result) {
            val ec = EntityContainer(entity)
            if(firstfloorSlidersSelection.contains(entity.entity_id))
            {
                ec.participation.add(LIGHTSLIDERS)
                val dm : lightViewData= ec.getViewDataModel()
                onAddWaitForUpdateEvent<lightViewData>(dm,ec.participation)
                lightSliders_has_changed = true
            }
            if(scripts.contains(entity.entity_id))
            {
                ec.participation.add(SCRIPTBUTTONS)
                drawleIconMap[ec.enti.entity_id]?.let {
                    ec.entityInfo = scriptInfo(it)
                }
                onAddWaitForUpdateEvent<scriptViewData>(ec.getViewDataModel(),ec.participation)
            }
            if(firstfloormusic.contains(entity.entity_id)){
                ec.participation.add(MUSIC)
                onAddWaitForUpdateEvent<musicViewData>(ec.getViewDataModel(), ec.participation)
            }
            EntitiesData.put(entity.entity_id, ec)
            Log.d(TAG, "creating entity " + entity.entity_id)
        }

        fun onEntityGetAll(entities: MutableList<result>?) {
            entities?.forEach { res ->
                if (EntitiesData.containsKey(res.entity_id)) {
                    //onEntityUpdate(res)
                }
                else {
                    onEntityCreate(res)}
            }
            onChange()
        }

    fun onEntityUpdate(event: baseEvent) {
        val entiId = "result.entity_id"
        val b= event

        when(b.event?.event_type){
            "state_changed" -> {
                val d = b.event?.data as Data_onChange

                EntitiesData[d.entity_id]?.enti = d.new_state!!
                when (d.entity_id.substringBefore(".")){
                    "light" -> {
                        lightSliders_has_changed = true
                    }
                    "media_player" -> {
                        music_has_changed = true
                    }
                }

                onChange()
            }

        }


      /*      if (it.enti != event) {
                if (scripts.contains(entiId)) {

                }
            }*/


    }

/*        fun onEntityUpdate(result: result) {
            val entiId = result.entity_id
            EntitiesData[entiId]?.let {
                if (it.enti != result) {
                    if (scripts.contains(entiId)) {
//TODO add what happens
                    }
                }
            }
            onChange()
        }*/

        fun SliderLightValueChange(entity_id: String, newLightValue: Float){

            EntitiesData[entity_id]?.let {
                if(it.enti.getDomain() == "light"){
                    (it.enti.attributes as Attributes_light).setbrightnessFromProcent = newLightValue
                    val b=  (it.enti.attributes as Attributes_light).brightness
                    val entiId = it.enti.entity_id
                    HomeAssistActions().CallLightService(entiId,b)
                }
            }
        }

        fun viewIsLoaded(entityId :String, ViewID : Int? = null){
            EntitiesData[entityId]?.let {
                it.viewState.isLoaded = true
                it.viewState.viewId = ViewID}
        }

        fun unloadView(entityId :String){
            EntitiesData[entityId]?.let {
                it.viewState.isLoaded = false
            }
        }

        fun <T : IbaseViewData> onAddWaitForUpdateEvent(data: T, participation: List<participation>) {

            if (participation.contains(LIGHTSLIDERS)) {
                if(data is lightViewData) {
                    lightSliders_local.add(data as lightViewData)
                    lightSliders_has_changed = true
                }
            }
            if (participation.contains(SCRIPTBUTTONS)) {
                if(data is scriptViewData) {
                    buttonEvents_local.add(data as scriptViewData)
                    buttonEvents_has_changed = true
                }
            }
            if(participation.contains(MUSIC)){
                if(data is musicViewData){
                    music_has_changed = true
                }
            }
        }
}


inline fun <reified T : IbaseViewData> copyMutableList(
    fromList: MutableLiveData<List<T>>
): MutableList<T> {
    return fromList.value as MutableList<T>}


