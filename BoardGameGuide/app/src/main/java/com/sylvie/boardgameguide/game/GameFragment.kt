package com.sylvie.boardgameguide.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.sylvie.boardgameguide.MainViewModel
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.data.Message
import com.sylvie.boardgameguide.databinding.FragmentGameBinding
import com.sylvie.boardgameguide.ext.getVmFactory
import com.sylvie.boardgameguide.home.HomeAdapter
import com.sylvie.boardgameguide.home.HomeFragmentDirections
import me.samlss.bloom.Bloom
import me.samlss.bloom.effector.BloomEffector
import me.samlss.bloom.shape.distributor.ParticleShapeDistributor
import me.samlss.bloom.shape.distributor.StarShapeDistributor

class GameFragment : Fragment() {

    val viewModel by viewModels<GameViewModel> { getVmFactory() }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentGameBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val adapter = GameAdapter(GameAdapter.OnClickListener {
            viewModel.navigateToDetail(it)
        })

        binding.recyclerGame.adapter = adapter

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                    findNavController().navigate(GameFragmentDirections.actionGlobalGameDetailFragment(it))
                viewModel.onDetailNavigated()
            }
        })

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

        binding.fab.setOnClickListener {
            val data = Game(
                id = "005",
                name = "卡坦島",
                image = mutableListOf("https://i.pinimg.com/originals/84/a7/1f/84a71faae084bf5a6c532902e52d64b4.jpg"),
                type = mutableListOf("策略"),
                playerLimit = 4,
                time = 120,
                rules = "卡坦島(Catan)桌遊裡，玩家扮演卡坦島的新移民者，要拓荒開墾自己的領地。" +
                        "玩家輪流擲骰子決定哪個板塊可以生產資源，因此加入了一點機率和運氣成份。" +
                        "透過在不同的板塊取得的資源，玩家可以建造村莊和道路。當村莊數量越多，就可以從板塊收成越多的資源。" +
                        "玩家也可以和其他玩家交易，或是買發展卡來獲取額外的資源和機會",
                roles = mutableListOf("玩家本身")
            )

            // Add a new document with a generated ID
            db.collection("Game")
                .add(data)
                .addOnSuccessListener { documentReference ->
                    documentReference.update("id", documentReference.id)
                    Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference}")
                }
                .addOnFailureListener {
                    Log.d("TAG", "$it")
                    Toast.makeText(this.context, "Please sign in to post", Toast.LENGTH_SHORT)
                        .show()
                }
        }


        return binding.root
    }

    private var mBloom: Bloom? = null

    fun boom(view: View) {
        val shapeDistributor: ParticleShapeDistributor = StarShapeDistributor()

        mBloom?.cancel()
        mBloom = Bloom.with(requireActivity())
            .setParticleRadius(15F)
            .setShapeDistributor(shapeDistributor)
            .setEffector(
                BloomEffector.Builder()
                    .setDuration(2000)
                    .setScaleRange(0.5f, 1.5f)
                    .setRotationSpeedRange(0.01f, 0.05f)
                    .setSpeedRange(0.1f, 0.5f)
                    .setAcceleration(0.00025f, 90)
                    .setAnchor((view.getWidth() / 2).toFloat(), view.getHeight().toFloat())
                    .setFadeOut(500, AccelerateInterpolator())
                    .build())
        mBloom?.boom(view)
    }
}