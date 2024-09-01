package com.glucode.about_you.engineers

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.glucode.about_you.R
import com.glucode.about_you.databinding.ItemEngineerBinding
import com.glucode.about_you.engineers.models.Engineer

class EngineersRecyclerViewAdapter(
    private val context: Context,
    private var engineers: List<Engineer>,
    private val onClick: (Engineer) -> Unit
) : RecyclerView.Adapter<EngineersRecyclerViewAdapter.EngineerViewHolder>() {

    override fun getItemCount() = engineers.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EngineerViewHolder {
        return EngineerViewHolder(ItemEngineerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    fun updateAdapterList(devs: List<Engineer>) {
        engineers = devs
    }

    override fun onBindViewHolder(holder: EngineerViewHolder, position: Int) {
        holder.bind(engineers[position], onClick)
    }

    inner class EngineerViewHolder(private val binding: ItemEngineerBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(engineer: Engineer, onClick: (Engineer) -> Unit) {
            binding.name.text = engineer.name
            binding.role.text = engineer.role
            binding.root.setOnClickListener {
                onClick(engineer)
            }
            engineer.defaultImageName?.let { image ->
                if (image.isNotEmpty()) {
                    binding.profileImage.setImageURI(Uri.parse(image))
                } else {
                    binding.profileImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_person))
                }
            }
            //TODO - set profile picture
//            statusIcon.setDrawable(item.icon)
        }
    }
}