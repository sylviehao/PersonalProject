package com.sylvie.boardgameguide.home.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.databinding.FragmentDetailEventBinding
import com.sylvie.boardgameguide.ext.getVmFactory
import com.sylvie.boardgameguide.game.detail.GameDetailFragmentDirections
import com.sylvie.boardgameguide.home.getTimeDate
import com.sylvie.boardgameguide.login.UserManager
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class DetailEventFragment : Fragment() {

    val viewModel by viewModels<DetailEventViewModel> { getVmFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentDetailEventBinding.inflate(inflater, container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val bundle = DetailEventFragmentArgs.fromBundle(requireArguments()).event
        viewModel.getEventData.value = bundle
        bundle.game?.name?.let { viewModel.getGame(it) }

        bundle.playerList?.let {

            if(it.any { userId-> userId == UserManager.userToken }){
                binding.buttonJoin.setText(R.string.leave)
            }else{
                binding.buttonJoin.setText(R.string.join)
            }
        }

//        binding.textCreatedTime.text = getTimeDate(bundle.createdTime.toDate())

        val adapter = DetailEventPlayerAdapter(viewModel)
        val adapter2 = DetailEventPhotoAdapter(DetailEventPhotoAdapter.OnClickListener{
            findNavController().navigate(R.id.action_global_uploadPhotoDialog)
        }, viewModel)

        binding.recyclerPlayer.adapter = adapter
        binding.recyclerPhoto.adapter = adapter2

        viewModel.getAllUsers()

        viewModel.photoPermission.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
//            adapter2.submitList(viewModel.toPhotoItems(it))
            viewModel.add(bundle.image!!)
        })

        viewModel.newArray.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            adapter2.submitList(viewModel.toPhotoItems(it))
        })

        // upload photo permission
        bundle.playerList?.let { viewModel.checkUserPermission(it) }


        viewModel.getAllUsers.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            adapter.submitList(bundle.playerList)
        })


        binding.buttonJoin.setOnClickListener {
            //判斷是否加入過
            UserManager.user.value?.let { userId ->
                viewModel.setPlayer(userId.id, bundle, true)
                if (bundle.playerList!!.any { it == userId.id }) {
                    bundle.playerList?.remove(userId.id)
                    viewModel.setPlayer(userId.id, bundle, false)
                    binding.buttonJoin.setText(R.string.join)
                    findNavController().navigate(R.id.action_global_deleteDialog)
                } else {
                    bundle.playerList?.add(userId.id)
                    binding.buttonJoin.setText(R.string.leave)
                    findNavController().navigate(R.id.action_global_joinDialog)
                }
                viewModel.getEventData.value = bundle
//                adapter.submitList(bundle.playerList)
                adapter.notifyDataSetChanged()
            }
        }


        binding.textStatus.setOnClickListener {
            findNavController().navigate(DetailEventFragmentDirections.actionGlobalNewPostFragment(viewModel.getGameData.value, bundle))
        }

        val dateString = SimpleDateFormat("MM/dd/yyyy HH:mm").format(Date(bundle.time))
        binding.textGameTime.text = dateString


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