package com.orbitalsonic.appfinderpackage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.orbitalsonic.appfinderpackage.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)

        val packagePagerAdapter = PackagePagerAdapter(this, supportFragmentManager)
        binding.viewPager.adapter = packagePagerAdapter
        binding.tabs.setupWithViewPager(binding.viewPager)

    }
}