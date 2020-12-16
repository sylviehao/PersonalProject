package com.sylvie.boardgameguide.create

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.databinding.FragmentNewGameBinding
import com.sylvie.boardgameguide.ext.getVmFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_new_post.button_add_photo
import java.io.File

class NewGameFragment : Fragment() {

    val viewModel by viewModels<NewGameViewModel> { getVmFactory() }
    lateinit var binding : FragmentNewGameBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNewGameBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val storage = Firebase.storage
        val storageRef = storage.reference
//        val db = FirebaseFirestore.getInstance()



        binding.buttonAddPhoto.setOnClickListener {
            checkPermission()
//            findNavController().navigate(R.id.action_global_uploadPhotoDialog)
        }


        binding.buttonGameCreate.setOnClickListener {

            if (filePath == ""){

                val typeList= mutableListOf<String>()
                typeList.add(binding.editGameType.text.toString())

                val rolesList= mutableListOf<String>()
                rolesList.add(binding.editGameRoles.text.toString())

                viewModel.addGame(
                    name = binding.editGameName.text.toString(),
                    type = typeList,
                    time = binding.editGameTime.text.toString().toLong(),
                    limit = binding.editPlayerLimit.text.toString().toInt(),
                    rules = binding.editGameRules.text.toString(),
                    roles = rolesList,
                    imagesUri = imagesList
                )
            } else {
                uploadPhoto(storageRef)
            }
        }


        viewModel.imagesUri.observe(viewLifecycleOwner, Observer {
            val typeList= mutableListOf<String>()
            typeList.add(binding.editGameType.text.toString())

            val rolesList= mutableListOf<String>()
            rolesList.add(binding.editGameRoles.text.toString())

            viewModel.addGame(
                name = binding.editGameName.text.toString(),
                type = typeList,
                time = binding.editGameTime.text.toString().toLong(),
                limit = binding.editPlayerLimit.text.toString().toInt(),
                rules = binding.editGameRules.text.toString(),
                roles = rolesList,
                imagesUri = viewModel.imagesUri.value!!
            )

        })


        viewModel.gameStatus.observe(viewLifecycleOwner, Observer {
            findNavController().navigate(R.id.action_global_gameFragment)
        })




        return binding.root
    }

    private fun uploadPhoto(storageRef: StorageReference) {
        val file = Uri.fromFile(File(filePath))
        val eventsRef = storageRef.child(file.lastPathSegment ?: "")

        val metadata = StorageMetadata.Builder()
            .setContentDisposition("game")
            .setContentType("image/jpg")
            .build()

            val uploadTask = eventsRef.putFile(file)
            uploadTask
                .addOnSuccessListener {
                    downloadImg(eventsRef)
                    Log.i("Upload", "Success")
                }
                .addOnFailureListener { exception ->
                    Log.i("Upload", exception.toString())
                }

    }

    val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_CONTACTS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //get image
                } else {
                    Toast.makeText(this.context, "Permission denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }
    var filePath: String = ""
    val imagesList = mutableListOf<String>()
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            Activity.RESULT_OK -> {
                filePath = ImagePicker.getFilePath(data) ?: ""
                if (filePath.isNotEmpty()) {
                    binding.clipCorner.visibility = View.VISIBLE
                    Toast.makeText(this.requireContext(), filePath, Toast.LENGTH_SHORT).show()
                    Glide.with(this.requireContext()).load(filePath).into(binding.imagePhoto)
                } else {
                    Toast.makeText(this.requireContext(), "Upload failed", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            ImagePicker.RESULT_ERROR -> Toast.makeText(
                this.requireContext(),
                ImagePicker.getError(data),
                Toast.LENGTH_SHORT
            ).show()
            else -> Toast.makeText(this.requireContext(), "Task Cancelled", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun checkPermission() {
        val permission = ActivityCompat.checkSelfPermission(
            this.requireContext(),
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            //未取得權限，向使用者要求允許權限
            ActivityCompat.requestPermissions(
                this.requireActivity(), arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                MY_PERMISSIONS_REQUEST_READ_CONTACTS
            )
            getLocalImg()
        }else{
            getLocalImg()
        }

    }

    private fun getLocalImg() {
        ImagePicker.with(this)
            .crop()                    //Crop image(Optional), Check Customization for more option
            .compress(1024)            //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            )    //Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    }

    private fun downloadImg(ref: StorageReference?) {
        if (ref == null) {
            Toast.makeText(this.requireContext(), "No file", Toast.LENGTH_SHORT).show()
            return
        }
        ref.downloadUrl.addOnSuccessListener {

            val imageList = mutableListOf<String>()
            imageList.add(it.toString())
            viewModel.imagesUri.value = imageList


//            Toast.makeText(this.requireContext(), "Success", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
                exception ->
            Toast.makeText(this.requireContext(), exception.message, Toast.LENGTH_SHORT).show()
        }
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