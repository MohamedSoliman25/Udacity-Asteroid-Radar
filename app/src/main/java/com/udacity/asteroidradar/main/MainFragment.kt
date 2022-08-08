package com.udacity.asteroidradar.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.AsteroidItemBinding
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.domain.Asteroid

class MainFragment : Fragment() {

    private  val TAG = "MainFragment"
    private lateinit var binding: FragmentMainBinding
    lateinit var asteroidAdapter:AsteroidsAdapter


    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }

        ViewModelProvider(this, MainViewModel.Factory(activity.application)).get(MainViewModel::class.java)

    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
         binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         asteroidAdapter = AsteroidsAdapter(AsteroidClick {asteroid->
            asteroid?.let {
                findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
            }
        })

        viewModel.asteroid.observe(viewLifecycleOwner){
//            Log.d(TAG, "onViewCreated : name ${it}")
            asteroidAdapter.asteroids = it
        }

        binding.asteroidRecycler.apply {
            adapter = asteroidAdapter
        }

//        viewModel.pictureOfDay.observe(viewLifecycleOwner){
//            Log.d(TAG, "onViewCreated: testimg ${it.url}")
//        }


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.show_all_menu ->  viewModel.asteroid.observe(viewLifecycleOwner){
                asteroidAdapter.asteroids = it
            }
            R.id.show_rent_menu -> viewModel.todayAsteroid.observe(viewLifecycleOwner){
                asteroidAdapter.asteroids = it
            }
            R.id.show_buy_menu -> viewModel.asteroid.observe(viewLifecycleOwner){
                asteroidAdapter.asteroids = it
            }
        }
        return super.onOptionsItemSelected(item)
    }
    }

    class AsteroidClick(val block: (Asteroid) -> Unit) {

        fun onClick(asteroid: Asteroid) = block(asteroid)
    }

    class AsteroidsAdapter(val callback: AsteroidClick) : RecyclerView.Adapter<AsteroidsHolder>() {

        /**
         * The asteroids that our Adapter will show
         */
        var asteroids: List<Asteroid> = emptyList()
            set(value) {
                field = value
                // Notify any registered observers that the data set has changed. This will cause every
                // element in our RecyclerView to be invalidated.
                notifyDataSetChanged()
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidsHolder {
            val binding: AsteroidItemBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    AsteroidsHolder.LAYOUT,
                    parent,
                    false
            )
            return AsteroidsHolder(binding)
        }

        override fun onBindViewHolder(holder: AsteroidsHolder, position: Int) {
            holder.asteroidItemBinding.also {
                it.asteroid = asteroids[position]
                it.asteroidCallback = callback
            }
        }

        override fun getItemCount(): Int = asteroids.size

    }

    class AsteroidsHolder(val asteroidItemBinding: AsteroidItemBinding) :
            RecyclerView.ViewHolder(asteroidItemBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.asteroid_item
        }
    }


