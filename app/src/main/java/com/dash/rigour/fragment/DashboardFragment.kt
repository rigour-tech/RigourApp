package com.dash.rigour.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dash.rigour.R
import com.dash.rigour.activity.ChatActivity
import com.dash.rigour.adapter.JobPostedAdapter
import com.dash.rigour.data.JobsInfo
import com.dash.rigour.databinding.FragmentDashboardBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import java.util.Calendar


class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var userArrayList: MutableList<JobsInfo>
    private lateinit var taskAdapter: JobPostedAdapter
    var fabVisible = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val view = binding.root

        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference.child("ProjectsAdded")


        val sharedPreferences = activity?.getSharedPreferences("info", Context.MODE_PRIVATE)
        val lastName = sharedPreferences?.getString("last_name", "").toString()

        binding.lastName.text = lastName

        fabVisible = false

        binding.addFab.setOnClickListener {
            // on below line we are checking
            // fab visible variable.
            if (!fabVisible) {

                // if its false we are displaying home fab
                // and settings fab by changing their
                // visibility to visible.
                binding.message.show()
                binding.addPost.show()

                // on below line we are setting
                // their visibility to visible.
                binding.message.visibility = View.VISIBLE
                binding.addPost.visibility = View.VISIBLE

                // on below line we are checking image icon of add fab
                binding.addFab.setImageDrawable(resources.getDrawable(R.drawable.baseline_close_24))

                // on below line we are changing
                // fab visible to true
                fabVisible = true
            } else {

                // if the condition is true then we
                // are hiding home and settings fab
                binding.message.hide()
                binding.addPost.hide()

                // on below line we are changing the
                // visibility of home and settings fab
                binding.message.visibility = View.GONE
                binding.addPost.visibility = View.GONE

                // on below line we are changing image source for add fab
                binding.addFab.setImageDrawable(resources.getDrawable(R.drawable.baseline_add_24))

                // on below line we are changing
                // fab visible to false.
                fabVisible = false
            }
        }

        // on below line we are adding
        // click listener for our home fab
        binding.message.setOnClickListener {
            // on below line we are displaying a toast message.

            val intent = Intent(requireContext(), ChatActivity::class.java)
            activity?.startActivity(intent)


        }

        // on below line we are adding on
        // click listener for settings fab
        binding.addPost.setOnClickListener {
            // on below line we are displaying a toast message

            Navigation.findNavController(view)
                .navigate(R.id.action_dashboardFragment2_to_addProjects)


        }




        check()
        getUserData()

        return view
    }

    private fun getUserData() {
        userArrayList = mutableListOf()
        taskAdapter = JobPostedAdapter(userArrayList)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {


                    for (userSnapshot in snapshot.children) {

                        val user = userSnapshot.getValue(JobsInfo::class.java)

                        if (user != null) {
                            userArrayList.add(user)
                        }

                    }
                    binding.recyclerView.adapter = taskAdapter

                }
                taskAdapter.notifyDataSetChanged()


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