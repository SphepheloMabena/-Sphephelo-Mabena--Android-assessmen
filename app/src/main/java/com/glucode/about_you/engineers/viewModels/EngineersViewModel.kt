package com.glucode.about_you.engineers.viewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.glucode.about_you.common.PreferencesManager
import com.glucode.about_you.common.SharedPreferenncesManager
import com.glucode.about_you.engineers.models.Engineer
import com.glucode.about_you.engineers.models.updateImage
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EngineersViewModel(val sharedPref: SharedPreferenncesManager): ViewModel() {

    private var _engineersWithImages = MutableLiveData<List<Engineer>>()
    val engineerWithImages: LiveData<List<Engineer>> = _engineersWithImages


    fun updateEngineerImages(engineers: List<Engineer>) {
        val lastIndex = engineers.size - 1
        val prefs = PreferencesManager()
        val updatedEngineers = ArrayList<Engineer>()
        engineers.forEachIndexed { index, engineer ->
            val imgUrl = sharedPref.readString(engineer.name)

            val updatedEngineer = engineer.updateImage(imgUrl)
            updatedEngineers.add(updatedEngineer)
            if (index == lastIndex) {
                _engineersWithImages.value = updatedEngineers
            }

            /*viewModelScope.launch { prefs.getStringPref(context, engineer.name).first().let{ imgUrl ->
                var updatedEngineer = engineer
                imgUrl?.let{
                    updatedEngineer = engineer.updateImage(imgUrl)
                }
                updatedEngineers.add(updatedEngineer)

                if (index == lastIndex) {
                    _engineersWithImages.value = updatedEngineers
                }

            } }*/
        }
    }

    fun resetList() {
        //_engineersWithImages = MutableLiveData<List<Engineer>>()
    }

    fun sortByYears(engineers: List<Engineer>? = _engineersWithImages.value) {
        engineers?.let {
            val unSortedList = it
            val sorted = unSortedList.sortedWith(compareBy {it.quickStats.years})
            _engineersWithImages.value = sorted.asReversed()
        }
    }

    fun sortByCoffees(engineers: List<Engineer>? = _engineersWithImages.value) {
        engineers?.let {
            val unSortedList = it
            val sorted = unSortedList.sortedWith(compareBy {it.quickStats.coffees})
            _engineersWithImages.value = sorted.asReversed()
        }
    }
    fun sortByBugs(engineers: List<Engineer>? = _engineersWithImages.value) {
        engineers?.let {
            val unSortedList = it
            val sorted = unSortedList.sortedWith(compareBy {it.quickStats.bugs})
            _engineersWithImages.value = sorted.asReversed()
        }
    }


}