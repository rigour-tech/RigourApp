package com.dash.rigour.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dash.rigour.R
import com.dash.rigour.data.FirebaseService
import com.dash.rigour.data.User

import com.dash.rigour.databinding.ActivityChatBinding
import com.dash.rigour.fragment.DashboardFragment
import com.example.myfirsttask.adapter.MessageAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    var userList = ArrayList<User>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)


        FirebaseService.sharedPref = getSharedPreferences(
            "sharedPref",
            Context.MODE_PRIVATE
        )
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            FirebaseService.token = it.token
        }

        binding.userRecyclerView.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)


        binding.imgBack.setOnClickListener {
            val intent = Intent(applicationContext, DashboardFragment::class.java)
            startActivity(intent)
        }



        getUserList()


    }

    private fun getUserList() {
        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!

        var userid = firebase.uid
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/$userid")


        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("UsersInfo")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                val currentUser = snapshot.getValue(User::class.java)
                if (currentUser!!.userImage == "") {
                    binding.imgProfile.setImageResource(R.drawable.profile)
                } else {
                    Glide.with(this@ChatActivity).load(currentUser.userImage)
                        .into(binding.imgProfile)
                }

                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val user = dataSnapShot.getValue(User::class.java)

                    if (!user!!.userId.equals(firebase.uid)) {

                        userList.add(user)
                    }
                }


                val adapter = MessageAdapter(this@ChatActivity, userList)
                binding.userRecyclerView.adapter = adapter

            }


        })

    }


}
