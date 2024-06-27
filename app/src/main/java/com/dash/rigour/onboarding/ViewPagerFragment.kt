package com.dash.rigour.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.dash.rigour.R
import com.dash.rigour.adapter.Onboarding
import com.dash.rigour.databinding.FragmentViewPagerBinding


class ViewPagerFragment : Fragment() {
    private var _binding: FragmentViewPagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        _binding = FragmentViewPagerBinding.inflate(inflater, container, false)
        val view = binding.root


        var fragmentList = arrayListOf<Fragment>(
            FirstFrame(),
            SecondFrame(),
            ThirdFrame(),

            )


        val adapter = Onboarding(
            fragmentList,
            requireActivity().supportFragmentManager, lifecycle
        )
        binding.viewPager.adapter = adapter
        binding.dotsIndicator.attachTo(binding.viewPager)


        binding.signup.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_viewPagerFragment_to_signUpFragment)
        }
        binding.login.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_viewPagerFragment_to_loginFragment)
        }



        return view
    }


}