package com.dash.rigour.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dash.rigour.R
import com.dash.rigour.adapter.JobPostedAdapter
import com.dash.rigour.data.JobsInfo
import com.dash.rigour.databinding.FragmentDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar


class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var userArrayList: ArrayList<JobsInfo>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val view = binding.root

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("ProjectsAdded")


        val sharedPreferences = activity?.getSharedPreferences("info", Context.MODE_PRIVATE)
        val lastName = sharedPreferences?.getString("last_name", "").toString()

        binding.lastName.text = lastName

        binding.addProject.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_dashboardFragment2_to_addProjects)
        }

        check()
        userArrayList = arrayListOf<JobsInfo>()
        getUserData()

        return view
    }

    private fun getUserData() {
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {


                    for (userSnapshot in snapshot.children) {

                        val user = userSnapshot.getValue(JobsInfo::class.java)
                        userArrayList.add(user!!)

                    }
                    binding.recyclerView.adapter = JobPostedAdapter(userArrayList)

                }


            }

            override fun onCancelled(error: DatabaseError) {


            }


        })


    }

    private fun check() {

        val c: Calendar = Calendar.getInstance()
        val timeOfDay: Int = c.get(Calendar.HOUR_OF_DAY)
        val text1 = "Good Morning"
        val text2 = "Good Afternoon"
        val text3 = "Good Evening"
        val text4 = "Good Night"
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