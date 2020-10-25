package com.example.simplehome

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.simplehome.models.UserTest

class EventButtonsFragment : Fragment() {

    companion object {
        fun newInstance() = EventButtonsFragment()
    }

    private lateinit var viewModel: EventButtonsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.event_buttons_fragment, container, false)



    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EventButtonsViewModel::class.java)

        //viewModel.getTest().observe( viewLifecycleOwner, Observer<List<String>>{})
        viewModel.getUserTest().observe(viewLifecycleOwner, Observer<UserTest>{
            val t = view?.findViewById<TextView>(R.id.hello)
            t?.text = "test2"
        })

    }
}