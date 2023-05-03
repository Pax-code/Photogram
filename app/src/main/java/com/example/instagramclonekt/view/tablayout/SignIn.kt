package com.example.instagramclonekt.view.tablayout

import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.instagramclonekt.R
import com.example.instagramclonekt.databinding.FragmentSignInBinding
import com.example.instagramclonekt.view.ui.LoginScreenDirections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignIn : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private val PREFS_NAME = "MyPrefsFile"
    private val KEY_REMEMBER_ME = "rememberMe"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inflate the layout for this fragment
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        val view = binding.root

        //Set up NavController
        val navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
        Navigation.setViewNavController(view, navController)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signIn.setOnClickListener(){
            textsIsEmpty(binding.root)
        }

        //Get the shared preferences
        val sharedPref: SharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, 0)

        //Set the switch buttons checked state based on the rememberMe value in shared preferences
        binding.switch1.isChecked = sharedPref.getBoolean(KEY_REMEMBER_ME, false)

        //Set the switch buttons onCheckedChangeListener to update the rememberMe value in shared preferences
        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            val editor: SharedPreferences.Editor = sharedPref.edit()
            editor.putBoolean(KEY_REMEMBER_ME, isChecked)
            editor.apply()
        }

        //If the rememberMe value is true skip the login screen and navigate to the main fragment
        if (binding.switch1.isChecked) {
            auth.currentUser?.let {
                val action = LoginScreenDirections.actionLoginScreenToMainFragment()
                Navigation.findNavController(view).navigate(action)
            }
        }
    }

    private fun textsIsEmpty(view: View){

        val email= binding.emailText.text.toString().trim()
        val password= binding.passwordText.text.toString().trim()

        if (TextUtils.isEmpty(email)){
            binding.emailText.error = "E-mail can not be empty"

        } else if (TextUtils.isEmpty(password)){

            binding.passwordText.error = "Password can not be empty"
        }else {
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                val action = LoginScreenDirections.actionLoginScreenToMainFragment()
                Navigation.findNavController(view).navigate(action)
            }.addOnFailureListener{
                Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
