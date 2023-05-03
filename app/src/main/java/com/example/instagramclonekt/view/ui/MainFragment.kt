package com.example.instagramclonekt.view.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.instagramclonekt.R
import com.example.instagramclonekt.databinding.FragmentMainBinding
import com.example.instagramclonekt.view.ui.AddPostScreeenFragment
import com.example.instagramclonekt.view.ui.FeedScreenFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Show the feed screen fragment when the main fragment is first created
        replaceFragment(FeedScreenFragment())
        // Handle the bottom navigation bar item selection
        binding.bottomNavBar.setOnItemSelectedListener {
            when(it){
                // Show the feed screen fragment when the home icon is selected
                R.id.nav_home-> replaceFragment(FeedScreenFragment())
                // Show the add post screen fragment when the add icon is selected
                R.id.nav_add-> replaceFragment(AddPostScreeenFragment())
                // Display a sign-out confirmation dialog when the exit icon is selected
                R.id.nav_exit-> signOutAlert(view)
                else ->{
                    FeedScreenFragment()
                }

            }
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = childFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        // Replace the existing fragment with the new one
        fragmentTransaction.replace(R.id.frame_layout_main,fragment)
        fragmentTransaction.commit()
    }

    private fun signOutAlert(view: View){
        // Display a confirmation dialog when the user wants to sign out
        val alert = AlertDialog.Builder(context)
        alert.setTitle("Photogram")
        alert.setMessage("You will be signed out. Are you sure?")
        // Sign out and navigate to the login screen when the user confirms
        alert.setPositiveButton("Yes"){dialog,which ->
            auth.signOut()
            val action = MainFragmentDirections.actionMainFragmentToLoginScreen()
            Navigation.findNavController(view).navigate(action)
        }
        alert.setNegativeButton("No"){dialog,which ->
        }
        alert.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clear the binding when the view is destroyed to avoid memory leaks
        _binding = null
    }
}
