package com.example.simplehome

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.simplehome.HomeAssistantConnection.HomeAssistActions
import com.example.simplehome.models.Attributes_light
import com.example.simplehome.models.result
import com.example.simplehome.HomeAssistantConnection.SocketManager
import com.example.simplehome.Repository.Entities
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private val TAG = "MainActivity"

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



        viewModel.horizontalButtons?.observe(this, Observer<List<result>>{ it ->

            Log.d(TAG, "setting horizontal buttons")
            it.forEach {result ->
                Log.d(TAG, "setting button" + result.entity_id)
                 val inflater = LayoutInflater.from(mainhorozonlalButtoonLinarLayout.context)
                 val view: View = inflater.inflate(R.layout.evenbutton, mainhorozonlalButtoonLinarLayout,true)

                val eb = view.findViewById<Button>(R.id.eventbutton)
                eb.text = result.attributes?.friendly_name

                eb.setOnClickListener(){
                        HomeAssistActions().sendScript(result.getEntiId())
                    }
                eb.id = eb.id + 1
            }
        })


        viewModel.lightSliders?.observe(this, Observer<List<result>>{
            Log.d(TAG, "setting light sliders")
            it.forEach{result ->


                Log.d(TAG, "setting light " + result.entity_id)
                val inflater = LayoutInflater.from(mainLightSlidersLinarLayout.context)
                val view: View = inflater.inflate(R.layout.evenbutton, mainLightSlidersLinarLayout,true)



                //TODO make dynamic

                if(result.entity_id == "light.hue_go_1")
                {

                   val att = result.attributes as Attributes_light

                    mainLight1TextView.text = att.friendly_name
                    mainLight1Slider.value =  att.brightness.toFloat()
                    mainLight1Slider.addOnChangeListener { slider, value, fromUser ->

                    }
                }

            }
        })

        Entities.setButtons()
        Entities.setLightSliders()


    }

}


//done socket connection
//TODO custom components
//TODO select entities from a list
