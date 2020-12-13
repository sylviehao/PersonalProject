package com.sylvie.boardgameguide.create

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import com.sylvie.boardgameguide.login.UserManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.data.Event
import com.sylvie.boardgameguide.data.Game
import com.sylvie.boardgameguide.data.Message
import com.sylvie.boardgameguide.databinding.FragmentNewPostBinding
import com.sylvie.boardgameguide.ext.getVmFactory
import com.sylvie.boardgameguide.home.detail.DetailPostPhotoAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_new_post.*
import java.io.File
import java.util.*


class NewPostFragment : Fragment() {


    private lateinit var binding: FragmentNewPostBinding
    val viewModel by viewModels<NewPostViewModel> { getVmFactory() }

    // Separate the situation from HomeFragment and from GameFragment
    var arg: Game? = null
    var event: Event? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewPostBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val adapter = NewPostPlayerAdapter()
        val adapter2 = NewPostPhotoAdapter()
        binding.recyclerPlayer.adapter = adapter
        binding.recyclerNewPostPhoto.adapter = adapter2


        arg = NewPostFragmentArgs.fromBundle(requireArguments()).game
        event = NewPostFragmentArgs.fromBundle(requireArguments()).event

        viewModel.game.value = arg
        viewModel.event.value = event

        //??
        adapter.submitList(event?.playerList)
        adapter2.submitList(event?.image)

//        val storageFirebase = FirebaseStorage.getInstance().reference
        var storage = Firebase.storage
        var storageRef = storage.reference
        var eventsRef = storageRef.child("events.jpg")
        var eventsImagesRef = storageRef.child("events/game.jpg")
//        eventsRef.name == eventsImagesRef.name
//        imagesRef = spaceRef.parent
//        val path = imagesRef?.path

        binding.buttonAddPhoto.setOnClickListener {
            checkPermission()
//            findNavController().navigate(R.id.action_global_uploadPhotoDialog)
        }

        binding.iconFinn.setOnClickListener {

            val file = Uri.fromFile(File("path/to/images/events.jpg"))
//            val metadata = StorageMetadata.Builder()
//                .setContentDisposition("game")
//                .setContentType("image/jpg")
//                .build()
            eventsRef = storageRef.child(file.lastPathSegment ?: "")
            val uploadTask = eventsRef.putFile(file)
            uploadTask.addOnFailureListener { exception ->
                textView.text = exception.message
            }.addOnSuccessListener {
                textView.text = "Success"
            }
//                ?.addOnProgressListener { taskSnapshot ->
//                val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
//                upload_progress.progress = progress
//                if (progress >= 100) {
//                    upload_progress.visibility = View.GONE
//                }
//            }
        }

        binding.editNewPostGameTime.setOnClickListener {
            SingleDateAndTimePickerDialog.Builder(context)
                .bottomSheet()
                .curved()
                .backgroundColor(resources.getColor(R.color.oliveGreen))
                .mainColor(Color.WHITE)
                .titleTextColor(Color.WHITE)
                //.stepSizeMinutes(15)
                //.todayText("aujourd'hui")
                .displayListener {}
                .title("Simple")
                .listener { date ->
                    binding.editNewPostGameTime.text = date.toString()
                    viewModel.date.value = date.time
                }
                .display()
        }

        binding.buttonNewPostCreate.setOnClickListener {

            val typeList= mutableListOf<String>()
            typeList.add(binding.editNewPostGameType.text.toString())

            val memberList = mutableListOf<String>()
            memberList.add(binding.editNewPostGameMember.text.toString())

            viewModel.addPost(
                topic = binding.editNewPostTopic.text.toString(),
                location = binding.editNewPostGameLocation.text.toString(),
                rules = binding.editNewPostGameRule.text.toString(),
                member = memberList,
                type = typeList,
                name = binding.editNewPostGameName.text.toString()
            )

            findNavController().navigate(R.id.action_global_homeFragment)
        }


        return binding.root
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                val filePath: String = ImagePicker.getFilePath(data) ?: ""
                if (filePath.isNotEmpty()) {
                    val imgPath = filePath
                    Toast.makeText(this.requireContext(), imgPath, Toast.LENGTH_SHORT).show()
                    Glide.with(this.requireContext()).load(filePath).into(button_add_photo)
                } else {
                    Toast.makeText(this.requireContext(), "Upload failed", Toast.LENGTH_SHORT).show()
                }
            }
            ImagePicker.RESULT_ERROR -> Toast.makeText(this.requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            else -> Toast.makeText(this.requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
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

    private fun checkPermission() {
        val permission = ActivityCompat.checkSelfPermission(this.requireContext(),
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            //未取得權限，向使用者要求允許權限
            ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE),
                MY_PERMISSIONS_REQUEST_READ_CONTACTS)
        } else {
            getLocalImg()
        }
    }

    private fun getLocalImg() {
        ImagePicker.with(this)
            .crop()                    //Crop image(Optional), Check Customization for more option
            .compress(1024)            //Final image size will be less than 1 MB(Optional)
            .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    }
}