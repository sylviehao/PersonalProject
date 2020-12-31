package com.sylvie.boardgameguide.create

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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.databinding.FragmentNewGameBinding
import com.sylvie.boardgameguide.ext.getVmFactory
import com.sylvie.boardgameguide.util.GetPhoto
import java.io.File

class NewGameFragment : Fragment() {

    val viewModel by viewModels<NewGameViewModel> { getVmFactory() }
    lateinit var binding : FragmentNewGameBinding
    private val myPermissionsRequestRead = 0
    private val imagesList = mutableListOf<String>()
    private val imageList = mutableListOf<String>()
    var localImageList = mutableListOf<String>()
    var filePath: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNewGameBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val toolsType = mutableListOf<String>("Dice" ,"Timer", "Picker")
        val gameType = mutableListOf<String>("策略" ,"陣營", "派對", "主題", "家庭", "戰爭", "益智", "兒童")
        val adapterTool = NewGameToolAdapter(viewModel)
        val adapterGameType = NewGameTypeAdapter(viewModel)
        binding.recyclerTools.adapter = adapterTool
        binding.recyclerGameType.adapter = adapterGameType

        adapterTool.submitList(toolsType)
        adapterGameType.submitList(gameType)

        val storage = Firebase.storage
        val storageRef = storage.reference

        binding.buttonAddPhoto.setOnClickListener {
            context?.let { context ->
                GetPhoto.checkPermissionAndGetLocalImg(
                    context,
                    requireActivity(),
                    this
                )
            }
        }

        binding.buttonGameCreate.setOnClickListener {

            if(viewModel.typeList.value.isNullOrEmpty()) {
                Toast.makeText(context, R.string.enter_game_type, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (filePath == "") {
                val rolesList= mutableListOf<String>()
                rolesList.add(binding.editGameRoles.text.toString())

                viewModel.addGame(
                    name = binding.editGameName.text.toString(),
                    time = binding.editGameTime.text.toString(),
                    limit = binding.editPlayerLimit.text.toString(),
                    rules = binding.editGameRules.text.toString(),
                    roles = rolesList,
                    imagesUri = imagesList
                )
            } else {
                uploadPhoto(storageRef)
            }
        }

        viewModel.imagesUri.observe(viewLifecycleOwner, Observer {

            val rolesList= mutableListOf<String>()
            rolesList.add(binding.editGameRoles.text.toString())

            viewModel.addGame(
                name = binding.editGameName.text.toString(),
                time = binding.editGameTime.text.toString(),
                limit = binding.editPlayerLimit.text.toString(),
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
                    binding.clipCorner.visibility = View.VISIBLE
                    localImageList.add(filePath)
                    viewModel.localImageList.value = localImageList
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

    private fun uploadPhoto(storageRef: StorageReference) {
        val file = Uri.fromFile(File(filePath))
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

    private fun downloadImg(ref: StorageReference?) {
        if (ref == null) {
            Toast.makeText(this.requireContext(), "No file", Toast.LENGTH_SHORT).show()
            return
        }
        ref.downloadUrl.addOnSuccessListener {
            imageList.add(it.toString())
            viewModel.imagesUri.value = imageList
        }.addOnFailureListener {
                exception ->
            Toast.makeText(this.requireContext(), exception.message, Toast.LENGTH_SHORT).show()
        }
    }
}