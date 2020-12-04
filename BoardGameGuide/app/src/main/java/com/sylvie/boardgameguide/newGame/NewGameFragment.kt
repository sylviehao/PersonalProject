package com.sylvie.boardgameguide.newGame

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.databinding.FragmentNewGameBinding
import kotlinx.android.synthetic.main.activity_main.*

class NewGameFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentNewGameBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        val db = FirebaseFirestore.getInstance()

        
        binding.buttonAddPhoto.setOnClickListener {
            findNavController().navigate(R.id.action_global_uploadPhotoDialog)
        }


        binding.buttonGameCreate.setOnClickListener {
//            val data = Game(
//                id = "005",
//                name = "卡坦島",
//                image = mutableListOf("https://i.pinimg.com/originals/84/a7/1f/84a71faae084bf5a6c532902e52d64b4.jpg"),
//                type = mutableListOf("策略"),
//                playerLimit = 4,
//                time = 120,
//                rules = "卡坦島(Catan)桌遊裡，玩家扮演卡坦島的新移民者，要拓荒開墾自己的領地。" +
//                        "玩家輪流擲骰子決定哪個板塊可以生產資源，因此加入了一點機率和運氣成份。" +
//                        "透過在不同的板塊取得的資源，玩家可以建造村莊和道路。當村莊數量越多，就可以從板塊收成越多的資源。" +
//                        "玩家也可以和其他玩家交易，或是買發展卡來獲取額外的資源和機會",
//                roles = mutableListOf("玩家本身")
//            )
//
//            // Add a new document with a generated ID
//            db.collection("Game")
//                .add(data)
//                .addOnSuccessListener { documentReference ->
//                    documentReference.update("id", documentReference.id)
//                    Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference}")
//                }
//                .addOnFailureListener {
//                    Log.d("TAG", "$it")
//                    Toast.makeText(this.context, "Please sign in to post", Toast.LENGTH_SHORT)
//                        .show()
//                }
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