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
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
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
import com.sylvie.boardgameguide.databinding.FragmentDetailEventBinding
import com.sylvie.boardgameguide.dialog.JoinDialog
import com.sylvie.boardgameguide.ext.getVmFactory
import com.sylvie.boardgameguide.game.detail.GameDetailFragmentDirections
import com.sylvie.boardgameguide.login.UserManager
import com.sylvie.boardgameguide.util.GetPhoto
import java.io.File

class DetailEventFragment : Fragment() {

    val viewModel by viewModels<DetailEventViewModel> { getVmFactory() }
    lateinit var binding: FragmentDetailEventBinding
    private val myPermissionsRequestRead = 0
    var localImageList = mutableListOf<String>()
    var filePath: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailEventBinding.inflate(inflater, container, false)
        val bundle = DetailEventFragmentArgs.fromBundle(requireArguments()).event
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        viewModel.eventData.value = bundle
        viewModel.getEvent(bundle.id)

        val storage = Firebase.storage
        val storageRef = storage.reference

        bundle.game?.name?.let { viewModel.getGame(it) }
        bundle.playerList?.let {
            checkJoinStatus(it, bundle)
            viewModel.checkPhotoPermission(it)
        }

        val adapterComment = DetailEventCommentAdapter(viewModel)
        val adapterTool = DetailEventToolAdapter(viewModel)
        val adapterPlayer = DetailEventPlayerAdapter(viewModel)
        val adapterPhoto = DetailEventPhotoAdapter(DetailEventPhotoAdapter.OnClickListener {
            context?.let { context ->
                GetPhoto.checkPermissionAndGetLocalImg(
                    context,
                    requireActivity(),
                    this
                )
            }
        }, viewModel)
        binding.recyclerPlayer.adapter = adapterPlayer
        binding.recyclerPhoto.adapter = adapterPhoto
        binding.recyclerComment.adapter = adapterComment
        binding.recyclerTools.adapter = adapterTool

        viewModel.profileNavigation.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                findNavController().navigate(
                    DetailEventFragmentDirections.actionGlobalProfileFragment(it)
                )
                viewModel.navigated()
            }
        })

        viewModel.imagesUri.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            viewModel.addPhoto(
                eventId = bundle.id,
                image = viewModel.imagesUri.value!!,
                status = true
            )
        })

        viewModel.eventData2.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it.image?.let { image->
                adapterPhoto.submitList(viewModel.toPhotoItems(viewModel.addImages(image)))
            }
        })

        viewModel.photoStatus.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            viewModel.getEvent(bundle.id)
        })

        viewModel.localImageList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            uploadPhoto(storageRef)
        })

        viewModel.allUsersData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            adapterPlayer.submitList(bundle.playerList)
        })

        viewModel.gameData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            adapterTool.submitList(bundle.game?.tools)
        })

        viewModel.toolNavigation.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                when (it) {
                    "Dice" -> findNavController().navigate(GameDetailFragmentDirections.actionGlobalDiceFragment())
                    "Timer" -> findNavController().navigate(GameDetailFragmentDirections.actionGlobalTimerFragment())
                    else -> findNavController().navigate(GameDetailFragmentDirections.actionGlobalBottleFragment())
                }
                viewModel.navigated()
            }
        })

        viewModel.joinStatus.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it.let {
                findNavController().navigate(
                    DetailEventFragmentDirections.actionGlobalJoinDialog(
                        JoinDialog.MessageType.JOIN
                    )
                )
            }
        })

        viewModel.leaveStatus.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it.let {
                findNavController().navigate(
                    DetailEventFragmentDirections.actionGlobalJoinDialog(
                        JoinDialog.MessageType.LEAVE
                    )
                )
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

        binding.buttonJoin.setOnClickListener {
            //Have joined or not
            UserManager.user.value?.let { user ->
                changeJoinStatus(bundle, user)
                adapterPlayer.notifyDataSetChanged()
            }
        }

        binding.textStatus.setOnClickListener {
            findNavController().navigate(
                DetailEventFragmentDirections.actionGlobalNewPostFragment(
                    viewModel.gameData.value, bundle
                )
            )
        }

        binding.buttonSortDown.setOnClickListener {
            showGameInfo(it)
        }

        binding.textHostName.setOnClickListener {
            findNavController().navigate(
                DetailPostFragmentDirections.actionGlobalProfileFragment(bundle.user!!.id)
            )
        }

        binding.imageHost.setOnClickListener {
            findNavController().navigate(
                DetailPostFragmentDirections.actionGlobalProfileFragment(bundle.user!!.id)
            )

        }

        return binding.root
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

    private fun changeJoinStatus(
        event: Event,
        userId: User
    ) {
        if (event.playerList!!.any { it == userId.id }) {
            viewModel.leaveStatus.value = true
            event.playerList?.remove(userId.id)
            viewModel.setPlayer(userId.id, event, false)
            binding.buttonJoin.setText(R.string.join)
        } else {
            viewModel.setPlayer(userId.id, event, true)
            viewModel.joinStatus.value = true
            event.playerList?.add(userId.id)
            binding.buttonJoin.setText(R.string.leave)
        }
        viewModel.eventData.value = event
    }

    private fun checkJoinStatus(
        players: MutableList<String>,
        bundle: Event
    ) {
        if (players.any { userId -> userId == UserManager.userToken }) {
            binding.buttonJoin.setText(R.string.leave)
        } else {
            if (players.size >= bundle.playerLimit) {
                binding.buttonJoin.setText(R.string.no_more_place)
                binding.buttonJoin.isEnabled = false
            } else {
                binding.buttonJoin.setText(R.string.join)
            }
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