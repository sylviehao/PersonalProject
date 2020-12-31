package com.sylvie.boardgameguide.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.databinding.FragmentGameBinding
import com.sylvie.boardgameguide.ext.getVmFactory
import com.sylvie.boardgameguide.util.Util.boom

class GameFragment : Fragment() {

    val viewModel by viewModels<GameViewModel> { getVmFactory() }
    lateinit var binding: FragmentGameBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = GameAdapter(GameAdapter.OnClickListener {
            viewModel.navigateToDetail(it)
        }, viewModel)

        binding.recyclerGame.adapter = adapter
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_global_newGameFragment)
        }

        binding.searchView.addTextChangedListener {
            viewModel.gameData.value?.let { gameList ->
                val filterList = viewModel.filter(gameList, binding.searchView.text.toString())
                adapter.submitList(filterList)
            }
        }

        viewModel.detailNavigation.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(
                    GameFragmentDirections.actionGlobalGameDetailFragment(it)
                )
                viewModel.onDetailNavigated()
            }
        })

        viewModel.boomStatus.observe(viewLifecycleOwner, Observer {
            boom(it, requireActivity())
        })

        viewModel.userData.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.getAllGames()
            }
        })

        viewModel.gameData.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
                adapter.notifyDataSetChanged()
            }
        })

        return binding.root
    }
}