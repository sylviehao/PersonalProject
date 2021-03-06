package com.sylvie.boardgameguide.home.detail

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
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.Message
import com.sylvie.boardgameguide.data.User
import com.sylvie.boardgameguide.databinding.FragmentDetailPostBinding
import com.sylvie.boardgameguide.ext.getVmFactory
import com.sylvie.boardgameguide.login.UserManager
import com.sylvie.boardgameguide.util.GetPhoto
import com.sylvie.boardgameguide.util.Util.getTimeDate
import java.io.File

class DetailPostFragment : Fragment() {

    val viewModel by viewModels<DetailPostViewModel> { getVmFactory() }
    private lateinit var binding: FragmentDetailPostBinding
    private val myPermissionsRequestRead = 0
    var localImageList = mutableListOf<String>()
    var filePath: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailPostBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val bundle = DetailPostFragmentArgs.fromBundle(requireArguments()).event
        viewModel.eventData.value = bundle
        viewModel.getEvent(bundle.id)

        val storage = Firebase.storage
        val storageRef = storage.reference

        val adapterComment = DetailPostCommentAdapter(viewModel)
        val adapterPlayer = DetailPostPlayerAdapter(viewModel)
        val adapterPhoto = DetailPostPhotoAdapter(DetailPostPhotoAdapter.OnClickListener {
            context?.let { context ->
                GetPhoto.checkPermissionAndGetLocalImg(
                    context,
                    requireActivity(),
                    this
                )
            }
        }, viewModel)
        binding.recyclerComment.adapter = adapterComment
        binding.recyclerPhoto.adapter = adapterPhoto
        binding.recyclerPlayer.adapter = adapterPlayer
        binding.textCreatedTime.text = getTimeDate(bundle.createdTime.toDate())

        viewModel.imagesUri.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            viewModel.addPhoto(
                eventId = bundle.id,
                image = viewModel.imagesUri.value!!,
                status = true
            )
        })

        viewModel.eventData2.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it.let {
                adapterPhoto.submitList(viewModel.toPhotoItems(viewModel.addImages(it.image!!)))
            }
        })

        viewModel.photoStatus.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            viewModel.getEvent(bundle.id)
        })

        // upload photo permission
        bundle.playerList?.let { viewModel.checkUserPermission(it) }

        viewModel.allUsersData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            adapterPlayer.submitList(bundle.playerList)
        })

        viewModel.localImageList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            uploadPhoto(storageRef)
        })

        viewModel.playerNavigation.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                findNavController().navigate(
                    DetailPostFragmentDirections.actionGlobalProfileFragment(it)
                )
                viewModel.navigated()
            }
        })

        //Get events snapshot
        viewModel.allEvents.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let { eventList ->
                viewModel.filterMessage(eventList, bundle.id)
            }
        })

        //Filter message snapshot
        viewModel.messages.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                adapterComment.submitList(it[0].message)
                adapterComment.notifyDataSetChanged()
            }
        })

        binding.buttonSend.setOnClickListener {
            val data = Message(
                hostId = viewModel.userData.value!!.id,
                userName = UserManager.user.value?.name,
                message = binding.editComment.text.toString()
            )
            viewModel.setMessage(data, bundle)
            binding.editComment.text = null
        }

        binding.imageHost.setOnClickListener {
            findNavController().navigate(
                DetailPostFragmentDirections.actionGlobalProfileFragment(bundle.user!!.id)
            )
        }

        binding.textHostName.setOnClickListener {
            findNavController().navigate(
                DetailPostFragmentDirections.actionGlobalProfileFragment(bundle.user!!.id)
            )
        }


        binding.buttonSortDown.setOnClickListener {
            showGameInfo(it)
        }

        binding.icLike.setOnClickListener {
            UserManager.user.value?.let { user ->
                changeLikeStatus(bundle, user)
            }
        }

        return binding.root
    }

    private fun changeLikeStatus(
        event: Event,
        userId: User
    ) {
        if (event.like!!.any { it == userId.id }) {
            viewModel.setLike(userId.id, event, false)
            event.like?.remove(userId.id)
            binding.icLike.setBackgroundResource(R.drawable.ic_good_circle)
        } else {
            viewModel.setLike(userId.id, event, true)
            event.like?.add(userId.id)
            binding.icLike.setBackgroundResource(R.drawable.ic_like_selected)
        }
        viewModel.eventData.value = event
    }

    private fun showGameInfo(it: View) {
        if (it.tag == "empty") {
            it.tag = "select"
            binding.constraintGameInfo.visibility = View.VISIBLE
        } else {
            it.tag = "empty"
            binding.constraintGameInfo.visibility = View.GONE
        }
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
            viewModel.imagesUri.value = it.toString()
        }.addOnFailureListener { exception ->
            Toast.makeText(this.requireContext(), exception.message, Toast.LENGTH_SHORT).show()
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
}
