package com.sylvie.boardgameguide.home.detail

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
import com.sylvie.boardgameguide.data.Message
import com.sylvie.boardgameguide.databinding.FragmentDetailPostBinding
import com.sylvie.boardgameguide.ext.getVmFactory
import com.sylvie.boardgameguide.login.UserManager
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class DetailPostFragment : Fragment() {

    val viewModel by viewModels<DetailPostViewModel> { getVmFactory() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDetailPostBinding.inflate(inflater, container, false)
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
//        bundle.game?.name?.let { viewModel.getGame(it) }

        val db = FirebaseFirestore.getInstance()

        db.collection("Event")
            .addSnapshotListener { value, error ->
                value?.let {
                    val listResult = mutableListOf<Event>()

                    it.forEach { data ->
                        val d = data.toObject(Event::class.java)
                        listResult.add(d)
                    }
                    var b = listResult.filter { result-> result.id == bundle.id }[0]
                    adapter.submitList(b.message)
                }
            }


        val dateString = SimpleDateFormat("MM/dd/yyyy HH:mm").format(Date(bundle.time))
        binding.textGameTime.text = dateString

        binding.icLike.setOnClickListener {
            UserManager.user.value?.let {userId->
                viewModel.setEvent(userId.id, bundle, true)
                if(bundle.like!!.any { it == userId.id }) {
                    bundle.like?.remove(userId.id)
                    viewModel.setEvent(userId.id, bundle, false)
                    binding.icLike.setBackgroundResource(R.drawable.ic_good_circle)
                }else{
                    bundle.like?.add(userId.id)
                    binding.icLike.setBackgroundResource(R.drawable.ic_like_selected)
                }
                viewModel.getEventData.value = bundle
            }
        }




        adapter2.submitList(bundle.image)
        adapter3.submitList(bundle.playerList)


        binding.buttonSend.setOnClickListener {

            val data = Message(
                hostId = bundle.user!!.id,
                user = UserManager.user.value,
                message = binding.editComment.text.toString()
            )
            val event = bundle
            event.message?.add(data)
            Log.i("message", event.toString())

            db.collection("Event").document(bundle.id)
                .set(event)
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
