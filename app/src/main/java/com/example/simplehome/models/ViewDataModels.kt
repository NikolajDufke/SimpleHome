package com.example.simplehome.models

import android.graphics.drawable.Drawable
import com.example.simplehome.R

interface IbaseViewData{
    val entity_id: String
    val friendly_name: String
    val state: String
    val isLoaded : Boolean
    val viewId : Int?
}

class baseViewData(
    override val entity_id: String,
    override val state: String,
    override val isLoaded: Boolean,
    override val viewId: Int?,
    override val friendly_name: String
): IbaseViewData

class scriptViewData(
    override val entity_id: String,
    override val friendly_name: String,
    override val state: String,
    override val isLoaded: Boolean,
    override val viewId: Int?,
    val Icon : Int = R.drawable.script
): IbaseViewData{

    constructor(baseViewData: baseViewData, icon: Int) : this(
        entity_id = baseViewData.entity_id,
        state = baseViewData.state,
        friendly_name = baseViewData.friendly_name,
        viewId = baseViewData.viewId,
        isLoaded = baseViewData.isLoaded,
        Icon = icon

    ) {

    }
}

class lightViewData(
    override val entity_id: String,
    override val state: String,
    override val isLoaded: Boolean,
    override val viewId: Int? = -1,
    var brightness: Float,
    override val friendly_name: String
): IbaseViewData {
    constructor(baseViewData: baseViewData, brightness: Float) : this(
        entity_id = baseViewData.entity_id,
        state = baseViewData.state,
        friendly_name = baseViewData.friendly_name,
        viewId = baseViewData.viewId,
        isLoaded = baseViewData.isLoaded,
        brightness = brightness

    )
}
class musicViewData(
    override val entity_id: String,
    override val friendly_name: String,
    override val state: String,
    override val isLoaded: Boolean,
    override val viewId: Int?,
    val Volume : Float,
    val Picture : String
):IbaseViewData {
    constructor(baseViewData: baseViewData, volume: Float, picture: String) : this(
        entity_id = baseViewData.entity_id,
        state = baseViewData.state,
        friendly_name = baseViewData.friendly_name,
        viewId = baseViewData.viewId,
        isLoaded = baseViewData.isLoaded,
        Volume = volume,
        Picture = picture
    )}


