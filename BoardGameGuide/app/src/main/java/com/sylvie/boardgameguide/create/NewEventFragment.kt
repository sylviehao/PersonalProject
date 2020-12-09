package com.sylvie.boardgameguide.create

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.databinding.FragmentNewEventBinding
import com.sylvie.boardgameguide.ext.getVmFactory
import com.sylvie.boardgameguide.game.detail.GameDetailFragmentArgs
import kotlinx.android.synthetic.main.activity_main.*

class NewEventFragment : Fragment() {

    val viewModel by viewModels<NewEventViewModel> { getVmFactory() }
    private lateinit var binding : FragmentNewEventBinding

    var arg: Game? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNewEventBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        arg = NewEventFragmentArgs.fromBundle(requireArguments()).game
        viewModel.game.value = arg


        binding.editNewEventGameTime.setOnClickListener {
            SingleDateAndTimePickerDialog.Builder(context)
                .bottomSheet()
                .curved()
                .backgroundColor(resources.getColor(R.color.oliveGreen))
                .mainColor(Color.WHITE)
                .titleTextColor(Color.WHITE)
                //.stepSizeMinutes(15)
                //.displayHours(false)
                //.displayMinutes(false)
                //.todayText("aujourd'hui")
                .displayListener {}
                .title("Simple")
                .listener { date ->
                    binding.editNewEventGameTime.text = date.toString()
                    viewModel.date.value = date.time
                }
                .display()
        }

        val db = FirebaseFirestore.getInstance()
        binding.buttonNewEventCreate.setOnClickListener {
            val data = viewModel.date.value?.let { date ->
                Event(
                    hostId = "eric",
                    topic = binding.editNewEventTopic.text.toString(),
                    time = date,
                    location = binding.editNewEventGameLocation.text.toString(),
                    image = mutableListOf("https://images.unsplash.com/photo-1573141335932-9b22c45aa2df?ixid=MXwxMjA3fDB8MHxzZWFyY2h8MzF8fGdhbWV8ZW58MHx8MHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"),
                    gameId = "a002",
                    playerLimit = 7,
                    status = "OPEN",
                )
            }

                // Add a new document with a generated ID
            if (data != null) {
                db.collection("Event")
                    .add(data)
                    .addOnSuccessListener { documentReference ->
                        documentReference.update("id", documentReference.id)
                        Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference}")
                    }
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