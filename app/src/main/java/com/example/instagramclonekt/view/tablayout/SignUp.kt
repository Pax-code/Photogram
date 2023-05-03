package com.example.instagramclonekt.view.tablayout

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.instagramclonekt.databinding.FragmentSignUpBinding
import com.example.instagramclonekt.view.ui.LoginScreenDirections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUp : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize auth variable with Firebase authentication instance.
        auth = Firebase.auth
        db = Firebase.firestore
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using the FragmentSignUpBinding.
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)

        binding.signUp.setOnClickListener(){
            // Call textsIsEmpty function with the view passed as a parameter.
            textsIsEmpty(it)
        }

        return binding.root
    }

    // Define textsIsEmpty function with a view parameter.
    private fun textsIsEmpty(view: View){
        val errorMsg = StringBuilder()

        // Get email, username, password, and passwordAgain values from their corresponding views and trim the spaces.
        val email= binding.emailText.text.toString().trim()
        val userName= binding.userNameText.text.toString().trim()
        val password= binding.passwordText.text.toString().trim()
        val passwordAgain= binding.passwordText2.text.toString().trim()

        // Check if email is empty.
        if (TextUtils.isEmpty(email)){
            // Set error message to emailText and append it to errorMsg.
            binding.emailText.error = "E-mail can not be empty"
            errorMsg.append(" ")
        }
        if (TextUtils.isEmpty(userName)){
            binding.userNameText.error = "User name can not be empty"
            errorMsg.append(" ")
        }else{

            // Check if username already exists in Firestore.
            db.collection("users")
                .whereEqualTo("username", userName)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.size() > 0) {
                        binding.userNameText.error = "Username already exists"
                        errorMsg.append(" ")
                    }
                }.addOnFailureListener{
                    Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
                }
        }
        if (TextUtils.isEmpty(password)){
            binding.passwordText.error = "Password can not be empty"
            errorMsg.append(" ")
        }
        if (TextUtils.isEmpty(passwordAgain)){
            binding.passwordText2.error = "Re-Enter password"
            errorMsg.append(" ")
        }
        // Check if password and passwordAgain are the same.
        if (password != passwordAgain ){
            errorMsg.append("Passwords are doesn't same.\n")
        }
        // Check if there is any error message.
        if (errorMsg.isNotEmpty()) {
            Toast.makeText(requireContext(), errorMsg.toString().trim(), Toast.LENGTH_SHORT).show()
        }else {
            // Create user with email and password using Firebase authentication instance.
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                // Add user to Firestore.
                val user = hashMapOf(
                    "username" to userName,
                    "email" to email
                )
                db.collection("users")
                    .add(user)
                    .addOnSuccessListener { documentReference ->
                        val action = LoginScreenDirections.actionLoginScreenToMainFragment()
                        Navigation.findNavController(view).navigate(action)
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG).show()
                    }

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
