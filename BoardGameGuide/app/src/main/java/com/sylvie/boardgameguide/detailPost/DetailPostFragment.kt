package com.sylvie.boardgameguide.detailPost

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.data.Message
import com.sylvie.boardgameguide.databinding.FragmentDetailPostBinding
import com.sylvie.boardgameguide.ext.getVmFactory
import kotlinx.android.synthetic.main.activity_main.*

class DetailPostFragment : Fragment() {

    val viewModel by viewModels<DetailPostViewModel> { getVmFactory() }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentDetailPostBinding.inflate(inflater, container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val adapter = DetailPostCommentAdapter()
        val adapter2 = DetailPostPhotoAdapter()
        val adapter3 = DetailPostPlayerAdapter()
        binding.recyclerComment.adapter = adapter
        binding.recyclerPhoto.adapter = adapter2
        binding.recyclerPlayer.adapter = adapter3


        val bundle = DetailPostFragmentArgs.fromBundle(requireArguments()).event

        viewModel.getEventData.value = bundle
        viewModel.getGame(bundle.gameId)

        val db = FirebaseFirestore.getInstance()

        db.collection("Event")
            .addSnapshotListener { value, error ->
            value?.let {
                val listResult = mutableListOf<Message>()
                it.forEach { data ->
                    val d = data.toObject(Message::class.java)
                    listResult.add(d)
                }
                adapter.submitList(listResult)
            }
        }

        adapter2.submitList(bundle.image)
        adapter3.submitList(bundle.playerList)


        binding.buttonSend.setOnClickListener {

            val data = Message (
                hostId = bundle.hostId,
                userId = "sylviehao",
                message = binding.editComment.text.toString()
            )
            val a = bundle
            a.message?.add(data)
            Log.i("message",a.toString())

            db.collection("Event").document(bundle.id)
                .set(a)
                .addOnSuccessListener { documentReference ->
//                    documentReference.update("id", bundle.id)
                    Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference}")
                }
                .addOnFailureListener {
                    Log.d("TAG", "$it")
                    Toast.makeText(this.context, "Please sign in to post", Toast.LENGTH_SHORT)
                        .show()
                }
        }

//        val db = FirebaseFirestore.getInstance()

//        db.collection("Game")
//            .get()
//            .addOnSuccessListener {
//                val listResult = mutableListOf<Game>()
//                it.forEach { data ->
//                    val d = data.toObject(Game::class.java)
//                    listResult.add(d)
//                }
//                viewModel.getGameData.value =
//                    listResult.filter { list -> list.id == bundle.gameId }[0]
//            }

        binding.buttonAddPhoto.setOnClickListener {
            findNavController().navigate(R.id.action_global_uploadPhotoDialog)
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
