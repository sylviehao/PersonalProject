package com.sylvie.boardgameguide.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.databinding.FragmentNewGameBinding
import com.sylvie.boardgameguide.ext.getVmFactory
import kotlinx.android.synthetic.main.activity_main.*

class NewGameFragment : Fragment() {

    val viewModel by viewModels<NewGameViewModel> { getVmFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentNewGameBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        val db = FirebaseFirestore.getInstance()

        
        binding.buttonAddPhoto.setOnClickListener {
            findNavController().navigate(R.id.action_global_uploadPhotoDialog)
        }


        binding.buttonGameCreate.setOnClickListener {

            val typeList= mutableListOf<String>()
            typeList.add(binding.editGameType.text.toString())

            val rolesList= mutableListOf<String>()
            rolesList.add(binding.editGameRoles.text.toString())

            viewModel.addGame(
                name = binding.editGameName.text.toString(),
                type = typeList,
                time = binding.editGameTime.text.toString().toLong(),
                limit = binding.editPlayerLimit.text.toString().toInt(),
                rules = binding.editGameRules.text.toString(),
                roles = rolesList
            )

            findNavController().navigate(R.id.action_global_gameFragment)
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        let {
            requireActivity().toolbar.visibility = View.GONE
            requireActivity().bottomNavView.visibility = View.GONE
        }
    }

    override fun onStop() {
        super.onStop()
        let {
            requireActivity().toolbar.visibility = View.VISIBLE
            requireActivity().bottomNavView.visibility = View.VISIBLE
        }
    }
}