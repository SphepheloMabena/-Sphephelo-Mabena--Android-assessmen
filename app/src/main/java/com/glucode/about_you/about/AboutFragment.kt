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
    val prefs = PreferencesManager()
    private lateinit var sharedPref: SharedPreferenncesManager;
    private val imagePickerLauncher = registerForActivityResult(ImagePickerContract()) { uri ->
        uri?.let {
            val profile = binding.container.get(PROFILE_VIEW_INDEX) as ProfileCustomView
            // Persist the access permissions
            val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            requireContext().contentResolver.takePersistableUriPermission(uri, takeFlags)
            profile.imageUri = it
            saveImage(it.toString())

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
        sharedPref =SharedPreferenncesManager(requireContext())
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

    private fun saveImage(uriString: String) {
        val engineerName = arguments?.getString("name")
        engineerName?.let { sharedPref.writeString(it, uriString) }

        /*lifecycleScope.launch {
            engineerName?.let { prefs.editStringPref(requireContext() ,it, uriString) }
        }*/
    }

    private  fun updateImage() {
        val engineerName = arguments?.getString("name")
        engineerName?.let {
            val imgUrl = sharedPref.readString(it)
            imgUrl?.let {
                if(it.isNotEmpty()) {
                    val profile = binding.container.get(PROFILE_VIEW_INDEX) as ProfileCustomView
                    profile.imageUri = Uri.parse(imgUrl)
                }
            }
        }
        /*lifecycleScope.launch {
            engineerName?.let { lifecycleScope.launch { prefs.getStringPref(requireContext(), engineerName).collect { imgUrl ->
                val profile = binding.container.get(PROFILE_VIEW_INDEX) as ProfileCustomView
                val contentResolver = requireContext().contentResolver
                //val file = File()
                //val uri = FileProvider.getUriForFile(context, "your.package.name.fileprovider", file)

                //val inputStream = contentResolver.openInputStream(uri)

                imgUrl?.let {
                    profile.imageUri = Uri.parse(imgUrl)
                }
                    //profile.imageUri = Uri.parse(imgUrl) }
            } } }
        }*/
    }

    companion object {
        private const val REQUEST_CODE = 233
        private const val PROFILE_VIEW_INDEX = 0
    }

}