package com.sylvie.boardgameguide.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.firestore.FirebaseFirestore
import com.sylvie.boardgameguide.MainViewModel
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.databinding.FragmentFavoriteBinding
import com.sylvie.boardgameguide.ext.getVmFactory
import com.sylvie.boardgameguide.game.GameAdapter

class FavoriteFragment : Fragment() {

    val viewModel by viewModels<FavoriteViewModel> { getVmFactory() }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFavoriteBinding.inflate(inflater, container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val adapter = FavoriteAdapter()
        binding.recyclerFavorite.adapter = adapter

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

        return binding.root
    }
}