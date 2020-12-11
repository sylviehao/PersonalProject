package com.sylvie.boardgameguide.create

import android.graphics.Color
import android.os.Bundle
import com.sylvie.boardgameguide.login.UserManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.data.Message
import com.sylvie.boardgameguide.databinding.FragmentNewPostBinding
import com.sylvie.boardgameguide.ext.getVmFactory
import com.sylvie.boardgameguide.home.detail.DetailPostPhotoAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class NewPostFragment : Fragment() {


    private lateinit var binding: FragmentNewPostBinding
    val viewModel by viewModels<NewPostViewModel> { getVmFactory() }

    // Separate the situation from HomeFragment and from GameFragment
    var arg: Game? = null
    var event: Event? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewPostBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val adapter = NewPostPlayerAdapter()
        val adapter2 = NewPostPhotoAdapter()
        binding.recyclerPlayer.adapter = adapter
        binding.recyclerNewPostPhoto.adapter = adapter2


        arg = NewPostFragmentArgs.fromBundle(requireArguments()).game
        event = NewPostFragmentArgs.fromBundle(requireArguments()).event

        viewModel.game.value = arg
        viewModel.event.value = event

        //??
        adapter.submitList(event?.playerList)
        adapter2.submitList(event?.image)

        binding.buttonAddPhoto.setOnClickListener {
            findNavController().navigate(R.id.action_global_uploadPhotoDialog)
        }

        binding.editNewPostGameTime.setOnClickListener {
            SingleDateAndTimePickerDialog.Builder(context)
                .bottomSheet()
                .curved()
                .backgroundColor(resources.getColor(R.color.oliveGreen))
                .mainColor(Color.WHITE)
                .titleTextColor(Color.WHITE)
                //.stepSizeMinutes(15)
                //.todayText("aujourd'hui")
                .displayListener {}
                .title("Simple")
                .listener { date ->
                    binding.editNewPostGameTime.text = date.toString()
                    viewModel.date.value = date.time
                }
                .display()
        }

        binding.buttonNewPostCreate.setOnClickListener {

            val typeList= mutableListOf<String>()
            typeList.add(binding.editNewPostGameType.text.toString())

            val memberList = mutableListOf<String>()
            memberList.add(binding.editNewPostGameMember.text.toString())

            viewModel.addPost(
                topic = binding.editNewPostTopic.text.toString(),
                location = binding.editNewPostGameLocation.text.toString(),
                rules = binding.editNewPostGameRule.text.toString(),
                member = memberList,
                type = typeList,
                name = binding.editNewPostGameName.text.toString()
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