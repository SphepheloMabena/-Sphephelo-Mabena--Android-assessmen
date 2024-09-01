package com.glucode.about_you.about

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.io.File

import androidx.core.content.FileProvider
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.glucode.about_you.about.viewModels.AboutViewModel
import com.glucode.about_you.about.views.ProfileCustomView
import com.glucode.about_you.about.views.QuestionCardView
import com.glucode.about_you.common.ImagePickerContract
import com.glucode.about_you.common.PreferencesManager
import com.glucode.about_you.common.SharedPreferenncesManager
import com.glucode.about_you.databinding.FragmentAboutBinding
import com.glucode.about_you.mockdata.MockData
import kotlinx.coroutines.launch

class AboutFragment: Fragment() {
    private lateinit var binding: FragmentAboutBinding
    private lateinit var viewModel:AboutViewModel
    private val imagePickerLauncher = registerForActivityResult(ImagePickerContract()) { uri ->
        uri?.let {
            val engineerName = arguments?.getString("name")
            val profile = binding.container.get(PROFILE_VIEW_INDEX) as ProfileCustomView
            val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            requireContext().contentResolver.takePersistableUriPermission(uri, takeFlags)
            profile.imageUri = it
            engineerName?.let { name -> viewModel.saveImage(name, it.toString()) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = AboutViewModel(requireContext())
        setUpProfileView()
        setUpQuestions()
        updateImage()
    }

    private fun setUpQuestions() {
        val engineerName = arguments?.getString("name")
        val engineer = MockData.engineers.first { it.name == engineerName }

        engineer.questions.forEach { question ->
            val questionView = QuestionCardView(requireContext())
            questionView.title = question.questionText
            questionView.answers = question.answerOptions
            questionView.selection = question.answer.index

            binding.container.addView(questionView)
        }
    }

    private fun setUpProfileView() {
        val engineerName = arguments?.getString("name")
        val profileView = ProfileCustomView(requireContext())
        profileView.position = arguments?.getString("position")
        profileView.name = engineerName
        profileView.onImageClick = { pickImage() }

        binding.container.addView(profileView)
    }

    private fun pickImage() {
        imagePickerLauncher.launch(Unit)
    }

    private fun updateImage() {
        val engineerName = arguments?.getString("name")
        engineerName?.let {
            val imgUrl = viewModel.getImageUriString(engineerName)
            imgUrl?.let {
                if (it.isNotEmpty()) {
                    val profile = binding.container.get(PROFILE_VIEW_INDEX) as ProfileCustomView
                    profile.imageUri = Uri.parse(imgUrl)
                }
            }
        }
    }

    companion object {
        private const val PROFILE_VIEW_INDEX = 0
    }
}