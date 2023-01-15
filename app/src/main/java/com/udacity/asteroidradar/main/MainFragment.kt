package com.udacity.asteroidradar.main

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.main.AsteroidAdapter
import com.udacity.asteroidradar.databinding.FragmentMainBinding

@RequiresApi(Build.VERSION_CODES.N)
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        val adapter = AsteroidAdapter(AsteroidAdapter.AsteroidClickListener { asteroidId ->
            viewModel.onAsteroidClicked(asteroidId)
        })
        binding.asteroidRecycler.adapter = adapter

        //navigating to detail fragment
        viewModel.navigateToDetailFragment.observe(viewLifecycleOwner) { asteroid ->
            asteroid?.let {
                findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.navigatedToDetailFragment()
            }
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
