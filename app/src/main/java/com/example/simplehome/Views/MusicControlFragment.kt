package com.example.simplehome.Views


import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.simplehome.R
import com.example.simplehome.ViewModels.MusicControlViewModel
import com.example.simplehome.models.musicViewData
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.music_control_fragment.*
import java.net.URL


class MusicControlFragment : Fragment() {

    companion object {
        fun newInstance() = MusicControlFragment()
    }

    private lateinit var viewModel: MusicControlViewModel
    private var VolumeControlViewState:Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.music_control_fragment, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
         viewModel.unloadView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(MusicControlViewModel::class.java)

        setVolumeControlViewState()
        MusicVolumeButton.setOnClickListener{
            when(VolumeControlViewState){
                View.VISIBLE -> {
                  MusicVolumeControl.visibility = View.GONE
                  setVolumeControlViewState()
              }
                View.GONE -> {
                  MusicVolumeControl.visibility = View.VISIBLE
                  setVolumeControlViewState()
              }
            }
        }

        var isVolumeUpdateFromHA : Boolean = false
        MusicVolumeControl.addOnChangeListener { slider, value, fromUser ->
            if(!isVolumeUpdateFromHA){
                viewModel.onVolumeChange(value)}
        }
        viewModel.Volume?.observe(viewLifecycleOwner, Observer<Float>{ volume ->
            if(MusicVolumeControl.value != volume){
                isVolumeUpdateFromHA = true
                MusicVolumeControl.value = volume
                isVolumeUpdateFromHA = false
            }
        })

        viewModel.MusicViewData?.observe(viewLifecycleOwner, Observer<musicViewData>{ player ->
            Picasso.get().load(player.Picture).into(imageView)
        })


        MusicPlauPauseButton.setOnClickListener {
            viewModel.playPause()
        }

        MusicBackButton.setOnClickListener {
            viewModel.PreviousTrack()
        }

        MusicForwardButton.setOnClickListener {
            viewModel.naxtTrack()
        }

    }



    private fun setVolumeControlViewState() {
        VolumeControlViewState = MusicVolumeControl.visibility
    }
}


