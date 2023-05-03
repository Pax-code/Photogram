package com.example.instagramclonekt.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instagramclonekt.adapters.FeedRecyclerAdapter
import com.example.instagramclonekt.databinding.FragmentFeedScreenBinding
import com.example.instagramclonekt.models.PostModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FeedScreenFragment : Fragment() {

    private var _binding: FragmentFeedScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var postArrayList: ArrayList<PostModel>
    private lateinit var feedRecyclerAdapter: FeedRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        firestore = Firebase.firestore

        postArrayList = ArrayList<PostModel>()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFeedScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        feedRecyclerAdapter = FeedRecyclerAdapter(postArrayList)
        binding.recyclerView.adapter = feedRecyclerAdapter

        gettingData()
    }
    private fun gettingData(){
        firestore.collection("Posts").orderBy("date",Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            if (error != null){
                Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_LONG).show()
            }else{
                if(value != null){
                    if (!value.isEmpty){

                        val documents = value.documents
                        postArrayList.clear()
                        for (document in documents){
                            val comment = document.get("comment") as String
                            val downloadUrl = document.get("downloadUrl") as String
                            val location = document.get("location") as String
                            val username = document.get("username") as String?

                            val postModel = PostModel(comment,username!!,location,downloadUrl)
                            postArrayList.add(postModel)
                        }
                        feedRecyclerAdapter.notifyDataSetChanged()
                    }
                }
            }git 
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}