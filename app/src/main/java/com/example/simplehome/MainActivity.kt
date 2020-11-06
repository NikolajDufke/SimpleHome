package com.example.simplehome

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.simplehome.HomeAssistantConnection.SocketManager
import com.example.simplehome.models.baseViewData
import com.example.simplehome.models.lightViewData
import com.example.simplehome.models.scriptViewData
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.Slider
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(){

    private lateinit var viewModel: MainActivityViewModel
    private val TAG = "MainActivity"

    var ElemetidHelper : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SocketManager.Start()


        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        //viewModel.getTest().observe( viewLifecycleOwner, Observer<List<String>>{})
        viewModel.toastMessage.observe(this, Observer<String>{
            val toast = Toast.makeText(baseContext,it , Toast.LENGTH_LONG).show()
           // main1Button.setText(it)

        })


        viewModel.horizontalButtons?.observe(this, Observer<List<scriptViewData>>{ it ->

            Log.d(TAG, "setting horizontal buttons")
            it.forEach {viewData ->
                if(viewData.isLoaded == false){
                    Log.d(TAG, "creating button" + viewData.entity_id)
                    val inflater = LayoutInflater.from(mainhorozonlalButtoonLinarLayout.context)
                    val view: View = inflater.inflate(R.layout.evenbutton, mainhorozonlalButtoonLinarLayout,true)

                    val eb = view.findViewById<MaterialButton>(R.id.eventbutton)
                    eb.setIconResource(viewData.Icon)

                    eb.setOnClickListener(){
                            viewModel.RunScript(viewData.entity_id)
                    }

                    val viewID =  findAvalibleId(eb.id)
                    eb.id = viewID

                    viewModel.ViewLoaded(viewData.entity_id, viewID)

                }
            }
        })

        viewModel.lightSliders?.observe(this, Observer<List<lightViewData>>{
            Log.d(TAG, "setting light sliders")
            it.forEach{viewData ->
                if(viewData.isLoaded == false) {
                    Log.d(TAG, "creating light " + viewData.entity_id)
                    val inflater = LayoutInflater.from(mainLightSlidersLinarLayout.context)
                    val view: View =
                        inflater.inflate(R.layout.lightslider, mainLightSlidersLinarLayout, true)

                    val textView = view.findViewById<TextView>(R.id.LightsliderTextView)
                    val slider = view.findViewById<Slider>(R.id.lightsliderSlider)

                    textView.text = viewData.friendly_name
                    slider.value = viewData.brightness
                    slider.addOnChangeListener { slider, value, fromUser ->
                        viewModel.SliderLightValueChange(viewData.entity_id,slider.value)
                    }


                    val viewID =  findAvalibleId(slider.id)
                    slider.id = viewID

                    viewModel.ViewLoaded(viewData.entity_id, viewID)
                    textView.id = findAvalibleId(textView.id)
                }
            }
        })

        viewModel.getHorizontalButtons()
        viewModel.getLightSliders()

    }

    fun findAvalibleId(id: Int): Int {

        do {
            val eid = ++ElemetidHelper
            if (findViewById<View?>(id + eid) == null){
            return id + ElemetidHelper
            }
        }while ((findViewById<View?>(id + eid) != null))

        return 0
    }



}


//done socket connection
//TODO custom components
//TODO select entities from a list
