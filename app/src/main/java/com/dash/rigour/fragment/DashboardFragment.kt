package com.dash.rigour.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dash.rigour.databinding.FragmentDashboardBinding
import java.util.Calendar


class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val view = binding.root

        val sharedPreferences = activity?.getSharedPreferences("info", Context.MODE_PRIVATE)
        val workerName = sharedPreferences?.getString("last_name", "").toString()

        binding.lastName.text = workerName


        return view
    }

    private fun check() {

        val c: Calendar = Calendar.getInstance()
        val timeOfDay: Int = c.get(Calendar.HOUR_OF_DAY)
        var text1 = "Good Morning"
        var text2 = "Good Afternoon"
        var text3 = "Good Evening"
        var text4 = "Good Night"
        if (timeOfDay in 0..11) {
            binding.greetingsName.text = text1.toString()
        } else if (timeOfDay in 12..15) {
            binding.greetingsName.text = text2.toString()
        } else if (timeOfDay in 16..20) {
            binding.greetingsName.text = text3.toString()
        } else {
            if (timeOfDay in (21..23)) {
                binding.greetingsName.text = text4.toString()
            }
        }
    }

}