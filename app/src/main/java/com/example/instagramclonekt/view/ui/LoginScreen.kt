package com.example.instagramclonekt.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.instagramclonekt.adapters.PageAdapter
import com.example.instagramclonekt.databinding.FragmentLoginScreenBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class LoginScreen : Fragment() {
    private var _binding: FragmentLoginScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var tablayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: PageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginScreenBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pageAdapter()

    }

    private fun pageAdapter(){
        tablayout = binding.tabLayout
        viewPager = binding.viewPager
        // Adapter initialization and setting
        adapter = PageAdapter(childFragmentManager,lifecycle)
        viewPager.adapter = adapter

        // TabLayout setup with ViewPager
        TabLayoutMediator(tablayout,viewPager){tab,position ->
            if (position == 0){
                tab.text = "SIGN IN"
            }else{
                tab.text = "REGISTER"
            }

        }.attach()

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}