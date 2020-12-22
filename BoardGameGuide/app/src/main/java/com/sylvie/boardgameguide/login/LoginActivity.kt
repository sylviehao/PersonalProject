package com.sylvie.boardgameguide.login

import android.widget.TextView
import com.sylvie.boardgameguide.data.Spark
import com.sylvie.boardgameguide.databinding.ActivityLoginBinding
import com.sylvie.boardgameguide.ext.getVmFactory
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sylvie.boardgameguide.MainActivity
import com.sylvie.boardgameguide.MainViewModel
import com.sylvie.boardgameguide.R


private const val RC_SIGN_IN = 20
class LoginActivity : AppCompatActivity() {
    private val TAG = this.javaClass.name
    private lateinit var spark: Spark
    private lateinit var binding: ActivityLoginBinding

    // FirebaseAuth
    private lateinit var auth: FirebaseAuth
    val viewModel by viewModels<LoginViewModel> { getVmFactory() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

//        spark = Spark(binding.root, Spark.ANIM_YELLOW_BLUE, 4000)
//        spark.startAnimation()


        // Initialize Firebase Auth
        auth = Firebase.auth
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        binding.buttonLogin.setOnClickListener {
            signIn(mGoogleSignInClient)
        }

        viewModel.getUserData.observe(this, Observer {
            viewModel.createUser()
        })

        viewModel.status.observe(this, Observer {
            startActivity(Intent(this, MainActivity::class.java))
        })


    }

    private fun signIn(mGoogleSignInClient: GoogleSignInClient) {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, intent to main activity with the signed-in user's information

                val user = auth.currentUser

                if (user != null) {
                    UserManager.userToken = user.uid
                    viewModel.firebaseUser.value = user
                    viewModel.getUser(user.uid)
                }
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG,"signInWithCredential:failure", task.exception)
            }
        }
    }

    private fun paddingPicture(tv: TextView, pic: Int) {
        val drawable = resources.getDrawable(pic)
        drawable.setBounds(0, 0, 50, 50)
        tv.setCompoundDrawables(drawable, null, null, null)
    }
}

