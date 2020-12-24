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
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.Message
import com.sylvie.boardgameguide.databinding.FragmentDetailPostBinding
import com.sylvie.boardgameguide.ext.getVmFactory
import com.sylvie.boardgameguide.home.HomeAdapter
import com.sylvie.boardgameguide.home.getTimeDate
import com.sylvie.boardgameguide.login.UserManager
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class DetailPostFragment : Fragment() {

    val viewModel by viewModels<DetailPostViewModel> { getVmFactory() }
    private lateinit var binding: FragmentDetailPostBinding
    private val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0
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


        val storage = Firebase.storage
        val storageRef = storage.reference
        val bundle = DetailPostFragmentArgs.fromBundle(requireArguments()).event
        viewModel.getEventData.value = bundle
        viewModel.getEvent(bundle.id)

        val adapter = DetailPostCommentAdapter(viewModel)
        val adapter2 = DetailPostPhotoAdapter(DetailPostPhotoAdapter.OnClickListener{
                checkPermission()
        }, viewModel)


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

        val adapter3 = DetailPostPlayerAdapter(viewModel)
        binding.recyclerComment.adapter = adapter
        binding.recyclerPhoto.adapter = adapter2
        binding.recyclerPlayer.adapter = adapter3


        binding.textCreatedTime.text = getTimeDate(bundle.createdTime.toDate())


        // upload photo permission
        bundle.playerList?.let { viewModel.checkUserPermission(it) }

        viewModel.photoPermission.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

        })

        val db = FirebaseFirestore.getInstance()

        db.collection("Event")
//            .whereEqualTo(FieldPath.documentId(),bundle.id)
            .orderBy("createdTime", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                value?.let {
                    val listResult = mutableListOf<Event>()

                    it.forEach { data ->
                        val d = data.toObject(Event::class.java)
                        listResult.add(d)

                    }
                    var b = listResult.filter { result-> result.id == bundle.id }[0]
                    adapter.submitList(b.message)
//                  adapter.submitList(listResult[0].message)
                    adapter.notifyDataSetChanged()
                }
            }


//        val dateString = SimpleDateFormat("MM/dd/yyyy HH:mm").format(Date(bundle.time))
//        binding.textGameTime.text = dateString

        binding.icLike.setOnClickListener {
            UserManager.user.value?.let {userId->
                viewModel.setEvent(userId.id, bundle, true)
                if(bundle.like!!.any { it == userId.id }) {
                    bundle.like?.remove(userId.id)
                    viewModel.setEvent(userId.id, bundle, false)
                    binding.icLike.setBackgroundResource(R.drawable.ic_good_circle)
                }else{
                    bundle.like?.add(userId.id)
                    binding.icLike.setBackgroundResource(R.drawable.ic_like_selected)
                }
                viewModel.getEventData.value = bundle
            }
        }


//        bundle.image?.let { viewModel.add(it) }

        viewModel.getAllUsers.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            adapter3.submitList(bundle.playerList)
        })

        viewModel.localImageList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            uploadPhoto(storageRef)
        })

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

//        val db = FirebaseFirestore.getInstance()

//        db.collection("Game")
//            .get()
//            .addOnSuccessListener {
//                val listResult = mutableListOf<Game>()
//                it.forEach { data ->
//                    val d = data.toObject(Game::class.java)
//                    listResult.add(d)
//                }
//                viewModel.getGameData.value =
//                    listResult.filter { list -> list.id == bundle.gameId }[0]
//            }

        binding.imageHost.setOnClickListener {
            findNavController().navigate(DetailPostFragmentDirections.actionGlobalProfileFragment(bundle.user!!.id))

        }

        binding.textHostName.setOnClickListener {
            findNavController().navigate(DetailPostFragmentDirections.actionGlobalProfileFragment(bundle.user!!.id))
        }


        binding.buttonSortDown.setOnClickListener {
            if (it.tag == "empty") {
                it.tag = "select"
                binding.constraintGameInfo.visibility = View.VISIBLE
            } else {
                it.tag = "empty"
                binding.constraintGameInfo.visibility = View.GONE
            }
        }

        return binding.root
    }

    fun getTimeDate(timestamp: Date): String {
        try {
            val netDate = (timestamp)
            val sfd = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.TAIWAN)
            return sfd.format(netDate)
        } catch (e: Exception) {
            return "date"
        }
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
