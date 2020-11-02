package com.example.simplehome.Views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.simplehome.R
import com.example.simplehome.ViewModels.BaseLightControlViewModel

class BaseLightControlFragment : Fragment() {

    companion object {
        fun newInstance() = BaseLightControlFragment()
    }

    public lateinit var viewModel: BaseLightControlViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.light_control_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BaseLightControlViewModel::class.java)
        // TODO: Use the ViewModel
    }

}