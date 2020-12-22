package com.sylvie.boardgameguide.dialog

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
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.sylvie.boardgameguide.NavigationDirections
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.databinding.DialogEditProfileBinding
import com.sylvie.boardgameguide.ext.getVmFactory
import com.sylvie.boardgameguide.login.UserManager
import java.io.File


class ProfileEditDialog : BottomSheetDialogFragment() {

    private lateinit var binding: DialogEditProfileBinding
    val viewModel by viewModels<ProfileEditViewModel> { getVmFactory() }

    private val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0
    var localImageList = mutableListOf<String>()
    var filePath: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.custom_dialog)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DialogEditProfileBinding.inflate(inflater, container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val user = ProfileEditDialogArgs.fromBundle(requireArguments()).user

        val storage = Firebase.storage
        val storageRef = storage.reference

        binding.buttonSend.setOnClickListener {
            uploadPhoto(storageRef,filePath)
        }

        binding.imageProfile.setOnClickListener {
            checkPermission()
        }

        viewModel.imageUri.observe(viewLifecycleOwner, Observer {
            user.apply {
                name = binding.editName.text.toString()
                image = it
            }
            val introduction = binding.editIntro.text.toString()
            viewModel.setUser(user, introduction)
        })

        viewModel.setUserData.observe(viewLifecycleOwner, Observer { 
            it?.let {
                findNavController().navigate(NavigationDirections.actionGlobalProfileFragment(it.id))
            }
        })


        return binding.root
    }


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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            Activity.RESULT_OK -> {
                filePath = ImagePicker.getFilePath(data) ?: ""
                if (filePath.isNotEmpty()) {
                    localImageList.add(filePath)
                    Glide.with(this.requireContext()).load(filePath).into(binding.imageProfile)
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

    private fun downloadImg(ref: StorageReference?) {
        if (ref == null) {
            Toast.makeText(this.requireContext(), "No file", Toast.LENGTH_SHORT).show()
            return
        }
        ref.downloadUrl.addOnSuccessListener {

            viewModel.imageUri.value = it.toString()

        }.addOnFailureListener { exception ->
            Toast.makeText(this.requireContext(), exception.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadPhoto(storageRef: StorageReference, localImage: String) {
        val file = Uri.fromFile(File(localImage))
        val eventsRef = storageRef.child(file.lastPathSegment ?: "")

//        val metadata = StorageMetadata.Builder()
//            .setContentDisposition("game")
//            .setContentType("image/jpg")
//            .build()

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

    private fun checkPermission() {
        val permission = ActivityCompat.checkSelfPermission(
            this.requireContext(),
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            //if not having permission, ask for it
            ActivityCompat.requestPermissions(
                this.requireActivity(), arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                MY_PERMISSIONS_REQUEST_READ_CONTACTS
            )
            getLocalImg()
        } else {
            getLocalImg()
        }

    }

    private fun getLocalImg() {
        ImagePicker.with(this)
            //Crop image(Optional), Check Customization for more option
            .crop()
            //Final image size will be less than 1 MB(Optional)
            .compress(1024)
            //Final image resolution will be less than 1080 x 1080(Optional)
            .maxResultSize(
                1080,
                1080
            )
            .start()
    }


}