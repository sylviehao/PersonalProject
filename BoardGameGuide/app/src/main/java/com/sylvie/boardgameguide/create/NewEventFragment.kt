package com.sylvie.boardgameguide.create

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
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
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.databinding.FragmentNewEventBinding
import com.sylvie.boardgameguide.ext.getVmFactory
import com.sylvie.boardgameguide.util.GetPhoto
import java.io.File

class NewEventFragment : Fragment() {

    val viewModel by viewModels<NewEventViewModel> { getVmFactory() }
    private lateinit var binding: FragmentNewEventBinding

    private val myPermissionsRequestRead = 0
    private val imageList = mutableListOf<String>()
    var localImageList = mutableListOf<String>()
    var filePath: String = ""

    // Separate the situation from HomeFragment and from GameFragment
    var arg: Game? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewEventBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val storage = Firebase.storage
        val storageRef = storage.reference

        val gameType = mutableListOf<String>("策略" ,"陣營", "派對", "主題", "家庭", "戰爭", "益智", "兒童")

        val adapter = NewPostPhotoAdapter()
        val adapter2 = NewEventGameTypeAdapter(viewModel)

        binding.recyclerNewPostPhoto.adapter = adapter
        binding.recyclerGameType.adapter = adapter2

        adapter2.submitList(gameType)

        arg = NewEventFragmentArgs.fromBundle(requireArguments()).game
        viewModel.game.value = arg

        binding.buttonAddPhoto.setOnClickListener {
            context?.let { context ->
                GetPhoto.checkPermissionAndGetLocalImg(
                    context,
                    requireActivity(),
                    this
                )
            }
        }

        binding.editNewEventGameTime.setOnClickListener {
            SingleDateAndTimePickerDialog.Builder(context)
                .bottomSheet()
                .curved()
                .backgroundColor(resources.getColor(R.color.oliveGreen))
                .mainColor(Color.WHITE)
                .titleTextColor(Color.WHITE)
                .displayListener {}
                .title("Simple")
                .listener { date ->
                    binding.editNewEventGameTime.text = date.toString()
                    viewModel.date.value = date.time
                }
                .display()
        }

        binding.buttonNewEventCreate.setOnClickListener {
            if(viewModel.typeList.value.isNullOrEmpty()) {
                Toast.makeText(context, "請填寫遊戲種類", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (filePath == "") {

                var id = ""
                arg?.id?.let { data ->
                    id = data
                }

                var tool = mutableListOf<String>()
                arg?.tools?.let { data ->
                    tool = data
                }

                viewModel.addPost(
                    gameId = id,
                    topic = binding.editNewEventTopic.text.toString(),
                    description = binding.editNewEventDescription.text.toString(),
                    location = binding.editNewEventGameLocation.text.toString(),
                    rules = binding.editNewEventGameRule.text.toString(),
                    name = binding.editNewEventGameName.text.toString(),
                    limit = binding.editNewEventGameMember.text.toString(),
                    imagesUri = mutableListOf(),
                    tools = tool
                )

            } else {
                for(localImage in localImageList) {
                    uploadPhoto(storageRef, localImage)
                }
            }

        }

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
                    topic = binding.editNewEventTopic.text.toString(),
                    description = binding.editNewEventDescription.text.toString(),
                    location = binding.editNewEventGameLocation.text.toString(),
                    rules = binding.editNewEventGameRule.text.toString(),
                    name = binding.editNewEventGameName.text.toString(),
                    limit = binding.editNewEventGameMember.text.toString(),
                    imagesUri = viewModel.imagesUri.value!!,
                    tools = tools
                )
            }
        })

        viewModel.localImageList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
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