package com.sylvie.boardgameguide.detailPost

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.databinding.FragmentDetailPostBinding
import kotlinx.android.synthetic.main.activity_main.*

class DetailPostFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentDetailPostBinding.inflate(inflater, container,false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.buttonAddPhoto.setOnClickListener {
            findNavController().navigate(R.id.action_global_uploadPhotoDialog)
        }

        val db = FirebaseFirestore.getInstance()

//        //即時監聽資料庫是否變動
//        db.collection("Event").addSnapshotListener { value, error ->
//            value?.let {
//                val listResult = mutableListOf<Event>()
//                it.forEach { data ->
//                    val d = data.toObject(Event::class.java)
//                    listResult.add(d)
//
//                    Log.i("REALTIMETAG", "${data.data}")
//
//                }
//                adapter.submitList(listResult)
//            }
//        }

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