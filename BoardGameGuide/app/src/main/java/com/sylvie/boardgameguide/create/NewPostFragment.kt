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
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.databinding.FragmentNewPostBinding
import com.sylvie.boardgameguide.ext.getVmFactory
import com.sylvie.boardgameguide.util.GetPhoto
import java.io.File


class NewPostFragment : Fragment() {

    val viewModel by viewModels<NewPostViewModel> { getVmFactory() }
    private lateinit var binding: FragmentNewPostBinding

    private val myPermissionsRequestRead = 0
    private val imagesList = mutableListOf<String>()
    private val imageList = mutableListOf<String>()
    private var filePath: String = ""
    var localImageList = mutableListOf<String>()

    // Separate the situation from HomeFragment and from GameFragment
    private var arg: Game? = null
    var event: Event? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewPostBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        arg = NewPostFragmentArgs.fromBundle(requireArguments()).game
        event = NewPostFragmentArgs.fromBundle(requireArguments()).event
        viewModel.game.value = arg
        viewModel.event.value = event

        val gameType = mutableListOf<String>("策略", "陣營", "派對", "主題", "家庭", "戰爭", "益智", "兒童")

        val adapterPlayer = NewPostPlayerAdapter(viewModel)
        val adapterPhoto = NewPostPhotoAdapter()
        val adapterPlayerFilter = NewPostPlayerFilterAdapter(viewModel)
        val adapterGameType = NewPostGameTypeAdapter(viewModel)
        binding.recyclerPlayer.adapter = adapterPlayer
        binding.recyclerNewPostPhoto.adapter = adapterPhoto
        binding.recyclerPlayerFilter.adapter = adapterPlayerFilter
        binding.recyclerGameType.adapter = adapterGameType

        if (viewModel.event.value?.playerList.isNullOrEmpty()) {
            binding.recyclerPlayer.visibility = View.GONE
        } else {
            binding.recyclerPlayer.visibility = View.VISIBLE
        }

        //initialize
        adapterPlayer.submitList(event?.playerList)
        adapterPhoto.submitList(event?.image)
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

        binding.editNewPostGameMember.addTextChangedListener {
            binding.recyclerPlayerFilter.visibility = View.VISIBLE
            viewModel.allUsersData.value?.let { userList ->
                val filterList =
                    viewModel.filter(userList, binding.editNewPostGameMember.text.toString())
                adapterPlayerFilter.submitList(filterList)
            }
        }

        binding.editNewPostGameMember.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.recyclerPlayerFilter.visibility = View.VISIBLE
                viewModel.allUsersData.value?.let { userList ->
                    adapterPlayerFilter.submitList(userList)
                }
            }
        }

        binding.buttonNewPostCreate.setOnClickListener {
            if (viewModel.typeList.value.isNullOrEmpty()) {
                Toast.makeText(context, R.string.enter_game_type, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (filePath == "") {

                var id = ""
                arg?.id?.let { data ->
                    id = data
                }

                var tools = mutableListOf<String>()
                arg?.tools?.let { data ->
                    tools = data
                }

                viewModel.addPost(
                    gameId = id,
                    topic = binding.editNewPostTopic.text.toString(),
                    description = binding.editNewPostDescription.text.toString(),
                    location = binding.editNewPostGameLocation.text.toString(),
                    rules = binding.editNewPostGameRule.text.toString(),
                    member = viewModel.userList.value!!,
                    name = binding.editNewPostGameName.text.toString(),
                    imagesUri = imagesList,
                    tools = tools
                )
            } else {
                for (localImage in localImageList) {
                    uploadPhoto(storageRef, localImage)
                }
            }

        }

        viewModel.focusStatus.observe(viewLifecycleOwner, Observer {
            binding.editNewPostGameMember.clearFocus()
        })

        viewModel.visibilityStatus.observe(viewLifecycleOwner, Observer {
            binding.recyclerPlayerFilter.visibility = View.GONE
        })

        viewModel.userList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it.isNullOrEmpty()) {
                binding.recyclerPlayer.visibility = View.GONE
            } else {
                binding.recyclerPlayer.visibility = View.VISIBLE
            }
            adapterPlayer.submitList(it)
            adapterPlayer.notifyDataSetChanged()
            binding.editNewPostGameMember.text = null
        })


        viewModel.imagesUri.observe(viewLifecycleOwner, Observer {

            if (it.size == localImageList.size) {

                var id = ""
                arg?.id?.let { data ->
                    id = data
                }

                var tools = mutableListOf<String>()
                arg?.tools?.let { data ->
                    tools = data
                }

                viewModel.addPost(
                    gameId = id,
                    topic = binding.editNewPostTopic.text.toString(),
                    description = binding.editNewPostDescription.text.toString(),
                    location = binding.editNewPostGameLocation.text.toString(),
                    rules = binding.editNewPostGameRule.text.toString(),
                    member = viewModel.userList.value!!,
                    name = binding.editNewPostGameName.text.toString(),
                    imagesUri = viewModel.imagesUri.value!!,
                    tools = tools
                )
            }

        })

        viewModel.localImageList.observe(viewLifecycleOwner, Observer {
            adapterPhoto.submitList(it)
            adapterPhoto.notifyDataSetChanged()
        })


        viewModel.eventStatus.observe(viewLifecycleOwner, Observer {
            findNavController().navigate(R.id.action_global_homeFragment)
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
                    viewModel.localImageList.value = localImageList
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

            imageList.add(it.toString())
            viewModel.imagesUri.value = imageList

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