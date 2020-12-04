package com.sylvie.boardgameguide.upload

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sylvie.boardgameguide.R
import com.sylvie.boardgameguide.databinding.DialogUploadPhotoBinding
import com.sylvie.boardgameguide.ext.getVmFactory
import com.sylvie.boardgameguide.ext.setTouchDelegate

class UploadPhotoDialog : BottomSheetDialogFragment() {

    private lateinit var binding: DialogUploadPhotoBinding
    val viewModel by viewModels<UploadPhotoViewModel> { getVmFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.custom_dialog)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DialogUploadPhotoBinding.inflate(inflater, container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.buttonCamera.setTouchDelegate()
        binding.viewModel = viewModel

//        viewModel.leave.observe(viewLifecycleOwner, Observer {
//            it?.let {
//                dismiss()
//                viewModel.onLeaveCompleted()
//            }
//        })



        return binding.root
    }

//    override fun dismiss() {
//        binding.layoutUpload.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_slide_down))
//        Handler().postDelayed({ super.dismiss() }, 200)
//    }
}