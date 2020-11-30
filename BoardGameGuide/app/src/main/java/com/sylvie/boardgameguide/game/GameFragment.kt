package com.sylvie.boardgameguide.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.data.Message
import com.sylvie.boardgameguide.databinding.FragmentGameBinding

class GameFragment : Fragment() {

    private val viewModel = GameViewModel()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentGameBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val adapter = GameAdapter()
        binding.recyclerGame.adapter = adapter


        val db = FirebaseFirestore.getInstance()
        //即時監聽資料庫是否變動
        db.collection("Game").addSnapshotListener { value, error ->
            value?.let {
                val listResult = mutableListOf<Game>()
                it.forEach { data ->
                    val d = data.toObject(Game::class.java)
                    listResult.add(d)
                    Log.i("REALTIMETAG", "${data.data}")
                }
                adapter.submitList(listResult)
            }
        }

        binding.fab.setOnClickListener {
            val data = Game(
                id = "004",
                name = "機密代號",
                image = mutableListOf("https://lh3.googleusercontent.com/proxy/kE2pddFEh5fTehtf2zpxObqJVA6QW2cXc-0JgWeFpZ3K-tL-6asVFG4xJmAPelv1HRp5UnIv6HP5Ogsoxv97Xm9TOSNU7avbnSVKvzHFmYTj-9Q6Es7UHCXlRgUu2KLJHpk47szrXmlN3dDbJfjiSfJM0RZg06W82scSeV_LZTKbTzVNADDoLEuI1c9pDsvJllFxT32PKGKaUjpxUW3cAIryTERGZJbyA8Y"),
                type = mutableListOf("派對", "陣營", "猜謎"),
                playerLimit = 8,
                time = 15,
                rules = "桌遊中玩家會分成兩隊，目標是要找出自己陣營的特務。每個特務會對應到一個代號，" +
                        "隊長要想出和代號相關的線索給隊員，讓隊員能夠透過線索找出同隊的特務卡，" +
                        "最先把同隊特務卡全部找出來的隊伍獲勝。桌遊的困難之處在隊長提供線索時，要避掉對方的特務卡和殺手卡，" +
                        "還要盡可能的將線索連結到好幾個特務代號上。整場桌遊的關鍵在隊長如何想出神來一筆的共通線索，" +
                        "快速找到特務卡。",
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
}