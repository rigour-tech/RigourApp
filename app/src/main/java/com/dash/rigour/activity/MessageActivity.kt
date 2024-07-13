package com.dash.rigour.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dash.rigour.databinding.ActivityMessageBinding

class MessageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMessageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}