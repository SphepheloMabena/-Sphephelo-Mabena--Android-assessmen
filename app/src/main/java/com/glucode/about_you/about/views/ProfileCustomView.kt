package com.glucode.about_you.about.views

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import com.glucode.about_you.databinding.ProfileViewBinding


class ProfileCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {
    private var binding: ProfileViewBinding

    var name: String? = null
        set(value) {
            field = value
            binding.name.text = value
        }

    var position: String? = null
    set(value) {
        field = value
        binding.position.text = value
    }

    var imageUri: Uri? = null
        set(value) {
            field = value
            binding.profileImage.setImageURI(imageUri)
        }

    var onImageClick : () -> Unit = {}

    init {

        binding = ProfileViewBinding.inflate(LayoutInflater.from(context), this, true)
        binding.profileImage.setOnClickListener { onImageClick() }
    }
}

