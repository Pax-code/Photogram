package com.example.instagramclonekt.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.instagramclonekt.view.tablayout.SignIn
import com.example.instagramclonekt.view.tablayout.SignUp

class PageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle ) : FragmentStateAdapter(fragmentManager, lifecycle){
    override fun getItemCount(): Int {
       return 2
    }

    override fun createFragment(position: Int): Fragment {
       return if (position == 0){
            SignIn()
        }else{
            SignUp()
        }
    }
}