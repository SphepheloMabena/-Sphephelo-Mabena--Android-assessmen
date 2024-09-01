package com.glucode.about_you


import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.glucode.about_you.common.SharedPreferenncesManager
import com.glucode.about_you.engineers.models.Engineer
import com.glucode.about_you.engineers.models.QuickStats
import com.glucode.about_you.mockdata.MockData
import com.glucode.about_you.engineers.viewModels.EngineersViewModel
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EngineersViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var context: Context
    private lateinit var sharedPref: SharedPreferenncesManager
    private lateinit var observer: Observer<List<Engineer>>

    private lateinit var viewModel: EngineersViewModel

    @Before
    fun setup() {
        sharedPref = mockk(relaxed = true)
        observer = mockk(relaxed = true)

        viewModel = EngineersViewModel(sharedPref)
        viewModel.engineerWithImages.observeForever(observer)
    }

    @Test
    fun `When updateEngineerImages is called Then fetch image url from prefs and update hte list`() {
        val reneenImage = "hello.png"

        every { sharedPref.readString("Reenen") } returns reneenImage

        viewModel.updateEngineerImages(MockData.engineers)

        viewModel.engineerWithImages.value!!.forEachIndexed { index, engineer ->
            assertEquals(engineer.defaultImageName, TestingExpectedData.engineersWithImages[index].defaultImageName)
        }

        val reneenData = viewModel.engineerWithImages.value!!.find { it.name == "Reenen" }

        assertEquals(reneenImage, reneenData!!.defaultImageName)

    }

    @Test
    fun `When sort by years is Clicked Then Sort the engineers by the years, starting from highest`() {
        viewModel.sortByYears(MockData.engineers)

        viewModel.engineerWithImages.value!!.forEachIndexed { index, engineer ->
            assertEquals(engineer.name, TestingExpectedData.engineersYears[index].name)
        }
    }

    @Test
    fun `When sort by coffees is Clicked Then Sort the engineers by the coffee, starting from higheys`() {
        viewModel.sortByCoffees(MockData.engineers)

        viewModel.engineerWithImages.value!!.forEachIndexed { index, engineer ->
            assertEquals(engineer.name, TestingExpectedData.engineersCoffes[index].name)
        }
    }

    @Test
    fun `When sort by bugs is Clicked Then Sort the engineers by the bugs, starting from highest`() {
        viewModel.sortByBugs(MockData.engineers)

        viewModel.engineerWithImages.value!!.forEachIndexed { index, engineer ->
            assertEquals(engineer.name, TestingExpectedData.engineersBugs[index].name)
        }
    }
}
