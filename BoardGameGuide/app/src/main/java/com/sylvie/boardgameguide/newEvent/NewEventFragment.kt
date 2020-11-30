package com.sylvie.boardgameguide.newEvent

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
import com.sylvie.boardgameguide.databinding.FragmentNewEventBinding
import com.sylvie.boardgameguide.databinding.FragmentNewPostBinding
import kotlinx.android.synthetic.main.activity_main.*

class NewEventFragment : Fragment() {

    //    private val viewModel by viewModels<NewPostViewModel> {  }
    private lateinit var binding : FragmentNewEventBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNewEventBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        val db = FirebaseFirestore.getInstance()
        binding.buttonNewEventCreate.setOnClickListener {
            val data = Event(
                hostId = "louis",
                topic = "好玩就好",
                time = 20201211,
                location = "馬路上",
                gameId = "風聲",
                playerLimit = 7,
                description = "讚讚",
                rules = "不可以太強",
                status = "OPEN",
            )

            // Add a new document with a generated ID
            db.collection("Event")
                .add(data)
                .addOnSuccessListener { documentReference ->
                    documentReference.update("id", documentReference.id)
                    Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference}")
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