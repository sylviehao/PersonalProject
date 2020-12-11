package com.sylvie.boardgameguide.create

import android.graphics.Color
import android.os.Bundle
import com.sylvie.boardgameguide.login.UserManager
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
import com.sylvie.boardgameguide.data.User
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

        binding.buttonAddPhoto.setOnClickListener {
            findNavController().navigate(R.id.action_global_uploadPhotoDialog)
        }

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

        binding.buttonNewEventCreate.setOnClickListener {
            val typeList= mutableListOf<String>()
            typeList.add(binding.editNewEventGameType.text.toString())

//            val memberList = mutableListOf<String>()
//            memberList.add(binding.editNewEventGameMember.text.toString())

            viewModel.addPost(
                topic = binding.editNewEventTopic.text.toString(),
                location = binding.editNewEventGameLocation.text.toString(),
                rules = binding.editNewEventGameRule.text.toString(),
//                member = memberList,
                type = typeList,
                name = binding.editNewEventGameName.text.toString(),
                limit = binding.editNewEventGameMember.text.toString().toInt()
            )

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