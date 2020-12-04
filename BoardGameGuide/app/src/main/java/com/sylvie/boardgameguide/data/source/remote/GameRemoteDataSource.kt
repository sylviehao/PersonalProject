package com.sylvie.boardgameguide.data.source.remote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import bolts.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.sylvie.boardgameguide.data.*
import com.sylvie.boardgameguide.data.source.GameDataSource
import kotlin.coroutines.suspendCoroutine
import kotlin.coroutines.resume

object GameRemoteDataSource : GameDataSource {

    override suspend fun getHome(): Result<List<HomeItem>> =

        suspendCoroutine { continuation ->
            FirebaseFirestore.getInstance()
                .collection("Event")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val list = mutableListOf<Event>()

                        val event1 = task.result?.toObjects(EventResult::class.java)
                        for (document in task.result!!) {
                            val event = document.toObject(Event::class.java)
                            list.add(event)
                        }
                        var a = EventResult(
                            list
                        )
                        Log.i("getHome","${a}")
                        continuation.resume(Result.Success(a.toHomeItems()))
                    } else {
                        task.exception?.let {
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                    }
                }
        }


    override fun getEvents(): MutableLiveData<List<Event>> {

        val liveData = MutableLiveData<List<Event>>()

        FirebaseFirestore.getInstance()
            .collection("Event")
            .orderBy("createdTime", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                value?.let {
                    val listResult = mutableListOf<Event>()
                    it.forEach { data ->
                        val d = data.toObject(Event::class.java)
                        listResult.add(d)
                    }
                    liveData.value = listResult
                }
            }
        return liveData
    }


        override suspend fun getGame(id: String): Result<List<Game>> =
            suspendCoroutine { continuation ->
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


    override suspend fun getUser(id: String): Result<User> =
        suspendCoroutine { continuation ->
            FirebaseFirestore.getInstance()
                .collection("User").document(id)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
//                        val list = mutableListOf<Game>()
//                        for (document in task.result!!) {
//                            val game = document.toObject(Game::class.java)
//                            list.add(game)
//                        }
                        val user = task.result?.toObject(User::class.java)
                        continuation.resume(Result.Success(user!!))
                    } else {
                        task.exception?.let {
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                    }
                }
        }
}