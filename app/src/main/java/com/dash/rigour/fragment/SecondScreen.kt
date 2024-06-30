package com.dash.rigour.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.dash.rigour.R
import com.dash.rigour.activity.HomeActivity
import com.dash.rigour.databinding.FragmentSecondScreenBinding
import com.google.firebase.auth.FirebaseAuth


class SecondScreen : Fragment() {
    private var _binding: FragmentSecondScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSecondScreenBinding.inflate(inflater, container, false)
        val view = binding.root

        auth = FirebaseAuth.getInstance()


        Handler(Looper.myLooper()!!).postDelayed(Runnable {
            if (auth.currentUser != null) {
                val intent = Intent(requireContext(), HomeActivity::class.java)
                activity?.startActivity(intent)
            } else {
                Navigation.findNavController(view)
                    .navigate(R.id.action_secondScreen_to_viewPagerFragment)
            }
        }, 7000)

        return view
    }


}