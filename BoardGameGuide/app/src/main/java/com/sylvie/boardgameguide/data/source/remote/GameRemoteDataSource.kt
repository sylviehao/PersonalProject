package com.sylvie.boardgameguide.data.source.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.data.source.GameDataSource
import kotlin.coroutines.suspendCoroutine
import com.sylvie.boardgameguide.data.Result
import kotlin.coroutines.resume

object GameRemoteDataSource : GameDataSource {
    override suspend fun getEvents(id: String): Result<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun getGame(id : String): Result<List<Game>> = suspendCoroutine{ continuation ->
        FirebaseFirestore.getInstance()
            .collection("Game")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<Game>()
                    for (document in task.result!!) {
                        val game = document.toObject(Game::class.java)
                        list.add(game)
                    }
                    continuation.resume(Result.Success(list))
                } else {
                    task.exception?.let {
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                }
            }
    }


}