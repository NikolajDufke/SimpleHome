package com.example.simplehome.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.simplehome.models.Attributes
import com.example.simplehome.models.Attributes_light
import com.example.simplehome.models.result


class Entities{

    companion object{

        private val TAG = "Entities"

        //Data collections
        var buttonEvents : MutableLiveData<List<result>> = MutableLiveData()
        var lightSliders_data : MutableLiveData<List<result>> = MutableLiveData()
        var EntitiesData : MutableMap<String, result> = mutableMapOf()

        //Hardcoded Entities
        val scripts : List<String> = listOf(
            "script.1585514325951",
            "script.1585515702737",
            "script.1586409097105")
        val firstFlooreEntities : List<String> = listOf(
            "light.hue_go_1",
            "light.repo",
            "media_player.borne_vaerelse",
            "light.ellas_seng",
            "light.johanna_s_seng",
            "sensor.motorola_one_vision_battery_level_2")
        val firstfloorSlidersSelection : List<String> = listOf(
            "light.hue_go_1",
            "light.repo")

        fun onEntityCreate(entity: result){
            EntitiesData.put(entity.entity_id,entity)
            Log.d(TAG, "getting entities " + entity.entity_id )
        }

        fun onEntityCreate(entities: MutableList<result>?){
            entities?.forEach { onEntityCreate(it) }
            Log.d(TAG, "setting buttons")
            setButtons()
            Log.d(TAG, "setting lights")
            setLightSliders()


        }

        fun onEntityUpdate(enti: result){

            val enti_old = EntitiesData[enti.entity_id]

            val attr_enti : Attributes
            val attr_enti_old : Attributes
            when(enti_old?.getDomain()){
                "light" -> {
                    attr_enti = enti.attributes as Attributes_light
                    attr_enti_old = EntitiesData[enti.entity_id]?.attributes as Attributes_light
                    }
                else -> {
                attr_enti = enti.attributes!!
                attr_enti_old = EntitiesData[enti.entity_id]?.attributes!!
                }
            }

            val condition1 = enti_old?.compareTo(enti)
            val condition2 = attr_enti_old.compareTo(attr_enti)

            if(condition1 != 0 || condition2 != 0) {
                EntitiesData[enti.entity_id]?.update(enti)
            }
        }

        fun setButtons(){
           upDateLiveData(scripts, buttonEvents)
        }
        fun setLightSliders() {
            upDateLiveData(firstfloorSlidersSelection, lightSliders_data)
        }
        fun upDateLiveData(entis: List<String>, distnation: MutableLiveData<List<result>>){
            val entiList = mutableListOf<result>()
            entis.forEach {
                EntitiesData[it]?.let {
                    it1 ->
                    val enti= it1.copy()
                    it1.add(enti)
                    entiList.add(enti)

                }
            }
            distnation.value = entiList
        }



        }

    }
