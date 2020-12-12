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
import com.sylvie.boardgameguide.login.UserManager

class ProfileFragment : Fragment() {

    val viewModel by viewModels<ProfileViewModel> { getVmFactory() }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileBinding.inflate(inflater, container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val db = FirebaseFirestore.getInstance()

        val adapter = ProfileBrowseAdapter(ProfileBrowseAdapter.OnClickListener {
            viewModel.navigateToDetail(it)
        })

        binding.recyclerBrowse.adapter = adapter

        binding.imageProfile.setOnClickListener {
            UserManager.clear()
            Toast.makeText(context, "Sign Out", Toast.LENGTH_SHORT).show()
        }

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(GameFragmentDirections.actionGlobalGameDetailFragment(it))
                viewModel.onDetailNavigated()
            }
        })

        //即時監聽資料庫是否變動
        db.collection("Event").whereEqualTo("status","OPEN")
            .addSnapshotListener { value, error ->
                value?.let {
                    val listResult = mutableListOf<Event>()
                    val listResultOpen = mutableListOf<Event>()
                    it.forEach { data ->
                        val d = data.toObject(Event::class.java)
                        listResult.add(d)
                    }
                    listResult.sortByDescending { it.createdTime }
                    listResultOpen.addAll( listResult.filter {list ->
                        list.playerList!!.any { name -> name == UserManager.user.value?.name }
                    })
                    binding.textGameNumber.setText(listResultOpen.size.toString())
                }
            }

        //即時監聽資料庫是否變動
        db.collection("Event").whereEqualTo("status","CLOSE")
            .addSnapshotListener { value, error ->
                value?.let {
                    val listResult = mutableListOf<Event>()
                    val listResultOpen = mutableListOf<Event>()
                    it.forEach { data ->
                        val d = data.toObject(Event::class.java)
                        listResult.add(d)
                    }
                    listResult.sortByDescending { it.createdTime }
                    listResultOpen.addAll( listResult.filter {list ->
                        list.playerList!!.any { name -> name == "sylviehao" }
                    })
                    binding.textPostNumber.setText(listResultOpen.size.toString())
                }
            }



        viewModel.getUserData.observe(viewLifecycleOwner, Observer {
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
        })

        binding.buttonEditInfo.setOnClickListener {
            binding.textDescription.isEnabled = true
//            binding.textEdit.text = "SEND"
            if (binding.textEdit.text == "SEND") {
            viewModel.getUserData.value?.let { user -> viewModel.setUser(user, binding.textDescription.text.toString() ) }
                binding.textDescription.isEnabled = false
                binding.textEdit.text = getString(R.string.edit)
            } else {
                binding.textEdit.text = "SEND"
            }
        }


//        binding.buttonEditInfo.setOnClickListener {
//            val data = User(
//                id = "001",
//                name = "sylviehao",
//                image = "https://images.unsplash.com/photo-1526800544336-d04f0cbfd700?ixid=MXwxMjA3fDB8MHxzZWFyY2h8MTZ8fHByb2ZpbGV8ZW58MHx8MHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60",
//                introduction = "嗨嗨",
//                favorite = mutableListOf(
//                    Game(
//                    )
//                ),
//                browseRecently = mutableListOf()
//            )
//
//            // Add a new document with a generated ID
//            db.collection("User").document("001")
//                .set(data)
//                .addOnSuccessListener { documentReference ->
////                    documentReference.update("id", documentReference.id)
//                    Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference}")
//                }
//                .addOnFailureListener {
//                    Log.d("TAG", "$it")
//                    Toast.makeText(this.context, "Please sign in to post", Toast.LENGTH_SHORT)
//                        .show()
//                }
//        }

        binding.constraintPost.setOnClickListener {
            findNavController().navigate(R.id.action_global_profilePostFragment)
        }

        binding.constraintEvent.setOnClickListener {
            findNavController().navigate(R.id.action_global_profileEventFragment)
        }


        return binding.root
    }
}