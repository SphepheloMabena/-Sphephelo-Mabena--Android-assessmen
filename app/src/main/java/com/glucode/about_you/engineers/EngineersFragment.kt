package com.glucode.about_you.engineers

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.glucode.about_you.R
import com.glucode.about_you.common.PreferencesManager
import com.glucode.about_you.databinding.FragmentEngineersBinding
import com.glucode.about_you.engineers.models.Engineer
import com.glucode.about_you.engineers.models.updateImage
import com.glucode.about_you.mockdata.MockData
import com.glucode.about_you.viewModels.EngineersViewModel
import kotlinx.coroutines.launch

class EngineersFragment : Fragment() {
    private lateinit var binding: FragmentEngineersBinding
    private val viewModel: EngineersViewModel = EngineersViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEngineersBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        setUpEngineersList(emptyList())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        viewModel.updateEngineerImages(requireContext(), MockData.engineers)
        //viewModel.updateEngineerImages(requireContext(), MockData.engineers)
    }

    override fun onResume() {
        //viewModel.updateEngineerImages(requireContext(), MockData.engineers)
        super.onResume()

    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_engineers, menu)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_years ->  {
                viewModel.sortByYears()
                return true
            }
            R.id.action_coffees -> {
                viewModel.sortByCoffees()
                return true
            }
            R.id.action_bugs -> {
                viewModel.sortByBugs()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initObservers() {
        viewModel.engineerWithImages.observe(viewLifecycleOwner) { updatedEngineers ->
            val adapter = binding.list.adapter as EngineersRecyclerViewAdapter

            adapter.updateAdapterList(updatedEngineers)
            adapter.notifyDataSetChanged()

        }
    }

    override fun onPause() {
        viewModel.resetList()
        super.onPause()
    }

    private fun setUpEngineersList(engineers: List<Engineer>) {

        binding.list.adapter = EngineersRecyclerViewAdapter(requireContext(),engineers) {
            goToAbout(it)
        }
        val dividerItemDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.list.addItemDecoration(dividerItemDecoration)
    }

    private fun goToAbout(engineer: Engineer) {
        val bundle = Bundle().apply {
            putString("name", engineer.name)
            putString("position", engineer.role)
        }
        findNavController().navigate(R.id.action_engineersFragment_to_aboutFragment, bundle)
    }
}