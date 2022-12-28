package com.crownedjester.soft.findoutagebyname.representation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.crownedjester.soft.findoutagebyname.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }
}