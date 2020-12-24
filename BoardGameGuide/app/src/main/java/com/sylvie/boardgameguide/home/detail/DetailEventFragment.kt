package com.sylvie.boardgameguide.home.detail

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
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.Message
import com.sylvie.boardgameguide.databinding.FragmentDetailEventBinding
import com.sylvie.boardgameguide.ext.getVmFactory
import com.sylvie.boardgameguide.game.detail.GameDetailFragmentDirections
import com.sylvie.boardgameguide.home.getTimeDate
import com.sylvie.boardgameguide.login.UserManager
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class DetailEventFragment : Fragment() {

    val viewModel by viewModels<DetailEventViewModel> { getVmFactory() }
    private val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0
    var localImageList = mutableListOf<String>()
    var filePath: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentDetailEventBinding.inflate(inflater, container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val storage = Firebase.storage
        val storageRef = storage.reference
        val bundle = DetailEventFragmentArgs.fromBundle(requireArguments()).event
        viewModel.getEventData.value = bundle
        viewModel.getEvent(bundle.id)
        bundle.game?.name?.let { viewModel.getGame(it) }

        bundle.playerList?.let {

            if(it.any { userId-> userId == UserManager.userToken }){
                binding.buttonJoin.setText(R.string.leave)
            }else{
                if(it.size >= bundle.playerLimit){
                    binding.buttonJoin.setText(R.string.no_more_place)
                    binding.buttonJoin.isEnabled = false
                }else{
                    binding.buttonJoin.setText(R.string.join)
                }
            }
        }

//        binding.textCreatedTime.text = getTimeDate(bundle.createdTime.toDate())

        val adapter = DetailEventPlayerAdapter(viewModel)
        val adapter2 = DetailEventPhotoAdapter(DetailEventPhotoAdapter.OnClickListener{
            checkPermission()
        }, viewModel)
        val adapter3 = DetailEventCommentAdapter(viewModel)

        binding.recyclerPlayer.adapter = adapter
        binding.recyclerPhoto.adapter = adapter2
        binding.recyclerComment.adapter = adapter3

        viewModel.getAllUsers()

        viewModel.photoPermission.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
//            adapter2.submitList(viewModel.toPhotoItems(it))

        })

        viewModel.imagesUri.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            viewModel.addPhoto(
                eventId = bundle.id,
                image = viewModel.imagesUri.value!!,
                status = true
            )
        })


        viewModel.getEventData2.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it.let {
                adapter2.submitList(viewModel.toPhotoItems(viewModel.add(it.image!!)))
            }
        })

        viewModel.photoStatus.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            viewModel.getEvent(bundle.id)
        })

        viewModel.localImageList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            uploadPhoto(storageRef)
        })

        // upload photo permission
        bundle.playerList?.let { viewModel.checkUserPermission(it) }


        viewModel.getAllUsers.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            adapter.submitList(bundle.playerList)
        })


        binding.buttonJoin.setOnClickListener {
            //判斷是否加入過
            UserManager.user.value?.let { userId ->
                viewModel.setPlayer(userId.id, bundle, true)
                if (bundle.playerList!!.any { it == userId.id }) {
                    bundle.playerList?.remove(userId.id)
                    viewModel.setPlayer(userId.id, bundle, false)
                    binding.buttonJoin.setText(R.string.join)
                    findNavController().navigate(R.id.action_global_deleteDialog)
                } else {
                    bundle.playerList?.add(userId.id)
                    binding.buttonJoin.setText(R.string.leave)
                    findNavController().navigate(R.id.action_global_joinDialog)
                }
                viewModel.getEventData.value = bundle
//                adapter.submitList(bundle.playerList)
                adapter.notifyDataSetChanged()
            }
        }


        binding.textStatus.setOnClickListener {
            findNavController().navigate(DetailEventFragmentDirections.actionGlobalNewPostFragment(viewModel.getGameData.value, bundle))
        }

        val dateString = SimpleDateFormat("MM/dd/yyyy HH:mm").format(Date(bundle.time))
        binding.textGameTime.text = dateString


        binding.buttonSortDown.setOnClickListener {
            if (it.tag == "empty") {
                it.tag = "select"
                binding.constraintGameInfo.visibility = View.VISIBLE
            } else {
                it.tag = "empty"
                binding.constraintGameInfo.visibility = View.GONE
            }
        }

        binding.textHostName.setOnClickListener {
            findNavController().navigate(DetailPostFragmentDirections.actionGlobalProfileFragment(bundle.user!!.id))
        }

        binding.imageHost.setOnClickListener {
            findNavController().navigate(DetailPostFragmentDirections.actionGlobalProfileFragment(bundle.user!!.id))

        }

        val db = FirebaseFirestore.getInstance()

        db.collection("Event")
            .orderBy("createdTime", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                value?.let {
                    val listResult = mutableListOf<Event>()

                    it.forEach { data ->
                        val d = data.toObject(Event::class.java)
                        listResult.add(d)

                    }
                    var b = listResult.filter { result-> result.id == bundle.id }[0]
                    adapter3.submitList(b.message)
                    adapter3.notifyDataSetChanged()
                }
            }

        binding.buttonSend.setOnClickListener {

            val data = Message(
                hostId = viewModel.getUserData.value!!.id,
                userName = UserManager.user.value?.name,
                message = binding.editComment.text.toString()
            )

            db.collection("Event").document(bundle.id)
                //No covering
                .update("message", FieldValue.arrayUnion(data))
                .addOnSuccessListener { documentReference ->
//                    documentReference.update("id", bundle.id)
                    Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference}")
                }
                .addOnFailureListener {
                    Log.d("TAG", "$it")
                    Toast.makeText(this.context, "Please sign in to post", Toast.LENGTH_SHORT)
                        .show()
                }

            binding.editComment.text = null
        }


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
                    viewModel.localImageList.value = localImageList
//                    Toast.makeText(this.requireContext(), filePath, Toast.LENGTH_SHORT).show()
//                    Glide.with(this.requireContext()).load(filePath).into(button_add_photo)
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


    //    val imageList = mutableListOf<String>()
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

    override fun onStart() {
        super.onStart()
        let {
//            requireActivity().toolbar.visibility = View.GONE
            requireActivity().bottomNavView.visibility = View.GONE
        }
    }

    override fun onStop() {
        super.onStop()
        let {
//            requireActivity().toolbar.visibility = View.VISIBLE
            requireActivity().bottomNavView.visibility = View.VISIBLE
        }
    }
}