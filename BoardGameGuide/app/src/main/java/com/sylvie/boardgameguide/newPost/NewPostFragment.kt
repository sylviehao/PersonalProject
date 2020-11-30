package com.sylvie.boardgameguide.newPost

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.Message
import com.sylvie.boardgameguide.databinding.FragmentNewPostBinding
import kotlinx.android.synthetic.main.activity_main.*

class NewPostFragment : Fragment() {

//    private val viewModel by viewModels<NewPostViewModel> {  }
    private lateinit var binding : FragmentNewPostBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNewPostBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.buttonAddPhoto.setOnClickListener {
            findNavController().navigate(R.id.action_global_uploadPhotoDialog)
        }


        val db = FirebaseFirestore.getInstance()
        binding.buttonNewPostCreate.setOnClickListener {

            val data = Event(
                hostId = "sylviehao",
                topic = "只收老司機",
                image = mutableListOf("https://images.unsplash.com/photo-1546381107-b5c6e7c1a8af?ixlib=rb-1.2.1&ixid=MXwxMjA3fDB8MHxzZWFyY2h8NzZ8fGJvYXJkJTIwZ2FtZXxlbnwwfHwwfA%3D%3D&auto=format&fit=crop&w=600&q=60"),
                time = 20201231,
                location = "我家",
                gameId = "說書人",
                playerLimit = 12,
                playerList = mutableListOf("Sylvie", "Louis", "Taiyi", "Gary", "Eric", "Tron", "Nicole", "Emil", "Johnny"),
                message = mutableListOf(Message(
                    id = "3",
                    eventId = "sylviehao",
                    userId = "eric",
                    message = "喝"
                )),
                description = "喜歡就來",
                rules = "遲到的請客",
                status = "ING",
                like = mutableListOf("louis", "eric", "tron", "sylvie", "nicole", "emil", "johnny")
            )

            // Add a new document with a generated ID
            db.collection("Event")
                .add(data)
                .addOnSuccessListener { documentReference ->
                    documentReference.update("id", documentReference.id)
                    Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference}")
                }
                .addOnFailureListener {
                    Log.d("TAG", "$it")
                    Toast.makeText(this.context, "Please sign in to post", Toast.LENGTH_SHORT)
                        .show()
                }
            findNavController().navigate(R.id.action_global_homeFragment)
        }





        return binding.root
    }

    override fun onStart() {
        super.onStart()
        let {
//            requireActivity().toolbar.visibility = View.GONE
            requireActivity().bottomNavView.visibility = View.GONE
        }
    }

    override fun onStop() {
        super.onStop()
        let {
//            requireActivity().toolbar.visibility = View.VISIBLE
            requireActivity().bottomNavView.visibility = View.VISIBLE
        }
    }
}