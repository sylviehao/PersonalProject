package com.sylvie.boardgameguide.profile

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
import com.sylvie.boardgameguide.util.GetPhoto.checkPermissionAndGetLocalImg
import java.io.File


class ProfileEditDialog : BottomSheetDialogFragment() {

    private lateinit var binding: DialogEditProfileBinding
    val viewModel by viewModels<ProfileEditViewModel> { getVmFactory() }

    private val myPermissionsRequestRead = 0
    var localImageList = mutableListOf<String>()
    var filePath: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.custom_dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DialogEditProfileBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val user = ProfileEditDialogArgs.fromBundle(requireArguments()).user

        viewModel.user.value = user

        val storage = Firebase.storage
        val storageRef = storage.reference

        binding.buttonSend.setOnClickListener {
            if (!filePath.isBlank()) {

                uploadPhoto(storageRef, filePath)
            } else {
                user.apply {
                    name = binding.editName.text.toString()
                }
                val introduction = binding.editIntro.text.toString()
                viewModel.setUser(user, introduction)
            }
        }

        binding.imageProfile.setOnClickListener {
            context?.let { context ->
                checkPermissionAndGetLocalImg(
                    context,
                    requireActivity(),
                    this
                )
            }
        }

        viewModel.imageUri.observe(viewLifecycleOwner, Observer {
            user.apply {
                name = binding.editName.text.toString()
                image = it
            }
            val introduction = binding.editIntro.text.toString()
            viewModel.setUser(user, introduction)
        })

        viewModel.userData.observe(viewLifecycleOwner, Observer {
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
            myPermissionsRequestRead -> {
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
}