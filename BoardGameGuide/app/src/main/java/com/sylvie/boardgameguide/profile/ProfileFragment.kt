package com.sylvie.boardgameguide.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.data.Message
import com.sylvie.boardgameguide.data.User
import com.sylvie.boardgameguide.databinding.FragmentProfileBinding
import com.sylvie.boardgameguide.ext.getVmFactory
import com.sylvie.boardgameguide.game.GameAdapter
import com.sylvie.boardgameguide.game.GameFragmentDirections
import com.sylvie.boardgameguide.game.GameViewModel

class ProfileFragment : Fragment() {

    val viewModel by viewModels<ProfileViewModel> { getVmFactory() }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileBinding.inflate(inflater, container,false)
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = ProfileBrowseAdapter(ProfileBrowseAdapter.OnClickListener {
            viewModel.navigateToDetail(it)
        })

        binding.recyclerBrowse.adapter = adapter

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(GameFragmentDirections.actionGlobalGameDetailFragment(it))
                viewModel.onDetailNavigated()
            }
        })

        val db = FirebaseFirestore.getInstance()
        //即時監聽資料庫是否變動
        db.collection("Game").addSnapshotListener { value, error ->
            value?.let {
                val listResult = mutableListOf<Game>()
                it.forEach { data ->
                    val d = data.toObject(Game::class.java)
                    listResult.add(d)
                }
                adapter.submitList(listResult)
            }
        }

//        val db = FirebaseFirestore.getInstance()
//        binding.buttonEditInfo.setOnClickListener {
//            val data = User(
//                name = "sylviehao",
//                introduction = "嗨嗨",
//                favorite = mutableListOf(
//                    Game(
//                    )
//                ),
//                browseRecently = mutableListOf()
//            )
//
//            // Add a new document with a generated ID
//            db.collection("User")
//                .add(data)
//                .addOnSuccessListener { documentReference ->
//                    documentReference.update("id", documentReference.id)
//                    Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference}")
//                }
//                .addOnFailureListener {
//                    Log.d("TAG", "$it")
//                    Toast.makeText(this.context, "Please sign in to post", Toast.LENGTH_SHORT)
//                        .show()
//                }
//        }


        return binding.root
    }
}