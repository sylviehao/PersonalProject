package com.sylvie.boardgameguide.data.source.remote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.sylvie.boardgameguide.data.*
import com.sylvie.boardgameguide.data.source.GameDataSource
import kotlin.coroutines.suspendCoroutine
import kotlin.coroutines.resume

object GameRemoteDataSource : GameDataSource {

    override suspend fun createUser(user: User): Result<Boolean> =
        suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance().collection("User")
            val document = db.document(user.id)
            document
                .set(user)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(""))
                    }
                }
        }

    override fun getAllUsers(): MutableLiveData<List<User>> {
            val liveData = MutableLiveData<List<User>>()
            FirebaseFirestore.getInstance()
                .collection("User")
                .addSnapshotListener { value, error ->
                    value?.let {
                        val listResult = mutableListOf<User>()
                        it.forEach { data ->
                            val user = data.toObject(User::class.java)
                            listResult.add(user)
                        }
                        liveData.value = listResult
                    }
                }
            return liveData
        }

    override suspend fun getUser(id: String): Result<User?> =
        suspendCoroutine { continuation ->
            FirebaseFirestore.getInstance()
                .collection("User").document(id)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user  = task.result?.toObject(User::class.java)
                        continuation.resume(Result.Success(user))
                    } else {
                        task.exception?.let {
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                    }
                }
        }

    override suspend fun setUser(user: User, introduction: String): Result<User> =
        suspendCoroutine { continuation ->
            user.introduction = introduction
            FirebaseFirestore.getInstance()
                .collection("User").document(user.id)
                .set(user)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.Success(user))
                    } else {
                        task.exception?.let {
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                    }
                }
        }

    override suspend fun getHome(): Result<List<HomeItem>> =
        suspendCoroutine { continuation ->
            FirebaseFirestore.getInstance()
                .collection("Event")
                .orderBy("createdTime", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val list = mutableListOf<Event>()
                        val eventList = task.result?.toObjects(EventResult::class.java)
                        for (document in task.result!!) {
                            val event = document.toObject(Event::class.java)
                            list.add(event)
                        }
                        val a = EventResult(
                            list
                        )
                        Log.i("getHome", "$a")
                        continuation.resume(Result.Success(a.toHomeItems()))
                    } else {
                        task.exception?.let {
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                    }
                }
        }

    override fun getEvents(status: String): MutableLiveData<List<Event>> {

        val liveData = MutableLiveData<List<Event>>()
        val db = FirebaseFirestore.getInstance()
            .collection("Event")
                when(status){
                    "OPEN"->db.whereEqualTo("status", status)
                    "CLOSE"->db.whereEqualTo("status", status)
                    else -> db
                }
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

    override suspend fun getEvent(id: String): Result<Event?> =
        suspendCoroutine { continuation ->
            FirebaseFirestore.getInstance()
                .collection("Event").document(id)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val event  = task.result?.toObject(Event::class.java)
                        continuation.resume(Result.Success(event))
                    } else {
                        task.exception?.let {
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                    }
                }
        }

    override suspend fun setLike(userId: String, event: Event, status: Boolean): Result<Boolean> =
        suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance().collection("Event")
            val document = db.document(event.id)
            if (status) {
                document.update("like", FieldValue.arrayUnion(userId))
            } else {
                document.update("like", FieldValue.arrayRemove(userId))
            }
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                    }
                }
        }

    override suspend fun setMessage(message: Message, event: Event): Result<Boolean> =
        suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance().collection("Event")
            val document = db.document(event.id)
                document.update("message", FieldValue.arrayUnion(message))
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                    }
                }
        }

    override suspend fun addPhoto(image: String, eventId: String, status: Boolean): Result<Boolean> =
        suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance().collection("Event")
            val document = db.document(eventId)
            if (status) {
                document.update("image", FieldValue.arrayUnion(image))
            } else {
                document.update("image", FieldValue.arrayRemove(image))
            }
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                    }
                }
        }

    override suspend fun setPlayer(userId: String, event: Event, status: Boolean): Result<Boolean> =
        suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance().collection("Event")
            val document = db.document(event.id)
            if (status) {
                document.update("playerList", FieldValue.arrayUnion(userId))
            } else {
                document.update("playerList", FieldValue.arrayRemove(userId))
            }
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                    }
                }
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

    override suspend fun getAllGames(): Result<List<Game>> =
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

    override suspend fun setGame(user: User, game: Game): Result<Boolean> =
        suspendCoroutine { continuation ->
            user.favorite?.add(game)

            FirebaseFirestore.getInstance()
                .collection("User").document(user.id)
                .set(user)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                    }
                }
        }

    override suspend fun removeGame(user: User, game: Game): Result<Boolean> =
        suspendCoroutine { continuation ->
            user.favorite = user.favorite?.filter { it.id != game.id } as MutableList<Game>?
            FirebaseFirestore.getInstance()
                .collection("User").document(user.id)
                .set(user)
                .addOnCompleteListener { task ->
                    continuation.resume(Result.Success(true))
                }.addOnFailureListener {
                    continuation.resume(Result.Error(it))
                }
        }

    override suspend fun addEvent(event: Event): Result<Boolean> =
        suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance().collection("Event")
            val document = db.document()
            event.id = document.id
            document.set(event)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(""))
                    }
                }
        }

    override suspend fun addGame(game: Game): Result<Boolean> =
        suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance().collection("Game")
            val document = db.document()
            game.id = document.id
            document.set(game)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail(""))
                    }
                }
        }

    override suspend fun setBrowseRecently(userId: String, gameId: BrowseRecently): Result<Boolean> =
        suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance().collection("User")
            val document = db.document(userId)
                document.update("browseRecently", FieldValue.arrayUnion(gameId))
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                    }
                }
        }

    override suspend fun getBrowseRecently(userId: String, gamesId: List<String>): Result<List<Game>> =
        suspendCoroutine { continuation ->
            FirebaseFirestore.getInstance().collection("Game")
                .whereIn(FieldPath.documentId(),gamesId)
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